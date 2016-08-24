(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .controller('MessageHierachiqueDialogController', MessageHierachiqueDialogController);

    MessageHierachiqueDialogController.$inject = ['Principal','$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'MessageHierachique', 'Projet'];

    function MessageHierachiqueDialogController (Principal,$timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, MessageHierachique, Projet) {
        var vm = this;

        vm.messageHierachique = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.projets = Projet.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function accessCurrentAccount(){
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.messageHierachique.sender =vm.account.login;
                vm.messageHierachique.date = new Date();
                console.log(vm.account);
            });
        }


        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.messageHierachique.id !== null) {
                accessCurrentAccount();

                MessageHierachique.update(vm.messageHierachique, onSaveSuccess, onSaveError);
            } else {
                accessCurrentAccount();
                vm.messageHierachique.actif = true;
                MessageHierachique.save(vm.messageHierachique, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('iconlabApp:messageHierachiqueUpdate', result);
            $uibModalInstance.close(result);
            toastr.info("Opération effectuée avec Succès");
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setFichier = function ($file, messageHierachique) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        messageHierachique.fichier = base64Data;
                        messageHierachique.fichierContentType = $file.type;
                    });
                });
            }
        };
        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
