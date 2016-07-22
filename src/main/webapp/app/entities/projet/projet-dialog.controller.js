(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .controller('ProjetDialogController', ProjetDialogController);

    ProjetDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Projet', 'Compte', 'MessageHierachique', 'Commentaire', 'Tache', 'User'];

    function ProjetDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Projet, Compte, MessageHierachique, Commentaire, Tache, User) {
        var vm = this;

        vm.projet = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.comptes = Compte.query();
        vm.messagehierachiques = MessageHierachique.query();
        vm.commentaires = Commentaire.query();
        vm.taches = Tache.query();
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.projet.id !== null) {
                Projet.update(vm.projet, onSaveSuccess, onSaveError);
            } else {
                Projet.save(vm.projet, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('iconlabApp:projetUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setFichierProjet = function ($file, projet) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        projet.fichierProjet = base64Data;
                        projet.fichierProjetContentType = $file.type;
                    });
                });
            }
        };
        vm.datePickerOpenStatus.dateDebut = false;
        vm.datePickerOpenStatus.dateFin = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
