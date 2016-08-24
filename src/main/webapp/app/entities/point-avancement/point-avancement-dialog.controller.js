(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .controller('PointAvancementDialogController', PointAvancementDialogController);

    PointAvancementDialogController.$inject = ['$timeout','$state','$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'PointAvancement', 'Tache'];

    function PointAvancementDialogController ($timeout,$state, $scope, $stateParams, $uibModalInstance, DataUtils, entity, PointAvancement, Tache) {
        var vm = this;

        vm.pointAvancement = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.taches = Tache.query();
        vm.tacheuser= Tache.get({id : $stateParams.idtache});
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.pointAvancement.id !== null) {
                if($state.params.idtache) {
                    vm.pointAvancement.tache = vm.tacheuser;
                }
                PointAvancement.update(vm.pointAvancement, onSaveSuccess, onSaveError);
            } else {

                if($state.params.idtache) {
                    vm.pointAvancement.tache = vm.tacheuser;
                }
                PointAvancement.save(vm.pointAvancement, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('iconlabApp:pointAvancementUpdate', result);
            $uibModalInstance.close(result);
            toastr.info(" Enregistrement réussie");
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setFichier = function ($file, pointAvancement) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        pointAvancement.fichier = base64Data;
                        pointAvancement.fichierContentType = $file.type;
                    });
                });
            }
        };
        vm.datePickerOpenStatus.datePub = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
