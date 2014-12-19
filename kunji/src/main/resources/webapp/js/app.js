(function () {
    'use strict';

    /* App Module */

    angular.module('kunji', ['motech-dashboard', 'kunji.controllers', 'kunji.directives', 'kunji.services', 'ngCookies'])
        .config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.
                when('/helloWorld/', {templateUrl: '../kunji/resources/partials/say-hello.html', controller: 'KunjiHelloWorldController'});
    }]);
}());
