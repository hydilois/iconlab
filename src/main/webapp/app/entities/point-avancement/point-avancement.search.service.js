(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .factory('PointAvancementSearch', PointAvancementSearch);

    PointAvancementSearch.$inject = ['$resource'];

    function PointAvancementSearch($resource) {
        var resourceUrl =  'api/_search/point-avancements/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
