(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .controller('EntidadPublicaDeleteController',EntidadPublicaDeleteController);

    EntidadPublicaDeleteController.$inject = ['$uibModalInstance', 'entity', 'EntidadPublica'];

    function EntidadPublicaDeleteController($uibModalInstance, entity, EntidadPublica) {
        var vm = this;
        vm.entidadPublica = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            EntidadPublica.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
