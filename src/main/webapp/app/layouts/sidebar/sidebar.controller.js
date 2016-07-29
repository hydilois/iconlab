(function() {
    'use strict';

    angular
    .module('iconlabApp')
    .controller('SidebarController', SidebarController);

    SidebarController.$inject = ['$scope','$state', 'Auth','Compte', 'Principal', 'ProfileService', 'LoginService'];

    function SidebarController ($scope,$state, Auth,Compte, Principal, ProfileService, LoginService) {
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
            getAccount();
        });
        if(vm.isAuthenticated()){
            loadAllCompte();
        getAccount();}

        function loadAllCompte() {
            vm.listeComptesTotal = [];
            Compte.query().$promise.then(function (data) {
                vm.listeComptesTotal = data;
                userCompte(vm.listeComptesTotal, $scope.mail);
                //console.log("authen" + vm.listeComptesTotal);

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

})();
