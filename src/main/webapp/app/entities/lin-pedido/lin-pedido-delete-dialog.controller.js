(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .controller('LinPedidoDeleteController',LinPedidoDeleteController);

    LinPedidoDeleteController.$inject = ['$uibModalInstance', 'entity', 'LinPedido'];

    function LinPedidoDeleteController($uibModalInstance, entity, LinPedido) {
        var vm = this;
        vm.linPedido = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            LinPedido.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
