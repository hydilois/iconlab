(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .controller('MessageHierachiqueDetailController', MessageHierachiqueDetailController);

    MessageHierachiqueDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'MessageHierachique', 'Projet'];

    function MessageHierachiqueDetailController($scope, $rootScope, $stateParams, DataUtils, entity, MessageHierachique, Projet) {
        var vm = this;

        vm.messageHierachique = entity;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('iconlabApp:messageHierachiqueUpdate', function(event, result) {
            vm.messageHierachique = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
