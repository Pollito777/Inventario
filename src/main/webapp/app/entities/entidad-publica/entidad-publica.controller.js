(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .controller('EntidadPublicaController', EntidadPublicaController);

    EntidadPublicaController.$inject = ['$scope', '$state', 'EntidadPublica'];

    function EntidadPublicaController ($scope, $state, EntidadPublica) {
        var vm = this;
        vm.entidadPublicas = [];
        vm.loadAll = function() {
            EntidadPublica.query(function(result) {
                vm.entidadPublicas = result;
            });
        };

        vm.loadAll();
        
    }
})();
