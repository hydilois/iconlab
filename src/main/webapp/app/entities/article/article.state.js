(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('article', {
            parent: 'entity',
            url: '/article?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Articles'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/article/articles.html',
                    controller: 'ArticleController',
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
        .state('article-detail', {
            parent: 'entity',
            url: '/article/{id}',
            data: {
                authorities: [],
                pageTitle: 'Article'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/article/article-detail.html',
                    controller: 'ArticleDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Article', function($stateParams, Article) {
                    return Article.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('article.new', {
            parent: 'article',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/article/article-dialog.html',
                    controller: 'ArticleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                titre: null,
                                contenu: null,
                                datePub: null,
                                fichier: null,
                                fichierContentType: null,
                                actif: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('article', null, { reload: true });
                }, function() {
                    $state.go('article');
                });
            }]
        })
        .state('article.edit', {
            parent: 'article',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/article/article-dialog.html',
                    controller: 'ArticleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Article', function(Article) {
                            return Article.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('article', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('article.delete', {
            parent: 'article',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/article/article-delete-dialog.html',
                    controller: 'ArticleDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Article', function(Article) {
                            return Article.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('article', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
