(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .controller('SolicitudCompraDetailController', SolicitudCompraDetailController);

    SolicitudCompraDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'SolicitudCompra'];

    function SolicitudCompraDetailController($scope, $rootScope, $stateParams, entity, SolicitudCompra) {
        var vm = this;
        vm.solicitudCompra = entity;
        vm.load = function (id) {
            SolicitudCompra.get({id: id}, function(result) {
                vm.solicitudCompra = result;
            });
        };
        var unsubscribe = $rootScope.$on('inventarioApp:solicitudCompraUpdate', function(event, result) {
            vm.solicitudCompra = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
