(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .controller('ProyectoController', ProyectoController);

    ProyectoController.$inject = ['$scope', '$state', 'Proyecto'];

    function ProyectoController ($scope, $state, Proyecto) {
        var vm = this;
        vm.proyectos = [];
        vm.loadAll = function() {
            Proyecto.query(function(result) {
                vm.proyectos = result;
            });
        };

        vm.loadAll();
        
    }
})();
