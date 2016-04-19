(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .controller('MaterialDetailController', MaterialDetailController);

    MaterialDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Material'];

    function MaterialDetailController($scope, $rootScope, $stateParams, entity, Material) {
        var vm = this;
        vm.material = entity;
        vm.load = function (id) {
            Material.get({id: id}, function(result) {
                vm.material = result;
            });
        };
        var unsubscribe = $rootScope.$on('inventarioApp:materialUpdate', function(event, result) {
            vm.material = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
