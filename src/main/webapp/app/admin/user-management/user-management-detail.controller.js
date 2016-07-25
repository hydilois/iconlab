(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .controller('UserManagementDetailController', UserManagementDetailController);

    UserManagementDetailController.$inject = ['$stateParams','DataUtils', 'User'];

    function UserManagementDetailController ($stateParams,DataUtils, User) {
        var vm = this;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.load = load;
        vm.user = {};

        vm.load($stateParams.login);

        function load (login) {
            User.get({login: login}, function(result) {
                vm.user = result;
            });
        }
    }
})();
