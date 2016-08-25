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
            },

            getSampleTimespans: function() {
                return [
                        {
                            from: new Date(),
                            to: new Date(),
                            name: 'Sprint 1 Timespan',
                            priority: undefined,
                            classes: [],
                            data: undefined
                        }
                    ];
            },
            getTacheByProjetGantt: function(id) {
                    return $http.get("api/tachesGantt/projet/" +id)
                    .then(function (response) {
                            return response.data;
                        },
                        function (errResponse) {
                            console.error("Erreur de recuperation de la liste des projets par compte");
                        }
                    );
            },
            getStatData: function () {
                return $http.get("api/taches/statData")
                    .then(function (response) {
                            return response.data;
                        },
                        function (errResponse) {
                            console.error("Erreur de recuperation de l'objet CounterStatistik ");
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
                        data.fromt = DateUtils.convertDateTimeFromServer(data.fromt);
                        data.tot = DateUtils.convertDateTimeFromServer(data.tot);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
