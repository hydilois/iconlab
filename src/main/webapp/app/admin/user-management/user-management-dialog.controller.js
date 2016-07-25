(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .controller('UserManagementDialogController',UserManagementDialogController);

    UserManagementDialogController.$inject = ['$timeout', '$scope','$stateParams','DataUtils', '$uibModalInstance', 'entity', 'User'];

    function UserManagementDialogController ($timeout, $scope,$stateParams,DataUtils, $uibModalInstance, entity, User) {
        var vm = this;

        vm.authorities = ['ROLE_USER', 'ROLE_ADMIN','ROLE_CEO','ROLE_PMO','ROLE_DO','ROLE_GRH','ROLE_FINANCE','ROLE_LOGISTIQUE'];
        vm.clear = clear;
        vm.languages = null;
        vm.save = save;
        vm.user = entity;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;



        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function onSaveSuccess (result) {
            vm.isSaving = false;
            $uibModalInstance.close(result);
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.setImage = function ($file, user) {
            console.log("je suis la le grand");
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

        function save () {
            vm.isSaving = true;
            if (vm.user.id !== null) {
                User.update(vm.user, onSaveSuccess, onSaveError);
            } else {
                vm.user.langKey = 'en';
                User.save(vm.user, onSaveSuccess, onSaveError);
            }
        }


    }
})();
