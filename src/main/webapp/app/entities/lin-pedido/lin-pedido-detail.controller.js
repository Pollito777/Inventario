(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .controller('LinPedidoDetailController', LinPedidoDetailController);

    LinPedidoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'LinPedido'];

    function LinPedidoDetailController($scope, $rootScope, $stateParams, entity, LinPedido) {
        var vm = this;
        vm.linPedido = entity;
        vm.load = function (id) {
            LinPedido.get({id: id}, function(result) {
                vm.linPedido = result;
            });
        };
        var unsubscribe = $rootScope.$on('inventarioApp:linPedidoUpdate', function(event, result) {
            vm.linPedido = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
