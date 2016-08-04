(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .controller('TacheDetailController', TacheDetailController);

    angular
        .module('iconlabApp')
        .controller('TacheProjetController', TacheProjetController);

    TacheDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'Tache', 'Projet', 'PointAvancement', 'User'];
    TacheProjetController.$inject = ['$scope', '$rootScope', '$state','entity','TacheSpecial'];

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

    function TacheProjetController($scope, $rootScope, $state,entity,TacheSpecial) {
        var vm = this;
        vm.projet = entity;

        var unsubscribe = $rootScope.$on('iconlabApp:projetUpdate', function(event, result) {
            vm.projet = result;
        });
        $scope.$on('$destroy', unsubscribe);

        if($state.params.id){
            TacheSpecial.getTacheByProjet($state.params.id).then(function(data){
                vm.listeTachesParProjet = data;
            }, function(){
                console.log('Erreur de recuperation des données');
            });
        }
    }
})();
