(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .controller('DocumentoDetailController', DocumentoDetailController);

    DocumentoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Documento'];

    function DocumentoDetailController($scope, $rootScope, $stateParams, entity, Documento) {
        var vm = this;
        vm.documento = entity;
        vm.load = function (id) {
            Documento.get({id: id}, function(result) {
                vm.documento = result;
            });
        };
        var unsubscribe = $rootScope.$on('inventarioApp:documentoUpdate', function(event, result) {
            vm.documento = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
