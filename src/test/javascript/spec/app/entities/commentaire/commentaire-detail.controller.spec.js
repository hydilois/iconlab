'use strict';

describe('Controller Tests', function() {

    describe('Commentaire Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCommentaire, MockProjet;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCommentaire = jasmine.createSpy('MockCommentaire');
            MockProjet = jasmine.createSpy('MockProjet');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Commentaire': MockCommentaire,
                'Projet': MockProjet
            };
            createController = function() {
                $injector.get('$controller')("CommentaireDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'iconlabApp:commentaireUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
