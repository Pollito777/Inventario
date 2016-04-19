(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .controller('EntidadPublicaDetailController', EntidadPublicaDetailController);

    EntidadPublicaDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'EntidadPublica', 'Proyecto'];

    function EntidadPublicaDetailController($scope, $rootScope, $stateParams, entity, EntidadPublica, Proyecto) {
        var vm = this;
        vm.entidadPublica = entity;
        vm.load = function (id) {
            EntidadPublica.get({id: id}, function(result) {
                vm.entidadPublica = result;
            });
        };
        var unsubscribe = $rootScope.$on('inventarioApp:entidadPublicaUpdate', function(event, result) {
            vm.entidadPublica = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
