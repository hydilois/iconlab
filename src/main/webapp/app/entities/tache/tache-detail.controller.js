(function () {
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
    TacheProjetController.$inject = ['DataUtils','$scope', '$rootScope', '$state', 'entity', 'TacheSpecial', 'CommentaireSpecial', 'DocumentSpecial', 'Principal', 'Commentaire', 'User', 'MessageHierachiqueSpecial'];
    GanttTacheProjetController.$inject = ['$scope', '$rootScope', '$state', 'entity', 'TacheSpecial'];


    function TacheDetailController($scope, $rootScope, $stateParams, DataUtils, entity, Tache, Projet, PointAvancement, User) {
        var vm = this;

        vm.tache = entity;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('iconlabApp:tacheUpdate', function (event, result) {
            vm.tache = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
    ;
    function TacheProjetController(DataUtils,$scope, $rootScope, $state, entity, TacheSpecial, CommentaireSpecial, DocumentSpecial, Principal, Commentaire, User, MessageHierachiqueSpecial) {
        var vm = this;
        vm.projet = entity;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('iconlabApp:projetUpdate', function (event, result) {
            vm.projet = result;
        });
        $scope.$on('$destroy', unsubscribe);

        function accessCurrentAccount() {
            Principal.identity().then(function (account) {
                vm.account = account;
                vm.commentaire.auteur = vm.account.login;
                vm.commentaire.datePost = new Date();
                vm.commentaire.projet = vm.projet;

            });
        }

        if ($state.params.idprojet) {
            TacheSpecial.getTacheByProjet($state.params.idprojet).then(function (data) {
                vm.listeTachesParProjet = data;

            }, function () {
                console.log('Erreur de recuperation des données');
            });
            DocumentSpecial.getDocumentByCUser().then(function (data) {
                console.log("I am there");
                vm.listeDocumentP = data;
            }, function () {
                console.log('Erreur de recuperation des données');
            });

            CommentaireSpecial.getCommentaireByProject($state.params.idprojet).then(function (data) {
                vm.listeCommentaireParProjet = data;
            }, function () {
                console.log('Erreur de recuperation des données');
            });
            CommentaireSpecial.getCommentaireByProject($state.params.idprojet).then(function (data) {
                vm.listeCommentaireParProjet = data;
            }, function () {
                console.log('Erreur de recuperation des données');
            });

            MessageHierachiqueSpecial.getMessageByProjet($state.params.idprojet).then(function (data) {
                vm.listeMessageParProjet = data;
            }, function () {
                console.log("Erreur");
            });
        }

        $scope.savecomment = function () {
            //vm.isSaving = true;
            accessCurrentAccount();
            Commentaire.save(vm.commentaire);
            $state.go('app.tacheprojet', null, {reload: true});
        }

        $scope.setSelected = function (idSelectedVote) {
            $scope.idSelectedVote = idSelectedVote;
        };

    }
    ;
    function GanttTacheProjetController($scope, $rootScope, $state, entity, TacheSpecial) {
        var vm = this;
        vm.projet = entity;

        var unsubscribe = $rootScope.$on('iconlabApp:projetUpdate', function (event, result) {
            vm.projet = result;
        });
        $scope.$on('$destroy', unsubscribe);


        var parent = {name: vm.projet.name, from: vm.projet.front, to: vm.projet.tot, children: [], content: '<i class="fa fa-file-code-o" ng-click="scope.handleRowIconClick(row.model)"></i> {{row.model.name}}'};


        if ($state.params.idprojet) {
            TacheSpecial.getTacheByProjetGantt($state.params.idprojet).then(function (datanew) {
                vm.listeTachesParProjetGantt = datanew;

                for (var i = 0; i < vm.listeTachesParProjetGantt.length; i++) {
                    parent.children[i] = vm.listeTachesParProjetGantt[i].name;
                }
                vm.listeTachesParProjetGantt.splice(0, 0, parent);
                $scope.data = vm.listeTachesParProjetGantt;

            }, function () {
                console.log('Erreur de recuperation des données');
            });
        }


    }

})();
