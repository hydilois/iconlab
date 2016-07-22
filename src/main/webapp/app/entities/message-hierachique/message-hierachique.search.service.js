(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .factory('MessageHierachiqueSearch', MessageHierachiqueSearch);

    MessageHierachiqueSearch.$inject = ['$resource'];

    function MessageHierachiqueSearch($resource) {
        var resourceUrl =  'api/_search/message-hierachiques/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
