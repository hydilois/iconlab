(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .controller('TacheDialogController', TacheDialogController);

    TacheDialogController.$inject = ['$timeout','$state' ,'$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Tache', 'Projet', 'PointAvancement', 'User'];

    function TacheDialogController ($timeout,$state ,$scope, $stateParams, $uibModalInstance, DataUtils, entity, Tache, Projet, PointAvancement, User) {
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
        vm.pojetP = Projet.get({id : $stateParams.idprojet});

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.tache.id !== null) {

                if ($state.params.idprojet){
                    vm.tache.projet = vm.pojetP;
                    console.log("pojet est ..."+vm.pojetP);
                }

                Tache.update(vm.tache, onSaveSuccess, onSaveError);
            } else {
                if ($state.params.idprojet){
                    vm.tache.projet = vm.pojetP;
                }
                vm.tache.actif = true;
                Tache.save(vm.tache, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('iconlabApp:tacheUpdate', result);
            $uibModalInstance.close(result);
            toastr.info("Tache créée");
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
        vm.datePickerOpenStatus.fromt = false;
        vm.datePickerOpenStatus.tot = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
