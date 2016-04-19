(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .controller('LinSolicitudDeleteController',LinSolicitudDeleteController);

    LinSolicitudDeleteController.$inject = ['$uibModalInstance', 'entity', 'LinSolicitud'];

    function LinSolicitudDeleteController($uibModalInstance, entity, LinSolicitud) {
        var vm = this;
        vm.linSolicitud = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            LinSolicitud.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
