(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .controller('PointAvancementDetailController', PointAvancementDetailController);

    PointAvancementDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'PointAvancement', 'Tache'];

    angular
    .module('iconlabApp')
    .controller('PointAvancementTacheController', PointAvancementTacheController);


    PointAvancementTacheController.$inject = ['$rootScope','$scope','$state','entity','PointAvancementSpecial'];

    function PointAvancementDetailController($scope, $rootScope, $stateParams, DataUtils, entity, PointAvancementSpecial, Tache) {
        var vm = this;

        vm.pointAvancement = entity;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('iconlabApp:pointAvancementUpdate', function(event, result) {
            vm.pointAvancement = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
     function PointAvancementTacheController($rootScope,$scope,$state,entity,PointAvancementSpecial) {
        var vm = this;

        vm.tache = entity;
        console.log("l'objet  est de "+vm.tache);
        var unsubscribe = $rootScope.$on('iconlabApp:tacheUpdate', function(event, result) {
            vm.tache = result;
        });
        $scope.$on('$destroy', unsubscribe);

        if($state.params.idtache){
            PointAvancementSpecial.getPointAvancementByTache($state.params.idtache).then(function(data){
                vm.listePa= data;
                console.log("la taille est de "+data.length);
            },function(){
                console.log("Erreur");
            });
        }

        $scope.select= function(item) {
            $scope.selected = item; 
        };

        $scope.isActive = function(item) {
            return $scope.selected === item;
        };
    }
})();
