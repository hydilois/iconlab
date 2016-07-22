(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .factory('CompteSearch', CompteSearch);

    CompteSearch.$inject = ['$resource'];

    function CompteSearch($resource) {
        var resourceUrl =  'api/_search/comptes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
