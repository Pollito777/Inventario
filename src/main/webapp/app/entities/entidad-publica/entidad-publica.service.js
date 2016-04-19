(function() {
    'use strict';
    angular
        .module('inventarioApp')
        .factory('EntidadPublica', EntidadPublica);

    EntidadPublica.$inject = ['$resource'];

    function EntidadPublica ($resource) {
        var resourceUrl =  'api/entidad-publicas/:id';

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
