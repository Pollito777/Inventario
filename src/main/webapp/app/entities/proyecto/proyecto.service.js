(function() {
    'use strict';
    angular
        .module('inventarioApp')
        .factory('Proyecto', Proyecto);

    Proyecto.$inject = ['$resource'];

    function Proyecto ($resource) {
        var resourceUrl =  'api/proyectos/:id';

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
