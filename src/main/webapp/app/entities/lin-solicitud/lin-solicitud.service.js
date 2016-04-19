(function() {
    'use strict';
    angular
        .module('inventarioApp')
        .factory('LinSolicitud', LinSolicitud);

    LinSolicitud.$inject = ['$resource'];

    function LinSolicitud ($resource) {
        var resourceUrl =  'api/lin-solicituds/:id';

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
