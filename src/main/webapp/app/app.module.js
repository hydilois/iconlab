(function() {
    'use strict';

    angular
        .module('iconlabApp', [
            'ngStorage', 
            'ngResource',
            'ngCookies',
            'ngAria',
            'ngCacheBuster',
            'ngFileUpload',
            'ui.bootstrap',
            'ui.bootstrap.datetimepicker',
            'ui.router',
            'infinite-scroll',
            // jhipster-needle-angularjs-add-module JHipster will add new module here
            'angular-loading-bar',
            //Augmentation des modules de gantt
            'gantt', // angular-gantt.
            'gantt.sortable',
            'gantt.movable',
            'gantt.drawtask',
            'gantt.tooltips',
            'gantt.bounds',
            'gantt.progress',
            'gantt.table',
            'gantt.tree',
            'gantt.groups',
            'gantt.dependencies',
            'gantt.overlap',
            'gantt.resizeSensor',
            'ngAnimate',
            'mgcrea.ngStrap'
        ]).config(['$compileProvider', function($compileProvider) {
    $compileProvider.debugInfoEnabled(false); // Remove debug info (angularJS >= 1.3)
}]).run(run);

    run.$inject = ['stateHandler'];

    function run(stateHandler) {
        stateHandler.initialize();
    }
})();
