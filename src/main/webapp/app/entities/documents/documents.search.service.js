(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .factory('DocumentsSearch', DocumentsSearch);

    DocumentsSearch.$inject = ['$resource'];

    function DocumentsSearch($resource) {
        var resourceUrl =  'api/_search/documents/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
