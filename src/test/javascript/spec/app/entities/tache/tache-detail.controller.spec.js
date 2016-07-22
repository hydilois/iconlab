'use strict';

describe('Controller Tests', function() {

    describe('Tache Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockTache, MockProjet, MockPointAvancement, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockTache = jasmine.createSpy('MockTache');
            MockProjet = jasmine.createSpy('MockProjet');
            MockPointAvancement = jasmine.createSpy('MockPointAvancement');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Tache': MockTache,
                'Projet': MockProjet,
                'PointAvancement': MockPointAvancement,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("TacheDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'iconlabApp:tacheUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
