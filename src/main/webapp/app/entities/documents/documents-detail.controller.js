(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .controller('DocumentsDetailController', DocumentsDetailController);

    DocumentsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'Documents', 'User'];

    function DocumentsDetailController($scope, $rootScope, $stateParams, DataUtils, entity, Documents, User) {
        var vm = this;

        vm.documents = entity;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('iconlabApp:documentsUpdate', function(event, result) {
            vm.documents = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
