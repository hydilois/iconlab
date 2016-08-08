(function() {
    'use strict';
    angular
        .module('iconlabApp')
        .factory('PointAvancement', PointAvancement);

   
    angular
        .module('iconlabApp')
        .factory('PointAvancementSpecial', PointAvancementSpecial);

     PointAvancement.$inject = ['$resource', 'DateUtils'];

    PointAvancementSpecial.$inject = ['$http', 'DateUtils'];

    function PointAvancementSpecial($http,DateUtils){
        return{
            getPointAvancementByTache: function (id) {
                return $http.get("api/point-avancements/tache/" +id)
                    .then(function (response) {
                            return response.data;
                        },
                        function (errResponse) {
                            console.error("Erreur de recuperation de la liste des points d'avancement par tache");
                        }
                    );
            }
    }
    }

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
