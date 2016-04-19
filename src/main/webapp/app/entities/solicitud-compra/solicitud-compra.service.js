(function() {
    'use strict';
    angular
        .module('inventarioApp')
        .factory('SolicitudCompra', SolicitudCompra);

    SolicitudCompra.$inject = ['$resource'];

    function SolicitudCompra ($resource) {
        var resourceUrl =  'api/solicitud-compras/:id';

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
