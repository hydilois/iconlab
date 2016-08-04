(function() {
    'use strict';
    angular
        .module('iconlabApp')
        .factory('Tache', Tache);

    angular
        .module('iconlabApp')
        .factory('TacheSpecial', TacheSpecial);

    Tache.$inject = ['$resource', 'DateUtils'];
    TacheSpecial.$inject = ['$http'];

    function TacheSpecial($http){
        return{
            getTacheByProjet: function (id) {
                return $http.get("api/taches/projet/" +id)
                    .then(function (response) {
                            return response.data;
                        },
                        function (errResponse) {
                            console.error("Erreur de recuperation de la liste des projets par compte");
                        }
                    );
            }
    }
    }

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
