(function() {
    'use strict';
    angular
        .module('iconlabApp')
        .factory('Documents', Documents);

    Documents.$inject = ['$resource'];

    function Documents ($resource) {
        var resourceUrl =  'api/documents/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
