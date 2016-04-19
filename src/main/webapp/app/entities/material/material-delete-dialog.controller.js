(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .controller('MaterialDeleteController',MaterialDeleteController);

    MaterialDeleteController.$inject = ['$uibModalInstance', 'entity', 'Material'];

    function MaterialDeleteController($uibModalInstance, entity, Material) {
        var vm = this;
        vm.material = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Material.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
