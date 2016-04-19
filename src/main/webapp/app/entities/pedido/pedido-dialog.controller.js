(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .controller('PedidoDialogController', PedidoDialogController);

    PedidoDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Pedido'];

    function PedidoDialogController ($scope, $stateParams, $uibModalInstance, entity, Pedido) {
        var vm = this;
        vm.pedido = entity;
        vm.load = function(id) {
            Pedido.get({id : id}, function(result) {
                vm.pedido = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('inventarioApp:pedidoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.pedido.id !== null) {
                Pedido.update(vm.pedido, onSaveSuccess, onSaveError);
            } else {
                Pedido.save(vm.pedido, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
