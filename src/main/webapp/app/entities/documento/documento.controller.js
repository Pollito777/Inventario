(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .controller('DocumentoController', DocumentoController);

    DocumentoController.$inject = ['$scope', '$state', 'Documento'];

    function DocumentoController ($scope, $state, Documento) {
        var vm = this;
        vm.documentos = [];
        vm.loadAll = function() {
            Documento.query(function(result) {
                vm.documentos = result;
            });
        };

        vm.loadAll();
        
    }
})();
