(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .controller('DocumentoDeleteController',DocumentoDeleteController);

    DocumentoDeleteController.$inject = ['$uibModalInstance', 'entity', 'Documento'];

    function DocumentoDeleteController($uibModalInstance, entity, Documento) {
        var vm = this;
        vm.documento = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Documento.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
