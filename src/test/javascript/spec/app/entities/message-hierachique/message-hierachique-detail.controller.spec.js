'use strict';

describe('Controller Tests', function() {

    describe('MessageHierachique Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockMessageHierachique, MockProjet;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockMessageHierachique = jasmine.createSpy('MockMessageHierachique');
            MockProjet = jasmine.createSpy('MockProjet');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'MessageHierachique': MockMessageHierachique,
                'Projet': MockProjet
            };
            createController = function() {
                $injector.get('$controller')("MessageHierachiqueDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'iconlabApp:messageHierachiqueUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
