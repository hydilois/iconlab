(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .controller('TacheDialogController', TacheDialogController);

    TacheDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Tache', 'Projet', 'PointAvancement', 'User'];

    function TacheDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Tache, Projet, PointAvancement, User) {
        var vm = this;

        vm.tache = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.projets = Projet.query();
        vm.pointavancements = PointAvancement.query();
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.tache.id !== null) {
                Tache.update(vm.tache, onSaveSuccess, onSaveError);
            } else {
                Tache.save(vm.tache, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('iconlabApp:tacheUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setFichierJoint = function ($file, tache) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        tache.fichierJoint = base64Data;
                        tache.fichierJointContentType = $file.type;
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
