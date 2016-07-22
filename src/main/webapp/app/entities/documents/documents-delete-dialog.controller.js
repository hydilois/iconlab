(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .controller('DocumentsDeleteController',DocumentsDeleteController);

    DocumentsDeleteController.$inject = ['$uibModalInstance', 'entity', 'Documents'];

    function DocumentsDeleteController($uibModalInstance, entity, Documents) {
        var vm = this;

        vm.documents = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Documents.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
