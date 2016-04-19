(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .controller('FlujoPedidoDetailController', FlujoPedidoDetailController);

    FlujoPedidoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'FlujoPedido'];

    function FlujoPedidoDetailController($scope, $rootScope, $stateParams, entity, FlujoPedido) {
        var vm = this;
        vm.flujoPedido = entity;
        vm.load = function (id) {
            FlujoPedido.get({id: id}, function(result) {
                vm.flujoPedido = result;
            });
        };
        var unsubscribe = $rootScope.$on('inventarioApp:flujoPedidoUpdate', function(event, result) {
            vm.flujoPedido = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
