(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('commentaire', {
            parent: 'entity',
            url: '/commentaire?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Commentaires'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/commentaire/commentaires.html',
                    controller: 'CommentaireController',
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
        .state('commentaire-detail', {
            parent: 'entity',
            url: '/commentaire/{id}',
            data: {
                authorities: ['ROLE_USER','ROLE_CEO','ROLE_DO','ROLE_PMO'],
                pageTitle: 'Commentaire'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/commentaire/commentaire-detail.html',
                    controller: 'CommentaireDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Commentaire', function($stateParams, Commentaire) {
                    return Commentaire.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('commentaire.new', {
            parent: 'commentaire',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/commentaire/commentaire-dialog.html',
                    controller: 'CommentaireDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    windowClass:'center-modal',
                    size: 'md',
                    resolve: {
                        entity: function () {
                            return {
                                contenu: null,
                                auteur: null,
                                datePost: null,
                                actif: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('commentaire', null, { reload: true });
                }, function() {
                    $state.go('commentaire');
                });
            }]
        })
        .state('app.tacheprojet.newusercomment', {
            parent: 'app.tacheprojet',
            url: '/commentaire/new',
            data: {
                authorities: ['ROLE_USER','ROLE_CEO','ROLE_DO','ROLE_PMO']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/commentaire/commentaire-dialog.html',
                    controller: 'CommentaireDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    windowClass:'center-modal',
                    size: 'md',
                    resolve: {
                        entity: function () {
                            return {
                                contenu: null,
                                datePost: null,
                                actif: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('app.tacheprojet', null, { reload: true });
                }, function() {
                    $state.go('app.tacheprojet');
                });
            }]
        })
        .state('commentaire.edit', {
            parent: 'commentaire',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER','ROLE_CEO','ROLE_DO','ROLE_PMO']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/commentaire/commentaire-dialog.html',
                    controller: 'CommentaireDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    windowClass:'center-modal',
                    size: 'md',
                    resolve: {
                        entity: ['Commentaire', function(Commentaire) {
                            return Commentaire.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('commentaire', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('commentaire.delete', {
            parent: 'commentaire',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/commentaire/commentaire-delete-dialog.html',
                    controller: 'CommentaireDeleteController',
                    windowClass:'center-modal',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Commentaire', function(Commentaire) {
                            return Commentaire.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('commentaire', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
