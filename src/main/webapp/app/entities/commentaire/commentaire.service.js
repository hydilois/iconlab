(function() {
    'use strict';
    angular
        .module('iconlabApp')
        .factory('Commentaire', Commentaire);

    Commentaire.$inject = ['$resource', 'DateUtils'];

    function Commentaire ($resource, DateUtils) {
        var resourceUrl =  'api/commentaires/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.datePost = DateUtils.convertDateTimeFromServer(data.datePost);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
