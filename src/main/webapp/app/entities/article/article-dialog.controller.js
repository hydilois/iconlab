(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .controller('ArticleDialogController', ArticleDialogController);

    ArticleDialogController.$inject = ['Principal','$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Article', 'User'];

    function ArticleDialogController (Principal,$timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Article, User) {
        var vm = this;

        vm.article = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.users = User.query();


        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function accessCurrentAccount(){
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.article.auteur =vm.account.login;
                console.log(vm.account);
            });
        }

        function save () {
            vm.isSaving = true;
            if (vm.article.id !== null) {
               accessCurrentAccount();
                Article.update(vm.article, onSaveSuccess, onSaveError);
            } else {
                accessCurrentAccount();
                Article.save(vm.article, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('iconlabApp:articleUpdate', result);
            $uibModalInstance.close(result);
            toastr.info("opération effectuée avec Succès");
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.datePub = false;

        vm.setImage = function ($file, article) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        article.image = base64Data;
                        article.imageContentType = $file.type;
                    });
                });
            }
        };

        vm.setFichier = function ($file, article) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        article.fichier = base64Data;
                        article.fichierContentType = $file.type;
                    });
                });
            }
        };

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
