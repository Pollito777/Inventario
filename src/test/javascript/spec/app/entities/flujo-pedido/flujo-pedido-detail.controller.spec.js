'use strict';

describe('Controller Tests', function() {

    describe('FlujoPedido Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockFlujoPedido;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockFlujoPedido = jasmine.createSpy('MockFlujoPedido');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'FlujoPedido': MockFlujoPedido
            };
            createController = function() {
                $injector.get('$controller')("FlujoPedidoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'inventarioApp:flujoPedidoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
