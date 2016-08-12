(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .controller('ArticleDetailController', ArticleDetailController);

    ArticleDetailController.$inject = ['$scope','$state', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'Article', 'User'];

    function ArticleDetailController($scope,$state, $rootScope, $stateParams, DataUtils, entity, Article, User) {
        var vm = this;

        vm.article = entity;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('iconlabApp:articleUpdate', function(event, result) {
            vm.article = result;
        });
        $scope.$on('$destroy', unsubscribe);
        $scope.clickGo = function(){
            $state.go('home');
        }

    }
})();
