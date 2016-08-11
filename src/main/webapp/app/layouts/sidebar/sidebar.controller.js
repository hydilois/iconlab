(function() {
    'use strict';

    angular
    .module('iconlabApp')
    .controller('SidebarController', SidebarController);

    SidebarController.$inject = ['$scope','$state', 'Auth','Compte','Projet','Tache', 'Principal', 'ProfileService', 'LoginService'];

    function SidebarController ($scope,$state, Auth, Compte, Projet, Tache, Principal, ProfileService, LoginService) {
        var vm = this;
        vm.login = login;
        vm.logout = logout;
        vm.toggleSidebar = toggleSidebar;
        vm.collapseSidebar = collapseSidebar;
        vm.$state = $state;
        vm.isSidebarCollapsed = true;
        vm.isAuthenticated = Principal.isAuthenticated;

        $scope.$on('authenticationSuccess', function() {
            loadAllCompte();
            loadAllProjet();
            loadAllTask();
            getAccount();
        });
        if(vm.isAuthenticated()){
            loadAllCompte();
            loadAllProjet();
            loadAllTask();
            getAccount();
        }

        function loadAllCompte() {
            vm.listeComptesTotal = [];
            Compte.query().$promise.then(function (data) {
                vm.listeComptesTotal = data;
                userCompte(vm.listeComptesTotal, $scope.mail);
            }, function () {
                console.log("Erreur de recuperation des données");
            });
        }
        function loadAllProjet() {
            vm.listeProjetsTotal = [];
            Projet.query().$promise.then(function (data) {
                vm.listeProjetsTotal = data;
                userProjet(vm.listeProjetsTotal, $scope.mail);
            }, function () {
                console.log("Erreur de recuperation des données");
            });
        }

        function loadAllTask() {
            vm.listeTaskTotal = [];
            Tache.query().$promise.then(function (data) {
                vm.listeTaskTotal = data;
                userTask(vm.listeTaskTotal, $scope.mail);

            }, function () {
                console.log("Erreur de recuperation des données");
            });
        }

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
                $scope.mail = vm.account.email;
            });
        }

        function userCompte(data, email){
            vm.listeComptes = [];
            for(var i=0; i<data.length; i++){
                if(data[i].user.email===email)
                    vm.listeComptes.push(data[i]);
            }
        }

        function userProjet(data, email){
            vm.listeProjets = [];
            for(var i=0; i<data.length; i++){
                if(data[i].user.email===email)
                    vm.listeProjets.push(data[i]);
            }
        }

        function userTask(data, email){
            vm.listeTasks = [];
            for(var i=0; i<data.length; i++){
                if(data[i].user.email===email)
                    vm.listeTasks.push(data[i]);
            }
        }


        ProfileService.getProfileInfo().then(function(response) {
            vm.inProduction = response.inProduction;
            vm.swaggerDisabled = response.swaggerDisabled;
        });

        function login() {
            collapseSidebar();
            LoginService.open();
        }


        function logout() {
            collapseSidebar();
            Auth.logout();
            $state.go('home');
        }

        function toggleSidebar() {
            vm.isSidebarCollapsed = !vm.isSidebarCollapsed;
        }

        function collapseSidebar() {
            vm.isSidebarCollapsed = true;
        }

    }
    
    // $(function() {
    //     $(".listeProjet").click(function() {
    //         $(".app-container").toggleClass("expanded");
    //     });
    //     return $(".navbar-right-expand-toggle").click(function() {
    //         $(".navbar-right").toggleClass("expanded");
    //     });
    // });

})();
