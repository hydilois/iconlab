(function() {
    'use strict';
    angular
        .module('iconlabApp')
        .factory('Projet', Projet);

    angular
        .module('iconlabApp')
        .factory('ProjetSpecial', ProjetSpecial);

    Projet.$inject = ['$resource', 'DateUtils'];
    ProjetSpecial.$inject = ['$http', 'DateUtils'];

    function ProjetSpecial($http,DateUtils){
        return{
            getProjetByCompte: function (id) {
                return $http.get("api/projets/compte/" +id)
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

    function Projet ($resource, DateUtils) {
        var resourceUrl =  'api/projets/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateDebut = DateUtils.convertLocalDateFromServer(data.dateDebut);
                        data.dateFin = DateUtils.convertLocalDateFromServer(data.dateFin);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.dateDebut = DateUtils.convertLocalDateToServer(data.dateDebut);
                    data.dateFin = DateUtils.convertLocalDateToServer(data.dateFin);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.dateDebut = DateUtils.convertLocalDateToServer(data.dateDebut);
                    data.dateFin = DateUtils.convertLocalDateToServer(data.dateFin);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
