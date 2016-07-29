(function() {
    'use strict';

    angular
    .module('iconlabApp')
    .controller('NavbarController', NavbarController);

    NavbarController.$inject = ['$scope','$state', 'Auth', 'Principal', 'ProfileService', 'LoginService'];

    function NavbarController ($scope,$state, Auth,  Principal, ProfileService, LoginService) {
        var vm = this;

        vm.isNavbarCollapsed = true;
        vm.isAuthenticated = Principal.isAuthenticated;

         $scope.$on('authenticationSuccess', function() {
        //     loadAllCompte();
            getAccount();
        });
             getAccount();

       /* function loadAllCompte() {
            vm.listeComptesTotal = [];
            Compte.query().$promise.then(function (data) {
                vm.listeComptesTotal = data;
                userCompte(vm.listeComptesTotal, $scope.mail);
                //console.log("authen" + vm.listeComptesTotal);

            }, function () {
                console.log("Erreur de recuperation des données");
            });
        }*/

        ProfileService.getProfileInfo().then(function(response) {
            vm.inProduction = response.inProduction;
            vm.swaggerDisabled = response.swaggerDisabled;
        });

        vm.login = login;
        vm.logout = logout;
        vm.toggleNavbar = toggleNavbar;
        vm.collapseNavbar = collapseNavbar;
        vm.$state = $state;
        vm.account = null;


        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
                //$scope.mail = vm.account.email;
            });
        }

        /*function userCompte(data, email){
            vm.listeComptes = [];
            for(var i=0; i<data.length; i++){
                if(data[i].user.email===email)
                    vm.listeComptes.push(data[i]);
            }
        }*/

          function login() {
            collapseNavbar();
            LoginService.open();
        }


        function logout() {
            collapseNavbar();
            Auth.logout();
            $state.go('home');
        }

        function toggleNavbar() {
            vm.isNavbarCollapsed = !vm.isNavbarCollapsed;
        }

        function collapseNavbar() {
            vm.isNavbarCollapsed = true;
        }
        $(function() {
            $(".navbar-expand-toggle").click(function() {
                $(".app-container").toggleClass("expanded");
                return $(".navbar-expand-toggle").toggleClass("fa-rotate-90");
            });
            return $(".navbar-right-expand-toggle").click(function() {
                $(".navbar-right").toggleClass("expanded");
                return $(".navbar-right-expand-toggle").toggleClass("fa-rotate-90");
            });
        });
}
})();
