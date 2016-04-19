(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .controller('FlujoPedidoDeleteController',FlujoPedidoDeleteController);

    FlujoPedidoDeleteController.$inject = ['$uibModalInstance', 'entity', 'FlujoPedido'];

    function FlujoPedidoDeleteController($uibModalInstance, entity, FlujoPedido) {
        var vm = this;
        vm.flujoPedido = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            FlujoPedido.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
