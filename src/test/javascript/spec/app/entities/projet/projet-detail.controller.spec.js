'use strict';

describe('Controller Tests', function() {

    describe('Projet Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockProjet, MockCompte, MockMessageHierachique, MockCommentaire, MockTache, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockProjet = jasmine.createSpy('MockProjet');
            MockCompte = jasmine.createSpy('MockCompte');
            MockMessageHierachique = jasmine.createSpy('MockMessageHierachique');
            MockCommentaire = jasmine.createSpy('MockCommentaire');
            MockTache = jasmine.createSpy('MockTache');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Projet': MockProjet,
                'Compte': MockCompte,
                'MessageHierachique': MockMessageHierachique,
                'Commentaire': MockCommentaire,
                'Tache': MockTache,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("ProjetDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'iconlabApp:projetUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
