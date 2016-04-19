(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .controller('LinSolicitudDetailController', LinSolicitudDetailController);

    LinSolicitudDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'LinSolicitud'];

    function LinSolicitudDetailController($scope, $rootScope, $stateParams, entity, LinSolicitud) {
        var vm = this;
        vm.linSolicitud = entity;
        vm.load = function (id) {
            LinSolicitud.get({id: id}, function(result) {
                vm.linSolicitud = result;
            });
        };
        var unsubscribe = $rootScope.$on('inventarioApp:linSolicitudUpdate', function(event, result) {
            vm.linSolicitud = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
