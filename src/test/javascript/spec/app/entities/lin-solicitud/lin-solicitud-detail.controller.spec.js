'use strict';

describe('Controller Tests', function() {

    describe('LinSolicitud Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLinSolicitud;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLinSolicitud = jasmine.createSpy('MockLinSolicitud');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LinSolicitud': MockLinSolicitud
            };
            createController = function() {
                $injector.get('$controller')("LinSolicitudDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'inventarioApp:linSolicitudUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
