(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('point-avancement', {
            parent: 'entity',
            url: '/point-avancement?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PointAvancements'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/point-avancement/point-avancements.html',
                    controller: 'PointAvancementController',
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
        .state('point-avancement-detail', {
            parent: 'entity',
            url: '/point-avancement/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PointAvancement'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/point-avancement/point-avancement-detail.html',
                    controller: 'PointAvancementDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'PointAvancement', function($stateParams, PointAvancement) {
                    return PointAvancement.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('point-avancement.new', {
            parent: 'point-avancement',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/point-avancement/point-avancement-dialog.html',
                    controller: 'PointAvancementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    windowClass:'center-modal',
                    size: 'md',
                    resolve: {
                        entity: function () {
                            return {
                                libelle: null,
                                description: null,
                                fichier: null,
                                fichierContentType: null,
                                datePub: null,
                                actif: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('point-avancement', null, { reload: true });
                }, function() {
                    $state.go('point-avancement');
                });
            }]
        })
        .state('app.patache.newpa', {
            parent: 'app.patache',
            url: '/new/patache',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/point-avancement/pointAvancementDetailuser.html',
                    controller: 'PointAvancementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    windowClass:'center-modal',
                    size: 'md',
                    resolve: {
                        entity: function () {
                            return {
                                libelle: null,
                                description: null,
                                fichier: null,
                                fichierContentType: null,
                                datePub: null,
                                actif: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('app.patache', null, { reload: true });
                }, function() {
                    $state.go('app.patache');
                });
            }]
        }).state('app.patache.editdocumentU', {
                parent: 'app.patache',
                url: '/{iddocpa}/editdocpa',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/documents/documents-dialog.html',
                        controller: 'DocumentsDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        windowClass:'center-modal',
                        size: 'md',
                        resolve: {
                            entity: ['Documents', function(Documents) {
                                return Documents.get({id : $stateParams.iddocpa}).$promise;
                            }]
                        }
                    }).result.then(function() {
                        $state.go('app.patache', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    });
                }]
            })
        .state('point-avancement.edit', {
            parent: 'point-avancement',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/point-avancement/point-avancement-dialog.html',
                    controller: 'PointAvancementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    windowClass:'center-modal',
                    size: 'md',
                    resolve: {
                        entity: ['PointAvancement', function(PointAvancement) {
                            return PointAvancement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('point-avancement', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('point-avancement.delete', {
            parent: 'point-avancement',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/point-avancement/point-avancement-delete-dialog.html',
                    controller: 'PointAvancementDeleteController',
                    controllerAs: 'vm',
                    windowClass:'center-modal',
                    size: 'md',
                    resolve: {
                        entity: ['PointAvancement', function(PointAvancement) {
                            return PointAvancement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('point-avancement', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
