(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('compte', {
                parent: 'entity',
                url: '/compte?page&sort&search',
                data: {
                    authorities: ['ROLE_USER','ROLE_CEO'],
                    pageTitle: 'Comptes'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/compte/comptes.html',
                        controller: 'CompteController',
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
            .state('compte-detail', {
                parent: 'entity',
                url: '/compte/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Compte'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/compte/compte-detail.html',
                        controller: 'CompteDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Compte', function($stateParams, Compte) {
                        return Compte.get({id : $stateParams.id}).$promise;
                    }]
                }
            })
            .state('compte.new', {
                parent: 'compte',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/compte/compte-dialog.html',
                        controller: 'CompteDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    nom: null,
                                    logo: null,
                                    logoContentType: null,
                                    actif: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function() {
                        $state.go('compte', null, { reload: true });
                    }, function() {
                        $state.go('compte');
                    });
                }]
            })
            .state('compte.edit', {
                parent: 'compte',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/compte/compte-dialog.html',
                        controller: 'CompteDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Compte', function(Compte) {
                                return Compte.get({id : $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function() {
                        $state.go('compte', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    });
                }]
            })
            .state('compte.delete', {
                parent: 'compte',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/compte/compte-delete-dialog.html',
                        controller: 'CompteDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['Compte', function(Compte) {
                                return Compte.get({id : $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function() {
                        $state.go('compte', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    });
                }]
            });
    }

})();
