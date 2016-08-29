(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .controller('CommentaireDialogController', CommentaireDialogController);

    CommentaireDialogController.$inject = ['Principal','$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Commentaire', 'Projet'];

    function CommentaireDialogController (Principal,$timeout, $scope, $stateParams, $uibModalInstance, entity, Commentaire, Projet) {
        var vm = this;

        vm.commentaire = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.projets = Projet.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });
        function accessCurrentAccount(){
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.commentaire.auteur =vm.account.firstName+" "+vm.account.lastName;
                console.log(vm.account);
            });
        }

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.commentaire.id !== null) {
                accessCurrentAccount();
                Commentaire.update(vm.commentaire, onSaveSuccess, onSaveError);
            } else {
                accessCurrentAccount();
                vm.commentaire.actif =true;
                Commentaire.save(vm.commentaire, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('iconlabApp:commentaireUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.datePost = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
