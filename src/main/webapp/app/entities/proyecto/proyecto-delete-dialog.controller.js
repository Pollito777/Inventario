(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .controller('ProyectoDeleteController',ProyectoDeleteController);

    ProyectoDeleteController.$inject = ['$uibModalInstance', 'entity', 'Proyecto'];

    function ProyectoDeleteController($uibModalInstance, entity, Proyecto) {
        var vm = this;
        vm.proyecto = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Proyecto.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
