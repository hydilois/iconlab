(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('message-hierachique', {
            parent: 'entity',
            url: '/message-hierachique?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MessageHierachiques'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/message-hierachique/message-hierachiques.html',
                    controller: 'MessageHierachiqueController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
            }
        })
        .state('message-hierachique-detail', {
            parent: 'entity',
            url: '/message-hierachique/{id}',
            data: {
                authorities: ['ROLE_USER','ROLE_CEO','ROLE_DO','ROLE_PMO'],
                pageTitle: 'MessageHierachique'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/message-hierachique/message-hierachique-detail.html',
                    controller: 'MessageHierachiqueDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'MessageHierachique', function($stateParams, MessageHierachique) {
                    return MessageHierachique.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('message-hierachique.new', {//etat de créatio  d'un message par un administrateur
            parent: 'message-hierachique',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/message-hierachique/message-hierachique-dialog.html',
                    controller: 'MessageHierachiqueDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    windowClass:'center-modal',
                    size: 'md',
                    resolve: {
                        entity: function () {
                            return {
                                contenu: null,
                                sender: null,
                                fichier: null,
                                fichierContentType: null,
                                date: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('message-hierachique', null, { reload: true });
                }, function() {
                    $state.go('message-hierachique');
                });
            }]
        })
        .state('app.projetcompte.newusermessage', {//etat de création des messages par un utilisateur
            parent: 'app.projetcompte',
            url: '/new/messages',
            data: {
                authorities: ['ROLE_USER','ROLE_CEO','ROLE_DO','ROLE_PMO']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/message-hierachique/message-hierachiqueU.html',
                    controller: 'MessageHierachiqueDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    windowClass:'center-modal',
                    size: 'md',
                    resolve: {
                        entity: function () {
                            return {
                                contenu: null,
                                sender: null,
                                fichier: null,
                                fichierContentType: null,
                                date: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('app.projetcompte', null, { reload: true });
                }, function() {
                    $state.go('app.projetcompte');
                });
            }]
        })
        .state('message-hierachique.edit', {
            parent: 'message-hierachique',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER','ROLE_CEO','ROLE_DO','ROLE_PMO']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/message-hierachique/message-hierachique-dialog.html',
                    controller: 'MessageHierachiqueDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    windowClass:'center-modal',
                    size: 'md',
                    resolve: {
                        entity: ['MessageHierachique', function(MessageHierachique) {
                            return MessageHierachique.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('message-hierachique', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('app.projetcompte.read', {
            parent: 'app.projetcompte',
            url: '/{idmess}/read',
            data: {
                authorities: ['ROLE_USER','ROLE_CEO','ROLE_DO','ROLE_PMO']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/message-hierachique/modalmessage.html',
                    controller: 'MessageHierachiqueDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    windowClass:'center-modal',
                    size: 'md',
                    resolve: {
                        entity: ['MessageHierachique', function(MessageHierachique) {
                            return MessageHierachique.get({id : $stateParams.idmess}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('app.projetcompte', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
            .state('app.patache.read', {
                parent: 'app.patache',
                url: '/{idmess}/read',
                data: {
                    authorities: ['ROLE_USER','ROLE_CEO','ROLE_DO','ROLE_PMO']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/message-hierachique/modalmessage.html',
                        controller: 'MessageHierachiqueDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        windowClass:'center-modal',
                        size: 'md',
                        resolve: {
                            entity: ['MessageHierachique', function(MessageHierachique) {
                                return MessageHierachique.get({id : $stateParams.idmess}).$promise;
                            }]
                        }
                    }).result.then(function() {
                        $state.go('app.patache', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    });
                }]
            })
    .state('app.tacheprojet.read', {
            parent: 'app.tacheprojet',
            url: '/{idmess}/read',
            data: {
                authorities: ['ROLE_USER','ROLE_CEO','ROLE_DO','ROLE_PMO']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/message-hierachique/modalmessage.html',
                    controller: 'MessageHierachiqueDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    windowClass:'center-modal',
                    size: 'md',
                    resolve: {
                        entity: ['MessageHierachique', function(MessageHierachique) {
                            return MessageHierachique.get({id : $stateParams.idmess}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('app.tacheprojet', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
    })
        .state('message-hierachique.delete', {
            parent: 'message-hierachique',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/message-hierachique/message-hierachique-delete-dialog.html',
                    controller: 'MessageHierachiqueDeleteController',
                    controllerAs: 'vm',
                    windowClass:'center-modal',
                    size: 'md',
                    resolve: {
                        entity: ['MessageHierachique', function(MessageHierachique) {
                            return MessageHierachique.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('message-hierachique', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
