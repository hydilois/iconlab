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
                        data.fromt = DateUtils.convertLocalDateFromServer(data.fromt);
                        data.tot = DateUtils.convertLocalDateFromServer(data.tot);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.fromt = DateUtils.convertLocalDateToServer(data.fromt);
                    data.tot = DateUtils.convertLocalDateToServer(data.tot);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.fromt = DateUtils.convertLocalDateToServer(data.fromt);
                    data.tot = DateUtils.convertLocalDateToServer(data.tot);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
