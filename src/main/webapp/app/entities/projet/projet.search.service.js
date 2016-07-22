(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .factory('ProjetSearch', ProjetSearch);

    ProjetSearch.$inject = ['$resource'];

    function ProjetSearch($resource) {
        var resourceUrl =  'api/_search/projets/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
