/*(function() {
    'use strict';

    angular
        .module('iconlabApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('viewstate', {
            //abstract:true,
            parent: 'app',

            url: 'pojetGen',
            data: {
                authorities: []
            },
            views: {
                'viewzone@': {
                    controller: 'ProjetCompteController',
                    controllerAs: 'vm',
                    templateUrl: 'app/entities/projet/projetcompte.html'
                    
                }
            }
        }).state('projetcompte', {
                parent: 'viewstate',
                url: '/projet/compte/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Projet'
                },
                views: {
                    'projetcontent@viewzone': {
                        controller: 'ProjetCompteController',
                        controllerAs: 'vm',
                        templateUrl: 'app/entities/projet/projetcomptes.html'
                        
                    }
                },
                resolve: {
                    compte: ['$stateParams', 'Compte', function($stateParams, Compte) {
                        console.log("id est "+$stateParams.id);
                        return Compte.get({id : $stateParams.id}).$promise;
                    }]
                }
            }).state('tache-projet', {
                parent: 'viewstate',

                url: '/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ListeTache'
                },
                views: {
                    'taskcontent@viewzone': {
                        controller: 'TacheProjetController',
                        controllerAs: 'vm',
                        templateUrl: 'app/entities/tache/listeTache.html'
                       
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Projet', function($stateParams, Projet) {
                        return Projet.get({id : $stateParams.id}).$promise;
                    }]
                }
            });
    }
})();
*/