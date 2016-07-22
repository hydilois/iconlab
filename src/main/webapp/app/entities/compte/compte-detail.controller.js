(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .controller('CompteDetailController', CompteDetailController);

    CompteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'Compte', 'Projet', 'User'];

    function CompteDetailController($scope, $rootScope, $stateParams, DataUtils, entity, Compte, Projet, User) {
        var vm = this;

        vm.compte = entity;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('iconlabApp:compteUpdate', function(event, result) {
            vm.compte = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
