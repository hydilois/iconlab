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
                authorities: ['ROLE_USER'],
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
        .state('message-hierachique.new', {
            parent: 'message-hierachique',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/message-hierachique/message-hierachique-dialog.html',
                    controller: 'MessageHierachiqueDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                contenu: null,
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
        .state('message-hierachique.edit', {
            parent: 'message-hierachique',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/message-hierachique/message-hierachique-dialog.html',
                    controller: 'MessageHierachiqueDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
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
