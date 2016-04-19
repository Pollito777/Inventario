(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .controller('FlujoPedidoController', FlujoPedidoController);

    FlujoPedidoController.$inject = ['$scope', '$state', 'FlujoPedido'];

    function FlujoPedidoController ($scope, $state, FlujoPedido) {
        var vm = this;
        vm.flujoPedidos = [];
        vm.loadAll = function() {
            FlujoPedido.query(function(result) {
                vm.flujoPedidos = result;
            });
        };

        vm.loadAll();
        
    }
})();
