(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .controller('TacheDeleteController',TacheDeleteController);

    TacheDeleteController.$inject = ['$uibModalInstance', 'entity', 'Tache'];

    function TacheDeleteController($uibModalInstance, entity, Tache) {
        var vm = this;

        vm.tache = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Tache.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
