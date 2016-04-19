(function() {
    'use strict';
    angular
        .module('inventarioApp')
        .factory('FlujoCompra', FlujoCompra);

    FlujoCompra.$inject = ['$resource'];

    function FlujoCompra ($resource) {
        var resourceUrl =  'api/flujo-compras/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
