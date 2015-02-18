(function () {
    'use strict';

    /* App Module */

    angular.module('frontlineworker', ['motech-dashboard', 'frontlineworker.controllers', 'frontlineworker.directives', 'frontlineworker.services', 'ngCookies'])
        .config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.
                when('/helloWorld/', {templateUrl: '../frontlineworker/resources/partials/say-hello.html', controller: 'FlwHelloWorldController'});
    }]);
}());
