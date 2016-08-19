(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .controller('PointAvancementDetailController', PointAvancementDetailController);

    PointAvancementDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'PointAvancement', 'Tache'];

    angular
    .module('iconlabApp')
    .controller('PointAvancementTacheController', PointAvancementTacheController);


    PointAvancementTacheController.$inject = ['$rootScope','$scope','$state','entity','PointAvancementSpecial','CommentaireSpecial','Commentaire','Principal','MessageHierachiqueSpecial','DocumentSpecial'];

    function PointAvancementDetailController($scope, $rootScope, $stateParams, DataUtils, entity, PointAvancementSpecial, Tache) {
        var vm = this;

        vm.pointAvancement = entity;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('iconlabApp:pointAvancementUpdate', function(event, result) {
            vm.pointAvancement = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
     function PointAvancementTacheController($rootScope,$scope,$state,entity,PointAvancementSpecial,CommentaireSpecial,Commentaire,Principal,MessageHierachiqueSpecial,DocumentSpecial) {
        var vm = this;

        vm.tache = entity;
        console.log("projet associ"+vm.tache.projet.id);
        var unsubscribe = $rootScope.$on('iconlabApp:tacheUpdate', function(event, result) {
            vm.tache = result;
        });
        $scope.$on('$destroy', unsubscribe);

        function accessCurrentAccount(){
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.commentaire.auteur =vm.account.login;
                vm.commentaire.datePost = new Date();
                vm.commentaire.projet = vm.tache.projet;


            });
        }

        if($state.params.idtache){
            PointAvancementSpecial.getPointAvancementByTache($state.params.idtache).then(function(data){
                vm.listePa= data;
            },function(){
                console.log("Erreur");
            });
            CommentaireSpecial.getCommentaireByProject(vm.tache.projet.id).then(function(data){
                vm.listeCommentairesParTache= data;
            },function(){
                console.log("Erreur");
            });

            MessageHierachiqueSpecial.getMessageByProjet(vm.tache.projet.id).then(function(data){
                vm.listeMessageParProjet = data;
            },function(){
                console.log("Erreur");
            });
            DocumentSpecial.getDocumentByCUser().then(function(data){
                vm.listeDocumentUser = data;
                console.log(vm.listeDocumentUser);
            },function(){
                console.log("Erreur");
            });
        }

        $scope.savecomment = function () {
            //vm.isSaving = true;

            $state.go('app.patache', null, { reload: true });
                accessCurrentAccount();
                Commentaire.save(vm.commentaire);
               $state.go('app.patache', null, { reload: true });
        }

        $scope.select= function(item) {
            $scope.selected = item;
        };

        $scope.isActive = function(item) {
            return $scope.selected === item;
        };
    }
})();
