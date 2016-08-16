(function() {
    'use strict';
    angular
        .module('iconlabApp')
        .factory('Commentaire', Commentaire);
    angular
        .module('iconlabApp')
        .factory('CommentaireSpecial', CommentaireSpecial);

    Commentaire.$inject = ['$resource', 'DateUtils'];
    CommentaireSpecial.$inject = ['$http'];
    
    
    function CommentaireSpecial($http){
        return{
            getCommentaireByProject: function (id) {
                return $http.get("api/commentaires/project/" +id)
                    .then(function (response) {
                            return response.data;
                        },
                        function (errResponse) {
                            console.error("Erreur de recuperation de la liste des commentaires par projet");
                        }
                    );
            }

    };
    }

    function Commentaire ($resource, DateUtils) {
        var resourceUrl =  'api/commentaires/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.datePost = DateUtils.convertDateTimeFromServer(data.datePost);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
