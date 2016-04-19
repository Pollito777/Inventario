(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .controller('ProyectoDetailController', ProyectoDetailController);

    ProyectoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Proyecto'];

    function ProyectoDetailController($scope, $rootScope, $stateParams, entity, Proyecto) {
        var vm = this;
        vm.proyecto = entity;
        vm.load = function (id) {
            Proyecto.get({id: id}, function(result) {
                vm.proyecto = result;
            });
        };
        var unsubscribe = $rootScope.$on('inventarioApp:proyectoUpdate', function(event, result) {
            vm.proyecto = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
