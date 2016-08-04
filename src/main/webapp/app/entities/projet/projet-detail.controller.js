(function() {
    'use strict';

    angular
    .module('iconlabApp')
    .controller('ProjetDetailController', ProjetDetailController);

    angular
    .module('iconlabApp')
    .controller('ProjetCompteController', ProjetCompteController);

    ProjetDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'Projet', 'Compte', 'MessageHierachique', 'Commentaire', 'Tache', 'User'];
    ProjetCompteController.$inject = ['$rootScope','$scope', 'ProjetSpecial','$state','entity'];

    function ProjetDetailController($scope, $rootScope, $stateParams, DataUtils, entity, Projet, Compte, MessageHierachique, Commentaire, Tache, User) {
        var vm = this;

        vm.projet = entity;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('iconlabApp:projetUpdate', function(event, result) {
            vm.projet = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }

    function ProjetCompteController($rootScope,$scope,ProjetSpecial,$state,entity) {
        var vm = this;

        vm.compte = entity;

        var unsubscribe = $rootScope.$on('iconlabApp:compteUpdate', function(event, result) {
            vm.compte = result;
        });
        $scope.$on('$destroy', unsubscribe);

        if($state.params.id){
            ProjetSpecial.getProjetByCompte($state.params.id).then(function(data){
                vm.listePr = data;
            },function(){
                console.log("Erreur");
            });
        }

        $scope.select= function(item) {
            $scope.selected = item; 
        };

        $scope.isActive = function(item) {
            return $scope.selected === item;
        };
    }
})();
