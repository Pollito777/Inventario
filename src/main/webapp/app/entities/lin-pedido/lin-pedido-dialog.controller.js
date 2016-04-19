(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .controller('LinPedidoDialogController', LinPedidoDialogController);

    LinPedidoDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'LinPedido'];

    function LinPedidoDialogController ($scope, $stateParams, $uibModalInstance, entity, LinPedido) {
        var vm = this;
        vm.linPedido = entity;
        vm.load = function(id) {
            LinPedido.get({id : id}, function(result) {
                vm.linPedido = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('inventarioApp:linPedidoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.linPedido.id !== null) {
                LinPedido.update(vm.linPedido, onSaveSuccess, onSaveError);
            } else {
                LinPedido.save(vm.linPedido, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
