'use strict';

describe('Controller Tests', function() {

    describe('Rol Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockRol, MockPermiso;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockRol = jasmine.createSpy('MockRol');
            MockPermiso = jasmine.createSpy('MockPermiso');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Rol': MockRol,
                'Permiso': MockPermiso
            };
            createController = function() {
                $injector.get('$controller')("RolDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'inventarioApp:rolUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
