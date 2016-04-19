(function() {
    'use strict';
    angular
        .module('inventarioApp')
        .factory('Material', Material);

    Material.$inject = ['$resource'];

    function Material ($resource) {
        var resourceUrl =  'api/materials/:id';

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
