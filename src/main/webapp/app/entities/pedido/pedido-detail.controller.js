(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .controller('PedidoDetailController', PedidoDetailController);

    PedidoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Pedido'];

    function PedidoDetailController($scope, $rootScope, $stateParams, entity, Pedido) {
        var vm = this;
        vm.pedido = entity;
        vm.load = function (id) {
            Pedido.get({id: id}, function(result) {
                vm.pedido = result;
            });
        };
        var unsubscribe = $rootScope.$on('inventarioApp:pedidoUpdate', function(event, result) {
            vm.pedido = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
