(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .controller('HomeController', HomeController);
        angular
        .module('iconlabApp')
        .controller('AcceuilInfoController', AcceuilInfoController);

    HomeController.$inject = ['$scope', 'Principal','StatService','Article','Projet', 'LoginService', '$state'];
    HomeController.$inject = ['$scope', 'Principal','Article','Projet', 'LoginService', '$state','Documents','DataUtils'];
    AcceuilInfoController.$inject = ['$scope', 'Principal','Article','Projet', 'LoginService', '$state'];

    function HomeController ($scope, Principal,StatService,Article,Projet,LoginService, $state) {
    function HomeController ($scope, Principal,Article,Projet, LoginService, $state,Documents,DataUtils) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        vm.openFile = DataUtils.openFile;

        var info = StatService.getStatData();


        $scope.mydata = {
            type : "bar",
            "plot": {
                "value-box": {
                    "text": "%node-value"
                }
            },
            title:{
                backgroundColor : "transparent",
                fontColor :"black",
                text : "Hello world"
            },
            backgroundColor : "white",
            "scale-x":{
                "labels":["Taches","PA","Documents","Projets","MessagesH","Comptes","Articles"]
            },
            series : [
                {
                    values : [1,3,3,6,7,8,7],
                    backgroundColor : "#4DC0CF"
                }
            ]
        };

        //values : [info.tacheLength,info.pointAvancementLength,info.documentLength,info.projeteLength,info.messageHierachiqueLength,info.compteLength,info.articleLength,],


            vm.isAuthenticated = Principal.isAuthenticated;

        $scope.$on('authenticationSuccess', function() {
            getAccount();
            loadAllDocuments();
        });
        if(!vm.isAuthenticated()){
            loadAllArticles();
        }else{
        getAccount();
        loadAllDocuments();}

        if($state.params.id){
            loadAllProjetHome();
        }

        function getAccount() {
            Principal.identity().then(function(account){
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function loadAllArticles() {
            vm.listeArticlesTotal = [];
            Article.query().$promise.then(function (data) {
                vm.listeArticlesTotal = data;

            }, function () {
                console.log("Erreur de recuperation des données");
            });
        }

        function loadAllDocuments() {
            vm.listeDocumentsTotal = [];
            Documents.query().$promise.then(function (data) {
                vm.listeDocumentsTotal = data;
                vm.listeDocumentsPrive=[];
                vm.listeDocumentsPublic=[];
            for (var i = 0; i < vm.listeDocumentsTotal.length; i++) {
                if(vm.listeDocumentsTotal[i].mode==="PUBLIC"
                    && vm.listeDocumentsTotal[i].actif===true){
                    vm.listeDocumentsPublic.push(vm.listeDocumentsTotal[i]);
                }else if(vm.listeDocumentsTotal[i].mode==="PRIVE"
                    && vm.listeDocumentsTotal[i].actif===true
                    && vm.listeDocumentsTotal[i].user.email===vm.account.email){
                    vm.listeDocumentsPrive.push(vm.listeDocumentsTotal[i]);
                }
            }
            }, function () {
                console.log("Erreur de recuperation des données");
            });
        }

        function loadAllProjetHome() {
            vm.listeProjetsTotalhome = [];
            Projet.query().$promise.then(function (data) {
                vm.listeProjetsTotalhome = data;
                findProjetByCompte(vm.listeProjetsTotalhome,$state.params.id);
            }, function () {
                console.log("Erreur de recuperation des données");
            });
        }

        function findProjetByCompte(data, id){
            vm.listeProjetsByComptes = [];
            for(var i=0; i<data.length; i++){
                if(data[i].compte.id===id)
                    vm.listeProjetsByComptes.push(data[i]);
            }
        }

        function register () {
            $state.go('register');
        }

        $scope.animationcadre = function(){
            console.log("yessssssssssss");
            $('cadrearticle').addClass('tabAnim');
        }
    }

    function AcceuilInfoController ($scope, Principal,Article,Projet, LoginService, $state) {
        var vm = this;

        function loadAllArticles() {
            vm.listeArticlesTotal = [];
            Article.query().$promise.then(function (data) {
                vm.listeArticlesTotal = data;

            }, function () {
                console.log("Erreur de recuperation des données");
            });
        }
    }

})();

