(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .controller('FlujoPedidoDialogController', FlujoPedidoDialogController);

    FlujoPedidoDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'FlujoPedido'];

    function FlujoPedidoDialogController ($scope, $stateParams, $uibModalInstance, entity, FlujoPedido) {
        var vm = this;
        vm.flujoPedido = entity;
        vm.load = function(id) {
            FlujoPedido.get({id : id}, function(result) {
                vm.flujoPedido = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('inventarioApp:flujoPedidoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.flujoPedido.id !== null) {
                FlujoPedido.update(vm.flujoPedido, onSaveSuccess, onSaveError);
            } else {
                FlujoPedido.save(vm.flujoPedido, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
