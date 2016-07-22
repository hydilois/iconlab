(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .factory('TacheSearch', TacheSearch);

    TacheSearch.$inject = ['$resource'];

    function TacheSearch($resource) {
        var resourceUrl =  'api/_search/taches/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
