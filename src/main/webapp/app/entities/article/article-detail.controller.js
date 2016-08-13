(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .controller('ArticleDetailController', ArticleDetailController);

    ArticleDetailController.$inject = ['$scope','$state', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'Article', 'User','Principal'];

    function ArticleDetailController($scope,$state, $rootScope, $stateParams, DataUtils, entity, Article, User,Principal) {
        var vm = this;

        vm.article = entity;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.isAuthenticated = Principal.isAuthenticated;

        var unsubscribe = $rootScope.$on('iconlabApp:articleUpdate', function(event, result) {
            vm.article = result;
        });
        $scope.$on('$destroy', unsubscribe);
        
        $scope.clickGo = function(){
            $state.go('home');
        }

    }
})();
