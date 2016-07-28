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
        vm.listeComptesTotal=[];
        vm.listeComptes = [];
        vm.account=null;

        
        getAccount();

        $scope.$on('authenticationSuccess', function() {
            Compte.query().$promise.then(function(data){
                vm.listeComptesTotal = data;
                userCompte(vm.listeComptesTotal,$scope.mail);
                
            },function(){
                console.log("Erreur de recuperation des données");
            });
            getAccount();
            
            //console.log(vm.listeComptes);
        });

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
                $scope.mail = vm.account.email;
               
                /*for(var i=0; i<vm.listeComptesTotal.length; i++){
                    console.log(vm.listeComptesTotal[i].user.email);
                    if(vm.listeComptesTotal[i].user.email===$scope.mail)
                        vm.listeComptes.push(vm.listeComptesTotal[i]);
                }
                console.log(vm.listeComptes);*/
            });
        }

        function userCompte(data, email){
            vm.listeComptes = [];
            for(var i=0; i<data.length; i++){
                if(data[i].user.email===email)
                    vm.listeComptes.push(data[i]);
            }
        }

        vm.isSidebarCollapsed = true;
        vm.isAuthenticated = Principal.isAuthenticated;


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

        /*function clear(){
            vm.listeComptes=[];
            vm.account=null;

        }*/

        /*Ajout des services de compte*/

    }

})();
