(function() {
    'use strict';
    angular
        .module('inventarioApp')
        .factory('LinPedido', LinPedido);

    LinPedido.$inject = ['$resource'];

    function LinPedido ($resource) {
        var resourceUrl =  'api/lin-pedidos/:id';

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
