(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .controller('LinSolicitudDialogController', LinSolicitudDialogController);

    LinSolicitudDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'LinSolicitud'];

    function LinSolicitudDialogController ($scope, $stateParams, $uibModalInstance, entity, LinSolicitud) {
        var vm = this;
        vm.linSolicitud = entity;
        vm.load = function(id) {
            LinSolicitud.get({id : id}, function(result) {
                vm.linSolicitud = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('inventarioApp:linSolicitudUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.linSolicitud.id !== null) {
                LinSolicitud.update(vm.linSolicitud, onSaveSuccess, onSaveError);
            } else {
                LinSolicitud.save(vm.linSolicitud, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
