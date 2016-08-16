(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .factory('Account', Account);

    Account.$inject = ['$resource'];


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

})();
