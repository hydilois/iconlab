(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .controller('TacheDetailController', TacheDetailController);

    TacheDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'Tache', 'Projet', 'PointAvancement', 'User'];

    function TacheDetailController($scope, $rootScope, $stateParams, DataUtils, entity, Tache, Projet, PointAvancement, User) {
        var vm = this;

        vm.tache = entity;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('iconlabApp:tacheUpdate', function(event, result) {
            vm.tache = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
