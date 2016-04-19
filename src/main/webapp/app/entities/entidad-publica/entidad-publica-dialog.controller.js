(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .controller('EntidadPublicaDialogController', EntidadPublicaDialogController);

    EntidadPublicaDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'EntidadPublica', 'Proyecto'];

    function EntidadPublicaDialogController ($scope, $stateParams, $uibModalInstance, entity, EntidadPublica, Proyecto) {
        var vm = this;
        vm.entidadPublica = entity;
        vm.proyectos = Proyecto.query();
        vm.load = function(id) {
            EntidadPublica.get({id : id}, function(result) {
                vm.entidadPublica = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('inventarioApp:entidadPublicaUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.entidadPublica.id !== null) {
                EntidadPublica.update(vm.entidadPublica, onSaveSuccess, onSaveError);
            } else {
                EntidadPublica.save(vm.entidadPublica, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
