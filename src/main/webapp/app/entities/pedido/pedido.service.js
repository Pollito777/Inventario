(function() {
    'use strict';
    angular
        .module('inventarioApp')
        .factory('Pedido', Pedido);

    Pedido.$inject = ['$resource'];

    function Pedido ($resource) {
        var resourceUrl =  'api/pedidos/:id';

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
