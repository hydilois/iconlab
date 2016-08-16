(function() {
    'use strict';
    angular
        .module('iconlabApp')
        .factory('Documents', Documents);

    angular
        .module('iconlabApp')
        .factory('DocumentSpecial', DocumentSpecial);

    Documents.$inject = ['$resource'];
    DocumentSpecial.$inject = ['$http', 'DateUtils'];

    function Documents ($resource) {
        var resourceUrl =  'api/documents/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    };
    function DocumentSpecial($http,DateUtils){
        return{
            getDocumentByCUser: function () {
                return $http.get("api/document/compteuser")
                    .then(function (response) {
                            return response.data;
                        },
                        function (errResponse) {
                            console.error("Erreur de recuperation de la liste des messages des projets par compte");
                        }
                    );
            }

        }
    }
})();
