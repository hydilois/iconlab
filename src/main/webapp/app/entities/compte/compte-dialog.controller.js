(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .controller('CompteDialogController', CompteDialogController);

    CompteDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Compte', 'Projet', 'User'];

    function CompteDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Compte, Projet, User) {
        var vm = this;

        vm.compte = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.projets = Projet.query();
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.compte.id !== null) {
                Compte.update(vm.compte, onSaveSuccess, onSaveError);
            } else {
                vm.compte.actif = true ;
                Compte.save(vm.compte, onSaveSuccess, onSaveError);
                //toastr.info("Enregistrement du compte effectué avec Succès");
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('iconlabApp:compteUpdate', result);
            $uibModalInstance.close(result);
            toastr.success("Compte créé");
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setLogo = function ($file, compte) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        compte.logo = base64Data;
                        compte.logoContentType = $file.type;
                    });
                });
            }
        };

    }
})();
