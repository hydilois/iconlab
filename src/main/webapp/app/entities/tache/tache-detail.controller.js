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



    TacheDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'Tache', 'Projet', 'PointAvancement', 'User','$uibModalInstance'];
    TacheProjetController.$inject = ['$interval','DataUtils','$scope', '$rootScope', '$state', 'entity', 'TacheSpecial', 'CommentaireSpecial', 'DocumentSpecial', 'Principal', 'Commentaire', 'User', 'MessageHierachiqueSpecial'];
    GanttTacheProjetController.$inject = ['$scope', '$rootScope', '$state', 'entity', 'TacheSpecial'];


    function TacheDetailController($scope, $rootScope, $stateParams, DataUtils, entity, Tache, Projet, PointAvancement, User,$uibModalInstance) {
        var vm = this;

        vm.tache = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        var unsubscribe = $rootScope.$on('iconlabApp:tacheUpdate', function (event, result) {
            vm.tache = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
    ;
    function TacheProjetController($interval,DataUtils,$scope, $rootScope, $state, entity, TacheSpecial, CommentaireSpecial, DocumentSpecial, Principal, Commentaire, User, MessageHierachiqueSpecial) {
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
                vm.commentaire.auteur = vm.account.firstName+" "+vm.account.lastName;
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
                vm.listeDocumentP = data;
            }, function () {
                console.log('Erreur de recuperation des données');
            });

            CommentaireSpecial.getCommentaireByProject($state.params.idprojet).then(function (data) {
                vm.listeCommentaireParProjet = data;
            }, function () {
                console.log('Erreur de recuperation des données');
            });
            /*CommentaireSpecial.getCommentaireByProject($state.params.idprojet).then(function (data) {
                vm.listeCommentaireParProjet = data;
            }, function () {
                console.log('Erreur de recuperation des données');
            });*/

            MessageHierachiqueSpecial.getMessageByProjet($state.params.idprojet).then(function (data) {
                vm.listeMessageParProjet = data;
            }, function () {
                console.log("Erreur");
            });

             }

/*
            $interval(function(){//pour aclualiser les donnees apres 100 ms
                
              if ($state.params.idprojet) {
                CommentaireSpecial.getCommentaireByProject($state.params.idprojet).then(function (data) {
                vm.listeCommentaireParProjet = data;
                 }, function () {
                 console.log('Erreur de recuperation des données');
                });
             }  
            },100);
*/
        $scope.savecomment = function () {
            //vm.isSaving = true;
            $state.go('app.tacheprojet', null, {reload: true});
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
        $scope.options = {
            mode: 'custom',
            scale: 'day',
            sortMode: undefined,
            sideMode: 'TreeTable',
            daily: false,
            maxHeight: false,
            width: false,
            zoom: 1,
            columns: ['model.name', 'from', 'to'],
            treeTableColumns: ['model.name','from','to'],
            columnsHeaders: {'model.name' : 'Name', 'from': 'From', 'to': 'To'},
            columnsClasses: {'model.name' : 'gantt-column-name', 'from': 'gantt-column-from', 'to': 'gantt-column-to'},
            columnsFormatters: {
                'from': function(from) {
                    return from !== undefined ? from.format('lll') : undefined;
                },
                'to': function(to) {
                    return to !== undefined ? to.format('lll') : undefined;
                }
            },
            treeHeaderContent: '<i class="fa fa-align-justify"></i> {{getHeader()}}',
            columnsHeaderContents: {
                'model.name': '<i class="fa fa-align-justify"></i> {{getHeader()}}',
                'from': '<i class="fa fa-calendar"></i> {{getHeader()}}',
                'to': '<i class="fa fa-calendar"></i> {{getHeader()}}'
            },
            autoExpand: 'none',
            taskOutOfRange: 'truncate',
            fromDate: moment(null),
            toDate: undefined,
            rowContent: '<i class="fa fa-align-justify"></i> {{row.model.name}}',
            taskContent : '<i class="fa fa-tasks"></i> {{task.model.name}}',
            allowSideResizing: true

        };

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
