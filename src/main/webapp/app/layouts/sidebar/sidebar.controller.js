(function() {
    'use strict';

    angular
    .module('iconlabApp')
    .controller('SidebarController', SidebarController);

    SidebarController.$inject = ['$scope','$state', 'Auth', 'Principal', 'ProfileService', 'LoginService'];

    function SidebarController ($scope,$state, Auth, Principal, ProfileService, LoginService) {
        var vm = this;

        vm.isSidebarCollapsed = true;
        vm.isAuthenticated = Principal.isAuthenticated;

         $scope.$on('authenticationSuccess', function() {
            getAccount();
        });
         getAccount();

        ProfileService.getProfileInfo().then(function(response) {
            vm.inProduction = response.inProduction;
            vm.swaggerDisabled = response.swaggerDisabled;
        });

        vm.login = login;
        vm.logout = logout;
        vm.toggleSidebar = toggleSidebar;
        vm.collapseSidebar = collapseSidebar;
        vm.$state = $state;
        vm.account = null;


        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }

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
