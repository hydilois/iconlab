(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .controller('HomeController', HomeController);
        angular
        .module('iconlabApp')
        .controller('AcceuilInfoController', AcceuilInfoController);


    HomeController.$inject = ['$scope', 'Principal','TacheSpecial','Article','Projet', 'LoginService', '$state','Documents','DataUtils','User','Message'];
    AcceuilInfoController.$inject = ['$scope', 'Principal','Article','Projet', 'LoginService', '$state'];


    function HomeController ($scope, Principal,TacheSpecial,Article,Projet,LoginService, $state,Documents ,DataUtils,User,Message) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        vm.openFile = DataUtils.openFile;

        $scope.pageSizedocPub = 4;
        $scope.currentPagedocPub = 1;

        $scope.pageSizedocPriv = 4;
        $scope.currentPagedocPriv = 1;

        $scope.pageSizeUsers = 4;
        $scope.currentPageUsers = 1;

        $scope.pageSizeArticles = 8;
        $scope.currentPageArticles = 1;

        $scope.pageSizeMess = 8;
        $scope.currentPageMess = 1;




            vm.isAuthenticated = Principal.isAuthenticated;

        $scope.$on('authenticationSuccess', function() {
            getAccount();
            loadAllDocuments();
            statChart();
        });
        if(!vm.isAuthenticated()){
            loadAllArticles();
        }else{
        getAccount();
        loadAllDocuments();
        statChart(); //actualisation du graph
        }

        if($state.params.id){
            statChart();
            loadAllProjetHome();
        }
        function statChart() {
            TacheSpecial.getStatData().then(function (datanew) {
                $scope.data = datanew;
                console.log('coool  ' + $scope.data.length);
                $scope.mydata = {
                    type: "line",//bar
                    "plot": {
                        "value-box": {
                            "text": "%node-value"
                        }
                    },
                    title: {
                        backgroundColor: "transparent",
                        fontColor: "black",
                        text: "Data base counter line chart "
                    },
                    backgroundColor: "transparent",
                    "scale-x": {
                        "labels": ["Taches", "PA", "Doc", "Projets", "MH", "Comptes", "Articles", "Com","Message","Users"]
                    },
                    series: [
                        {
                            values: [$scope.data[0], $scope.data[1], $scope.data[2], $scope.data[3], $scope.data[4], $scope.data[5], $scope.data[6], $scope.data[7], $scope.data[8],$scope.data[9]],
                            backgroundColor: "#4DC0CF"
                        }
                    ]
                };
                var somme = 0;
                for (var i = 0; i < 10; i++) {
                    somme = somme + $scope.data[i];
                }


                $scope.mydata2 = {
                    type: "bar",
                    "plot": {
                        "value-box": {
                            "text": "%node-value"
                        }
                    },
                    title: {
                        backgroundColor: "transparent",
                        fontColor: "black",
                        text: "Data base counter bar chart "
                    },
                    backgroundColor: "transparent",
                    "scale-x": {
                        "labels": ["Taches", "PA", "Doc", "Projets", "MH", "Comptes", "Articles", "Com","Message", "Users"]
                    },
                    series: [
                        {
                            values: [Math.round(($scope.data[0] / somme) * 100), Math.round(($scope.data[1] / somme) * 100), Math.round(($scope.data[2] / somme) * 100), Math.round(($scope.data[3] / somme) * 100), Math.round(($scope.data[4] / somme) * 100), Math.round(($scope.data[5] / somme) * 100), Math.round(($scope.data[6] / somme) * 100), Math.round(($scope.data[7] / somme) * 100), Math.round(($scope.data[8] / somme) * 100), Math.round(($scope.data[9] / somme) * 100)],
                            backgroundColor: "#4DC0CF"
                        }
                    ]
                };

            }, function () {
                console.log('Erreur de recuperation des données');
            });
        }
        //values : [info.tacheLength,info.pointAvancementLength,info.documentLength,info.projeteLength,info.messageHierachiqueLength,info.compteLength,info.articleLength,],


            vm.isAuthenticated = Principal.isAuthenticated;

        $scope.$on('authenticationSuccess', function() {
            getAccount();
            loadAllDocuments();
            loadAllUsers();
            loadAllMessage();
            //$()
        });
        if(!vm.isAuthenticated()){
            loadAllArticles();
        }else{
        getAccount();
        loadAllDocuments();
        loadAllUsers();
            loadAllMessage();}

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

                documentbelonging(vm.listeDocumentsTotal);
            }, function () {
                console.log("Erreur de recuperation des données");
            });
        }

        function loadAllMessage() {
            vm.listeMessageTotal = [];
            vm.listeMessageLocal = [];
            Message.query().$promise.then(function (data) {
                vm.listeMessageTotal = data;
                filterMessage();
            }, function () {
                console.log("Erreur de recuperation des données");
            });
        }

        function filterMessage(){
            for(var i=0; i<vm.listeMessageTotal.length;i++){
                console.log(vm.listeMessageTotal);
                for(var j=0; j<vm.listeMessageTotal[i].users.length; j++){
                    if(vm.listeMessageTotal[i].users[j].email === vm.account.email){
                        vm.listeMessageLocal.push(vm.listeMessageTotal[i]);
                    }
                }
            }
        }

        function documentbelonging(donnee){
                vm.listeDocumentsPrive=[];
                vm.listeDocumentsPublic=[];
            for (var i = 0; i < donnee.length; i++) {
                if(donnee[i].mode==="PUBLIC"
                    && donnee[i].actif===true){
                    vm.listeDocumentsPublic.push(donnee[i]);
                }else if(donnee[i].mode==="PRIVE"
                    && donnee[i].actif===true
                    && donnee[i].user.email===vm.account.email){
                    vm.listeDocumentsPrive.push(donnee[i]);
                }
            }
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

        function loadAllUsers() {
            vm.listeUsersTotal = [];
            vm.count = 0;
            User.query().$promise.then(function (data) {
                vm.listeUsersTotal = data;
                for (var i = 0; i < vm.listeUsersTotal.length; i++) {
                    if(vm.listeUsersTotal[i].activated === true) {
                        vm.count++;
                    }
                }
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

