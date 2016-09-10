(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('documents', {
            parent: 'entity',
            url: '/documents?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'Documents'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/documents/documents.html',
                    controller: 'DocumentsController',
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
        .state('documents-detail', {
            parent: 'entity',
            url: '/documents/{id}',
            data: {
                authorities: ['ROLE_USER','ROLE_CEO','ROLE_DO','ROLE_PMO'],
                pageTitle: 'Documents'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/documents/documents-detail.html',
                    controller: 'DocumentsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Documents', function($stateParams, Documents) {
                    return Documents.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('documents.new', {
            parent: 'documents',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
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
                        entity: function () {
                            return {
                                titre: null,
                                sender:null,
                                fichier: null,
                                fichierContentType: null,
                                mode: null,
                                actif: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('documents', null, { reload: true });
                }, function() {
                    $state.go('documents');
                });
            }]
        })
        .state('home.newuserdocument', {
            parent: 'home',
            url: '/new/document',
            data: {
                authorities: ['ROLE_USER','ROLE_CEO','ROLE_DO','ROLE_PMO']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/documents/documents-dialogU.html',
                    controller: 'DocumentsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    windowClass:'center-modal',
                    size: 'md',
                    resolve: {
                        entity: function () {
                            return {
                                titre: null,
                                fichier: null,
                                fichierContentType: null,
                                mode: null,
                                actif: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('home', null, { reload: true });
                }, function() {
                    $state.go('home');
                });
            }]
        })
        .state('app.patache.newdocpa', {
            parent: 'app.patache',
            url: '/new/documentpa',
            data: {
                authorities: ['ROLE_USER','ROLE_CEO','ROLE_DO','ROLE_PMO']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/documents/documents-dialogU.html',
                    controller: 'DocumentsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    windowClass:'center-modal',
                    size: 'md',
                    resolve: {
                        entity: function () {
                            return {
                                titre: null,
                                fichier: null,
                                fichierContentType: null,
                                mode: null,
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
        })
        .state('app.tacheprojet.newuserdocument', {//nouveau document vu du chef d'un projet
            parent: 'app.tacheprojet',
            url: '/new/document1',
            data: {
                authorities: ['ROLE_USER','ROLE_CEO','ROLE_DO','ROLE_PMO']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/documents/documents-dialogU.html',
                    controller: 'DocumentsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    windowClass:'center-modal',
                    size: 'md',
                    resolve: {
                        entity: function () {
                            return {
                                titre: null,
                                fichier: null,
                                fichierContentType: null,
                                mode: null,
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
        }).state('app.projetcompte.newuserdocument', {//nouveau document vu du chef de compte
                parent: 'app.projetcompte',
                url: '/new/document',
                data: {
                    authorities: ['ROLE_USER','ROLE_CEO','ROLE_DO','ROLE_PMO']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/documents/documents-dialogU.html',
                        controller: 'DocumentsDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        windowClass:'center-modal',
                        size: 'md',
                        resolve: {
                            entity: function () {
                                return {
                                    titre: null,
                                    fichier: null,
                                    fichierContentType: null,
                                    mode: null,
                                    actif: null,
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
        .state('documents.edit', {//etat de l'édition d'un document par l'administrateur
            parent: 'documents',
            url: '/{id}/edittAd',
            data: {
                authorities: ['ROLE_ADMIN']
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
                            return Documents.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('documents', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('app.projetcompte.edituserdocument', {
            parent: 'app.projetcompte',
            url: '/{iddoc}/editus',
            data: {
                authorities: ['ROLE_USER','ROLE_CEO','ROLE_DO','ROLE_PMO']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/documents/documents-dialogU.html',
                    controller: 'DocumentsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    windowClass:'center-modal',
                    size: 'md',
                    resolve: {
                        entity: ['Documents', function(Documents) {
                            return Documents.get({id : $stateParams.iddoc}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('app.projetcompte', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('documents.delete', {
            parent: 'documents',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/documents/documents-delete-dialog.html',
                    controller: 'DocumentsDeleteController',
                    controllerAs: 'vm',
                    windowClass:'center-modal',
                    size: 'md',
                    resolve: {
                        entity: ['Documents', function(Documents) {
                            return Documents.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('documents', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
