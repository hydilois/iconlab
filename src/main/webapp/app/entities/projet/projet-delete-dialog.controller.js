(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .controller('ProjetDeleteController',ProjetDeleteController);

    ProjetDeleteController.$inject = ['$uibModalInstance', 'entity', 'Projet'];

    function ProjetDeleteController($uibModalInstance, entity, Projet) {
        var vm = this;

        vm.projet = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Projet.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
