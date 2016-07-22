(function() {
    'use strict';
    angular
        .module('iconlabApp')
        .factory('PointAvancement', PointAvancement);

    PointAvancement.$inject = ['$resource', 'DateUtils'];

    function PointAvancement ($resource, DateUtils) {
        var resourceUrl =  'api/point-avancements/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.datePub = DateUtils.convertDateTimeFromServer(data.datePub);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
