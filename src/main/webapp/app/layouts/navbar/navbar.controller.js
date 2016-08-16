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
            animation();
            getAccount();
        });
        animation();
        getAccount();

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

        function login() {
            collapseNavbar();
            LoginService.open();
        }


        function logout() {
            animation();
            collapseNavbar();
            Auth.logout();

            $state.go('home', null, { reload: true });
            $(".image").removeClass('hide');
            $(".colorationnav").removeClass('navbarhaut');
            $(".icon").addClass('hide');
            $(".nomicon2").removeClass('hide');
            $(".nomicon1").addClass('hide');
            if($(".navbar-expand-toggle").hasClass("fa-rotate-90")){
                $(".app-container").toggleClass("expanded");
                $(".navbar-expand-toggle").toggleClass("fa-rotate-90");
        }
        }

        function toggleNavbar() {
            vm.isNavbarCollapsed = !vm.isNavbarCollapsed;
        }

        function collapseNavbar() {
            vm.isNavbarCollapsed = true;
        }

        function animation(){
            if(vm.isAuthenticated()){
                $(".image").addClass('hide');
                $(".icon").removeClass('hide');

                $(".nomicon1").removeClass('hide');
                $(".nomicon2").addClass('hide');

                $(".colorationnav").addClass('navbarhaut');

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
        }else{
                $(".image").removeClass('hide');
                $(".icon").addClass('hide');

                $(".colorationnav").removeClass('navbarhaut');

                $(".nomicon2").removeClass('hide');
                $(".nomicon1").addClass('hide');
        }

    }
    }
    })();
