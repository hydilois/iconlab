(function() {
    'use strict';
    angular
        .module('iconlabApp')
        .factory('MessageHierachique', MessageHierachique);

    MessageHierachique.$inject = ['$resource', 'DateUtils'];

    function MessageHierachique ($resource, DateUtils) {
        var resourceUrl =  'api/message-hierachiques/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.date = DateUtils.convertDateTimeFromServer(data.date);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
