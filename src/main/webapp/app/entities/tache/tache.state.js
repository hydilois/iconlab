(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('tache', {
            parent: 'entity',
            url: '/tache?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Taches'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tache/taches.html',
                    controller: 'TacheController',
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
        .state('tache-detail', {
            parent: 'entity',
            url: '/tache/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Tache'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tache/tache-detail.html',
                    controller: 'TacheDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Tache', function($stateParams, Tache) {
                    return Tache.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('projetcompte.tache-projet', {
            parent: 'entity',
            url: '/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Tache'
            },
            views:{
                'content@':{
            templateUrl: 'app/entities/tache/listeTache.html',
            controller:'TacheProjetController',
            controllerAs:'vm'
            }
        },
            resolve: {
                entity: ['$stateParams', 'Projet', function($stateParams, Projet) {
                    return Projet.get({id : $stateParams.id}).$promise;
                }]
            }
            }
        )
        .state('tache.new', {
            parent: 'tache',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tache/tache-dialog.html',
                    controller: 'TacheDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    windowClass:'center-modal',
                    size: 'md',
                    resolve: {
                        entity: function () {
                            return {
                                nom: null,
                                description: null,
                                fichierJoint: null,
                                fichierJointContentType: null,
                                role: null,
                                dateDebut: null,
                                dateFin: null,
                                actif: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('tache', null, { reload: true });
                }, function() {
                    $state.go('tache');
                });
            }]
        })
        .state('tache.edit', {
            parent: 'tache',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tache/tache-dialog.html',
                    controller: 'TacheDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    windowClass:'center-modal',
                    size: 'md',
                    resolve: {
                        entity: ['Tache', function(Tache) {
                            return Tache.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tache', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tache.delete', {
            parent: 'tache',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tache/tache-delete-dialog.html',
                    controller: 'TacheDeleteController',
                    controllerAs: 'vm',
                    windowClass:'center-modal',
                    size: 'md',
                    resolve: {
                        entity: ['Tache', function(Tache) {
                            return Tache.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tache', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
