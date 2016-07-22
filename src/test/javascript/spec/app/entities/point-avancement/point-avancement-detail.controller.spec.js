'use strict';

describe('Controller Tests', function() {

    describe('PointAvancement Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPointAvancement, MockTache;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPointAvancement = jasmine.createSpy('MockPointAvancement');
            MockTache = jasmine.createSpy('MockTache');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'PointAvancement': MockPointAvancement,
                'Tache': MockTache
            };
            createController = function() {
                $injector.get('$controller')("PointAvancementDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'iconlabApp:pointAvancementUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
