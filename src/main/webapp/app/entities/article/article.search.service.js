(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .factory('ArticleSearch', ArticleSearch);

    ArticleSearch.$inject = ['$resource'];

    function ArticleSearch($resource) {
        var resourceUrl =  'api/_search/articles/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
