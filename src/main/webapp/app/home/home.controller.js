(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .controller('HomeController', HomeController);
        angular
        .module('iconlabApp')
        .controller('AcceuilInfoController', AcceuilInfoController);

    HomeController.$inject = ['$scope', 'Principal','Article','Projet', 'LoginService', '$state'];
    AcceuilInfoController.$inject = ['$scope', 'Principal','Article','Projet', 'LoginService', '$state'];

    function HomeController ($scope, Principal,Article,Projet, LoginService, $state) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;

        vm.isAuthenticated = Principal.isAuthenticated;

        $scope.$on('authenticationSuccess', function() {
            //loadAllProjetHome()
            getAccount();
        });
        if(!vm.isAuthenticated()){
            loadAllArticles();
        }else{
        //loadAllProjetHome();
        getAccount();}

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
            console.log(vm.listeProjetsByComptes);
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
