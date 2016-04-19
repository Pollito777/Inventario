(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .controller('PedidoController', PedidoController);

    PedidoController.$inject = ['$scope', '$state', 'Pedido'];

    function PedidoController ($scope, $state, Pedido) {
        var vm = this;
        vm.pedidos = [];
        vm.loadAll = function() {
            Pedido.query(function(result) {
                vm.pedidos = result;
            });
        };

        vm.loadAll();
        
    }
})();
