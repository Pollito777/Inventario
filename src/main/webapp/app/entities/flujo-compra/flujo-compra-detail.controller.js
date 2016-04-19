(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .controller('FlujoCompraDetailController', FlujoCompraDetailController);

    FlujoCompraDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'FlujoCompra'];

    function FlujoCompraDetailController($scope, $rootScope, $stateParams, entity, FlujoCompra) {
        var vm = this;
        vm.flujoCompra = entity;
        vm.load = function (id) {
            FlujoCompra.get({id: id}, function(result) {
                vm.flujoCompra = result;
            });
        };
        var unsubscribe = $rootScope.$on('inventarioApp:flujoCompraUpdate', function(event, result) {
            vm.flujoCompra = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
