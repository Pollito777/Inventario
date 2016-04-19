(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .controller('ProyectoDialogController', ProyectoDialogController);

    ProyectoDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Proyecto'];

    function ProyectoDialogController ($scope, $stateParams, $uibModalInstance, entity, Proyecto) {
        var vm = this;
        vm.proyecto = entity;
        vm.load = function(id) {
            Proyecto.get({id : id}, function(result) {
                vm.proyecto = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('inventarioApp:proyectoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.proyecto.id !== null) {
                Proyecto.update(vm.proyecto, onSaveSuccess, onSaveError);
            } else {
                Proyecto.save(vm.proyecto, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
