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
                            name: 'Sprint 1 Timespan'
                            //priority: undefined,
                            //classes: [],
                            //data: undefined
                        }
                    ];
            },
            getSampleData: function() {
                var data =  [
                        
                        {name: 'Content', tasks: [
                            {name: 'Content', color: '#F1C232', from: new Date(2013, 10, 26, 9, 0, 0), to: new Date(2013, 10, 29, 16, 0, 0), progress : 50}
                        ]},
                        {name: 'Documentation', tasks: [
                            {name: 'Documentation', color: '#F1C232', from: new Date(2013, 10, 26, 8, 0, 0), to: new Date(2013, 10, 28, 18, 0, 0)}
                        ]}
                    ];
                    console.log(data);
                return data;
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
