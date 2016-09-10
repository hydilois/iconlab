(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .controller('MessageDialogController', MessageDialogController);

    MessageDialogController.$inject = ['Principal','$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Message', 'User'];

    function MessageDialogController (Principal,$timeout, $scope, $stateParams, $uibModalInstance, entity, Message, User) {
        var vm = this;

        vm.message = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function accessCurrentAccount(){
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.message.sender =vm.account.firstName+" "+vm.account.lastName;
                vm.message.dateenvoi = new Date();

            });
        }

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.message.id !== null) {
                accessCurrentAccount();
                Message.update(vm.message, onSaveSuccess, onSaveError);
            } else {
                accessCurrentAccount();
                Message.save(vm.message, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('iconlabMessageApp:messageUpdate', result);
            $uibModalInstance.close(result);
            toastr.info("Message envoyée");
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
