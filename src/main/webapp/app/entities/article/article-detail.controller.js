(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .controller('ArticleDetailController', ArticleDetailController);

    ArticleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'Article', 'User'];

    function ArticleDetailController($scope, $rootScope, $stateParams, DataUtils, entity, Article, User) {
        var vm = this;

        vm.article = entity;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('iconlabApp:articleUpdate', function(event, result) {
            vm.article = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
