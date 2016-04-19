(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .controller('DocumentoDialogController', DocumentoDialogController);

    DocumentoDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Documento'];

    function DocumentoDialogController ($scope, $stateParams, $uibModalInstance, entity, Documento) {
        var vm = this;
        vm.documento = entity;
        vm.load = function(id) {
            Documento.get({id : id}, function(result) {
                vm.documento = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('inventarioApp:documentoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.documento.id !== null) {
                Documento.update(vm.documento, onSaveSuccess, onSaveError);
            } else {
                Documento.save(vm.documento, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
