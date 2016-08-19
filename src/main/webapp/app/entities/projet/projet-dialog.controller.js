(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .controller('ProjetDialogController', ProjetDialogController);

    ProjetDialogController.$inject = ['$timeout','$state', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Projet', 'Compte', 'MessageHierachique', 'Commentaire', 'Tache', 'User'];

    function ProjetDialogController ($timeout,$state, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Projet, Compte, MessageHierachique, Commentaire, Tache, User) {
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
        vm.comptechefprojet = Compte.get({id :$stateParams.id});

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.projet.id !== null) {
                if ($state.params.id){
                    vm.projet.compte = vm.comptechefprojet;
                }
                Projet.update(vm.projet, onSaveSuccess, onSaveError);
            } else {
                if ($state.params.id){
                    vm.projet.compte = vm.comptechefprojet;
                }
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
        vm.datePickerOpenStatus.fromt = false;
        vm.datePickerOpenStatus.tot = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
