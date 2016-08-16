(function() {
    'use strict';
    angular
        .module('iconlabApp')
        .factory('MessageHierachique', MessageHierachique);

        angular
        .module('iconlabApp')
        .factory('MessageHierachiqueSpecial', MessageHierachiqueSpecial);

    MessageHierachique.$inject = ['$resource', 'DateUtils'];
    MessageHierachiqueSpecial.$inject = ['$http', 'DateUtils'];

    function MessageHierachiqueSpecial($http,DateUtils){
        return{
            getMessageByCompte: function (id) {
                return $http.get("api/message-hierachiques/compte/" +id)
                    .then(function (response) {
                            return response.data;
                        },
                        function (errResponse) {
                            console.error("Erreur de recuperation de la liste des messages des projets par compte");
                        }
                    );
            },
            getMessageByCompteUser: function () {
                return $http.get("api/message-hierachiques/compte/user")
                    .then(function (response) {
                            return response.data;
                        },
                        function (errResponse) {
                            console.error("Erreur de recuperation de la liste des messages par user");
                        }
                    );
            }

    };
    }

    function MessageHierachique ($resource, DateUtils) {
        var resourceUrl =  'api/message-hierachiques/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.date = DateUtils.convertDateTimeFromServer(data.date);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
