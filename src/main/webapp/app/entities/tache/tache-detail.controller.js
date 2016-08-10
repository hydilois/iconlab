(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .controller('TacheDetailController', TacheDetailController);

    angular
        .module('iconlabApp')
        .controller('TacheProjetController', TacheProjetController);
    angular
        .module('iconlabApp')
        .controller('GanttTacheProjetController', GanttTacheProjetController);



    TacheDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'Tache', 'Projet', 'PointAvancement', 'User'];
    TacheProjetController.$inject = ['$scope', '$rootScope', '$state','entity','TacheSpecial'];
    GanttTacheProjetController.$inject = ['$scope', '$rootScope', '$state','entity','TacheSpecial'];


    function TacheDetailController($scope, $rootScope, $stateParams, DataUtils, entity, Tache, Projet, PointAvancement, User) {
        var vm = this;

        vm.tache = entity;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('iconlabApp:tacheUpdate', function(event, result) {
            vm.tache = result;
        });
        $scope.$on('$destroy', unsubscribe);
    };
    function TacheProjetController($scope, $rootScope, $state, entity,TacheSpecial) {
        var vm = this;
        vm.projet = entity;

        var unsubscribe = $rootScope.$on('iconlabApp:projetUpdate', function(event, result) {
            vm.projet = result;
        });
        $scope.$on('$destroy', unsubscribe);

        if($state.params.idprojet){
            TacheSpecial.getTacheByProjet($state.params.idprojet).then(function(data){
                vm.listeTachesParProjet = data;

            }, function(){
                console.log('Erreur de recuperation des données');
            });
        }

    }
    ;
    function GanttTacheProjetController($scope, $rootScope, $state, entity,TacheSpecial) {
        var vm = this;
        vm.projet = entity;

        var unsubscribe = $rootScope.$on('iconlabApp:projetUpdate', function(event, result) {
            vm.projet = result;
        });
        $scope.$on('$destroy', unsubscribe);


        if($state.params.idprojet){
            TacheSpecial.getTacheByProjetGantt($state.params.idprojet).then(function(datanew){
                vm.listeTachesParProjetGantt = datanew;

                $scope.data = vm.listeTachesParProjetGantt;

                console.log('je suis la'+$scope.data);
            }, function(){
                console.log('Erreur de recuperation des données');
            });
        }


    }

})();
