(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .controller('LinPedidoController', LinPedidoController);

    LinPedidoController.$inject = ['$scope', '$state', 'LinPedido'];

    function LinPedidoController ($scope, $state, LinPedido) {
        var vm = this;
        vm.linPedidos = [];
        vm.loadAll = function() {
            LinPedido.query(function(result) {
                vm.linPedidos = result;
            });
        };

        vm.loadAll();
        
    }
})();
