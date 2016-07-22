(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .controller('MessageHierachiqueDialogController', MessageHierachiqueDialogController);

    MessageHierachiqueDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'MessageHierachique', 'Projet'];

    function MessageHierachiqueDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, MessageHierachique, Projet) {
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

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.messageHierachique.id !== null) {
                MessageHierachique.update(vm.messageHierachique, onSaveSuccess, onSaveError);
            } else {
                MessageHierachique.save(vm.messageHierachique, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('iconlabApp:messageHierachiqueUpdate', result);
            $uibModalInstance.close(result);
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
