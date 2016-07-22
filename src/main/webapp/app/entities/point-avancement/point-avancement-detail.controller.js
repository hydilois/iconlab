(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .controller('PointAvancementDetailController', PointAvancementDetailController);

    PointAvancementDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'PointAvancement', 'Tache'];

    function PointAvancementDetailController($scope, $rootScope, $stateParams, DataUtils, entity, PointAvancement, Tache) {
        var vm = this;

        vm.pointAvancement = entity;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('iconlabApp:pointAvancementUpdate', function(event, result) {
            vm.pointAvancement = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
