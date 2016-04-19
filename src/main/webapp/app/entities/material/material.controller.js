(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .controller('MaterialController', MaterialController);

    MaterialController.$inject = ['$scope', '$state', 'Material'];

    function MaterialController ($scope, $state, Material) {
        var vm = this;
        vm.materials = [];
        vm.loadAll = function() {
            Material.query(function(result) {
                vm.materials = result;
            });
        };

        vm.loadAll();
        
    }
})();
