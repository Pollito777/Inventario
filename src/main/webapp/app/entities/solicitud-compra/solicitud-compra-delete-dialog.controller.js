(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .controller('SolicitudCompraDeleteController',SolicitudCompraDeleteController);

    SolicitudCompraDeleteController.$inject = ['$uibModalInstance', 'entity', 'SolicitudCompra'];

    function SolicitudCompraDeleteController($uibModalInstance, entity, SolicitudCompra) {
        var vm = this;
        vm.solicitudCompra = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            SolicitudCompra.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
