(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .controller('MessageHierachiqueDeleteController',MessageHierachiqueDeleteController);

    MessageHierachiqueDeleteController.$inject = ['$uibModalInstance', 'entity', 'MessageHierachique'];

    function MessageHierachiqueDeleteController($uibModalInstance, entity, MessageHierachique) {
        var vm = this;

        vm.messageHierachique = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MessageHierachique.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
