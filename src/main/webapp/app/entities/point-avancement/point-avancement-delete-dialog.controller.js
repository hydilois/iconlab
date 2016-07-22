(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .controller('PointAvancementDeleteController',PointAvancementDeleteController);

    PointAvancementDeleteController.$inject = ['$uibModalInstance', 'entity', 'PointAvancement'];

    function PointAvancementDeleteController($uibModalInstance, entity, PointAvancement) {
        var vm = this;

        vm.pointAvancement = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PointAvancement.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
