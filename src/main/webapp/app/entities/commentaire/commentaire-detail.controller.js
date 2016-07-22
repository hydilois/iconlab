(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .controller('CommentaireDetailController', CommentaireDetailController);

    CommentaireDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Commentaire', 'Projet'];

    function CommentaireDetailController($scope, $rootScope, $stateParams, entity, Commentaire, Projet) {
        var vm = this;

        vm.commentaire = entity;

        var unsubscribe = $rootScope.$on('iconlabApp:commentaireUpdate', function(event, result) {
            vm.commentaire = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
