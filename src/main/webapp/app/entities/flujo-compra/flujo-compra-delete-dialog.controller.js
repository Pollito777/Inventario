(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .controller('FlujoCompraDeleteController',FlujoCompraDeleteController);

    FlujoCompraDeleteController.$inject = ['$uibModalInstance', 'entity', 'FlujoCompra'];

    function FlujoCompraDeleteController($uibModalInstance, entity, FlujoCompra) {
        var vm = this;
        vm.flujoCompra = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            FlujoCompra.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
