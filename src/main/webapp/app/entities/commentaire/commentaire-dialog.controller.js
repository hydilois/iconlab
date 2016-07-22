(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .controller('CommentaireDialogController', CommentaireDialogController);

    CommentaireDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Commentaire', 'Projet'];

    function CommentaireDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Commentaire, Projet) {
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

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.commentaire.id !== null) {
                Commentaire.update(vm.commentaire, onSaveSuccess, onSaveError);
            } else {
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
