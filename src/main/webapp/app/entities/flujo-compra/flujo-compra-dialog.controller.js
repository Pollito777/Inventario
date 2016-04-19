(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .controller('FlujoCompraDialogController', FlujoCompraDialogController);

    FlujoCompraDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'FlujoCompra'];

    function FlujoCompraDialogController ($scope, $stateParams, $uibModalInstance, entity, FlujoCompra) {
        var vm = this;
        vm.flujoCompra = entity;
        vm.load = function(id) {
            FlujoCompra.get({id : id}, function(result) {
                vm.flujoCompra = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('inventarioApp:flujoCompraUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.flujoCompra.id !== null) {
                FlujoCompra.update(vm.flujoCompra, onSaveSuccess, onSaveError);
            } else {
                FlujoCompra.save(vm.flujoCompra, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
