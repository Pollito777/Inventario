(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .controller('FlujoCompraController', FlujoCompraController);

    FlujoCompraController.$inject = ['$scope', '$state', 'FlujoCompra'];

    function FlujoCompraController ($scope, $state, FlujoCompra) {
        var vm = this;
        vm.flujoCompras = [];
        vm.loadAll = function() {
            FlujoCompra.query(function(result) {
                vm.flujoCompras = result;
            });
        };

        vm.loadAll();
        
    }
})();
