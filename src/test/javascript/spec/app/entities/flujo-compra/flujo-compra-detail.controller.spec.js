'use strict';

describe('Controller Tests', function() {

    describe('FlujoCompra Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockFlujoCompra;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockFlujoCompra = jasmine.createSpy('MockFlujoCompra');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'FlujoCompra': MockFlujoCompra
            };
            createController = function() {
                $injector.get('$controller')("FlujoCompraDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'inventarioApp:flujoCompraUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
