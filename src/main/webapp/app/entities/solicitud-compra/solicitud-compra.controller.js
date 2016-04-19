(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .controller('SolicitudCompraController', SolicitudCompraController);

    SolicitudCompraController.$inject = ['$scope', '$state', 'SolicitudCompra'];

    function SolicitudCompraController ($scope, $state, SolicitudCompra) {
        var vm = this;
        vm.solicitudCompras = [];
        vm.loadAll = function() {
            SolicitudCompra.query(function(result) {
                vm.solicitudCompras = result;
            });
        };

        vm.loadAll();
        
    }
})();
