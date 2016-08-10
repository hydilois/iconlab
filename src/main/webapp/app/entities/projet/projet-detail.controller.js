(function() {
    'use strict';

    angular
    .module('iconlabApp')
    .controller('ProjetDetailController', ProjetDetailController);
    'use strict';
    angular
    .module('iconlabApp')
    .controller('ProjetCompteController', ProjetCompteController);

    ProjetDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'Projet', 'Compte', 'MessageHierachique', 'Commentaire', 'Tache', 'User'];
    ProjetCompteController.$inject = ['$rootScope','$scope','$state','compte', 'ProjetSpecial','MessageHierachiqueSpecial'];

    function ProjetDetailController($scope, $rootScope, $stateParams, DataUtils, entity, Projet, Compte, MessageHierachique, Commentaire, Tache, User) {
        var vm = this;
        vm.projet = entity;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('iconlabApp:projetUpdate', function(event, result) {
            vm.projet = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }

    function ProjetCompteController($rootScope,$scope,$state,compte,ProjetSpecial,MessageHierachiqueSpecial) {
        var vm = this;

        vm.compte = compte;
        console.log("l'objet  est de "+vm.compte);
        var unsubscribe = $rootScope.$on('iconlabApp:compteUpdate', function(event, result) {
            vm.compte = result;
        });
        $scope.$on('$destroy', unsubscribe);

        if($state.params.id){
            ProjetSpecial.getProjetByCompte($state.params.id).then(function(data){
                vm.listePr = data;
                console.log("la taille est de "+data.length);
            },function(){
                console.log("Erreur");
            });

            MessageHierachiqueSpecial.getMessageByCompte($state.params.id).then(function(data){
                vm.listeMessage = data;
                console.log("la taille est de "+data.length);
            },function(){
                console.log("Erreur");
            });

        }

        //$scope.idSelectedVote = null;
        $scope.setSelected = function (idSelectedVote) {
            $scope.idSelectedVote = idSelectedVote;
        };
    }
})();



// (function() {
//     'use strict';

//     angular
//     .module('iconlabApp')
//     .controller('ProjetCompteController', ProjetCompteController);
  
//     ProjetCompteController.$inject = ['$rootScope','$scope','$state','compte', 'ProjetSpecial'];



//     function ProjetCompteController($rootScope,$scope,$state,compte,ProjetSpecial) {
//         var vm = this;

//         vm.compte = compte;
//         console.log("l'objet  est de "+vm.compte+" id egale a "+vm.compte.id);
//         var unsubscribe = $rootScope.$on('iconlabApp:compteUpdate', function(event, result) {
//             vm.compte = result;
//         });
//         $scope.$on('$destroy', unsubscribe);

//         if($state.params.id){
//             ProjetSpecial.getProjetByCompte($state.params.id).then(function(data){
//                 vm.listePr = data;
//                 console.log("la taille est de "+data.length);
//             },function(){
//                 console.log("Erreur");
//             });
//         }

//         $scope.select= function(item) {
//             $scope.selected = item; 
//         };

//         $scope.isActive = function(item) {
//             return $scope.selected === item;
//         };
//     }
// })();