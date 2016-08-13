(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .controller('DocumentsDialogController', DocumentsDialogController);

    DocumentsDialogController.$inject = ['Principal','$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Documents', 'User'];

    function DocumentsDialogController (Principal,$timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Documents, User) {
        var vm = this;

        vm.documents = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function accessCurrentAccount(){
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.documents.sender =vm.account.login;
            });
        }

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.documents.id !== null) {
                accessCurrentAccount();
                Documents.update(vm.documents, onSaveSuccess, onSaveError);
            } else {
                accessCurrentAccount();
                Documents.save(vm.documents, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('iconlabApp:documentsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setFichier = function ($file, documents) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        documents.fichier = base64Data;
                        documents.fichierContentType = $file.type;
                    });
                });
            }
        };

    }
})();
