'use strict';

describe('Controller Tests', function() {

    describe('SolicitudCompra Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockSolicitudCompra;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockSolicitudCompra = jasmine.createSpy('MockSolicitudCompra');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'SolicitudCompra': MockSolicitudCompra
            };
            createController = function() {
                $injector.get('$controller')("SolicitudCompraDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'inventarioApp:solicitudCompraUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
