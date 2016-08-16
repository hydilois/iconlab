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
            'zingchart-angularjs',
            //Augmentation des modules de gantt
            'gantt',
            'gantt.sortable',
            'gantt.movable',
            'gantt.drawtask',
            'gantt.tooltips',
            'gantt.bounds',
            'gantt.progress',
            'gantt.table',
            'gantt.tree',
            'gantt.groups',
            'bw.paging',
            'ngAnimate'
        ]).run(run);

    run.$inject = ['stateHandler'];

    function run(stateHandler) {
        stateHandler.initialize();
    }

     angular
            .module('iconlabApp').filter("startFrom", function () {
                 return function (data,start) {
                    //var data;
                     return data.slice(start);
                 };
});
}

)();
