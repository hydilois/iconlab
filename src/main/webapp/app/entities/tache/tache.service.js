(function() {
    'use strict';
    angular
        .module('iconlabApp')
        .factory('Tache', Tache);

    Tache.$inject = ['$resource', 'DateUtils'];

    function Tache ($resource, DateUtils) {
        var resourceUrl =  'api/taches/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateDebut = DateUtils.convertDateTimeFromServer(data.dateDebut);
                        data.dateFin = DateUtils.convertDateTimeFromServer(data.dateFin);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
