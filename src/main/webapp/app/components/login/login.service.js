(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .factory('LoginService', LoginService);

    LoginService.$inject = ['$uibModal'];

    function LoginService ($uibModal) {
        var service = {
            open: open
        };

        var modalInstance = null;
        var resetModal = function () {
            modalInstance = null;
        };

        return service;

        function open () {
            if (modalInstance !== null) return;
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/components/login/login1.html',
                controller: 'LoginController',
                controllerAs: 'vm',
                windowClass:'center-modal',
                size: 'md'
            });
            modalInstance.result.then(
                resetModal,
                resetModal
            );
        }
    }
})();
