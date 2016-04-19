(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .controller('SolicitudCompraDialogController', SolicitudCompraDialogController);

    SolicitudCompraDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'SolicitudCompra'];

    function SolicitudCompraDialogController ($scope, $stateParams, $uibModalInstance, entity, SolicitudCompra) {
        var vm = this;
        vm.solicitudCompra = entity;
        vm.load = function(id) {
            SolicitudCompra.get({id : id}, function(result) {
                vm.solicitudCompra = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('inventarioApp:solicitudCompraUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.solicitudCompra.id !== null) {
                SolicitudCompra.update(vm.solicitudCompra, onSaveSuccess, onSaveError);
            } else {
                SolicitudCompra.save(vm.solicitudCompra, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
