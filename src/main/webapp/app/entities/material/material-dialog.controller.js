(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .controller('MaterialDialogController', MaterialDialogController);

    MaterialDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Material'];

    function MaterialDialogController ($scope, $stateParams, $uibModalInstance, entity, Material) {
        var vm = this;
        vm.material = entity;
        vm.load = function(id) {
            Material.get({id : id}, function(result) {
                vm.material = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('inventarioApp:materialUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.material.id !== null) {
                Material.update(vm.material, onSaveSuccess, onSaveError);
            } else {
                Material.save(vm.material, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
