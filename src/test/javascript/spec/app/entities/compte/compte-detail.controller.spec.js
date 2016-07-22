'use strict';

describe('Controller Tests', function() {

    describe('Compte Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCompte, MockProjet, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCompte = jasmine.createSpy('MockCompte');
            MockProjet = jasmine.createSpy('MockProjet');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Compte': MockCompte,
                'Projet': MockProjet,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("CompteDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'iconlabApp:compteUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
