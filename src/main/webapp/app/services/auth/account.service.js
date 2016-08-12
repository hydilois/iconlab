(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .factory('Account', Account);
    angular
        .module('iconlabApp')
        .factory('StatService', StatService);

    Account.$inject = ['$resource'];
    StatService.$inject =['$http']

    function Account ($resource,$http) {
        var service = $resource('api/account', {}, {
            'get': { method: 'GET', params: {}, isArray: false,
                interceptor: {
                    response: function(response) {
                        // expose response
                        return response;
                    }
                }
            }
        });

        return service;
    }
    function StatService($http){
        return{
            getStatData: function () {
                return $http.get("api/audits/statData")
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
})();
