(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .controller('PedidoDeleteController',PedidoDeleteController);

    PedidoDeleteController.$inject = ['$uibModalInstance', 'entity', 'Pedido'];

    function PedidoDeleteController($uibModalInstance, entity, Pedido) {
        var vm = this;
        vm.pedido = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Pedido.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
