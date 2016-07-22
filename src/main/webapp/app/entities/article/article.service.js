(function() {
    'use strict';
    angular
        .module('iconlabApp')
        .factory('Article', Article);

    Article.$inject = ['$resource', 'DateUtils'];

    function Article ($resource, DateUtils) {
        var resourceUrl =  'api/articles/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.datePub = DateUtils.convertLocalDateFromServer(data.datePub);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.datePub = DateUtils.convertLocalDateToServer(data.datePub);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.datePub = DateUtils.convertLocalDateToServer(data.datePub);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
