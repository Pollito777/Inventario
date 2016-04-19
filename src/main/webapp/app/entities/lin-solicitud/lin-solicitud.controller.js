(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .controller('LinSolicitudController', LinSolicitudController);

    LinSolicitudController.$inject = ['$scope', '$state', 'LinSolicitud'];

    function LinSolicitudController ($scope, $state, LinSolicitud) {
        var vm = this;
        vm.linSolicituds = [];
        vm.loadAll = function() {
            LinSolicitud.query(function(result) {
                vm.linSolicituds = result;
            });
        };

        vm.loadAll();
        
    }
})();
