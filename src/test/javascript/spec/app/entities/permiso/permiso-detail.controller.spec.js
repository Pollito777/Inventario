'use strict';

describe('Controller Tests', function() {

    describe('Permiso Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPermiso, MockFormulario, MockUsuario, MockRol;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPermiso = jasmine.createSpy('MockPermiso');
            MockFormulario = jasmine.createSpy('MockFormulario');
            MockUsuario = jasmine.createSpy('MockUsuario');
            MockRol = jasmine.createSpy('MockRol');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Permiso': MockPermiso,
                'Formulario': MockFormulario,
                'Usuario': MockUsuario,
                'Rol': MockRol
            };
            createController = function() {
                $injector.get('$controller')("PermisoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'inventarioApp:permisoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
