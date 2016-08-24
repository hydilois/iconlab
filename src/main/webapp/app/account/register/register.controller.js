(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .controller('RegisterController', RegisterController);


    RegisterController.$inject = ['$state','$scope','$timeout', 'Auth', 'LoginService','DataUtils'];

    function RegisterController ($state,$scope,$timeout, Auth, LoginService,DataUtils) {
        var vm = this;

        vm.doNotMatch = null;
        vm.error = null;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.errorUserExists = null;
        vm.login = LoginService.open;
        vm.register = register;
        vm.registerAccount = {};
        vm.success = null;




        vm.setImage = function ($file, user) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        user.image = base64Data;
                        user.imageContentType = $file.type;
                    });
                });
            }
        };

        $timeout(function (){angular.element('#login').focus();});

        function register () {
            if (vm.registerAccount.password !== vm.confirmPassword) {
                vm.doNotMatch = 'ERROR';
            } else {
                vm.registerAccount.langKey =  'en' ;
                vm.doNotMatch = null;
                vm.error = null;
                vm.errorUserExists = null;
                vm.errorEmailExists = null;

                Auth.createAccount(vm.registerAccount).then(function () {
                    vm.success = 'OK';
                    $state.go('home',null,{reload:true});
                    toastr.info("Enregistrement Ok ,Attender votre identificqtion par l'administration ");
                }).catch(function (response) {
                    vm.success = null;
                    if (response.status === 400 && response.data === 'login already in use') {
                        vm.errorUserExists = 'ERROR';
                    } else if (response.status === 400 && response.data === 'e-mail address already in use') {
                        vm.errorEmailExists = 'ERROR';
                    } else {
                        vm.error = 'ERROR';
                    }
                });
            }
        }
    }
})();
