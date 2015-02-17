(function () {
    'use strict';

    /* App Module */

    angular.module('academy', ['motech-dashboard', 'academy.controllers', 'academy.directives', 'academy.services', 'ngCookies'])
        .config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.
                when('/helloWorld/', {templateUrl: '../academy/resources/partials/say-hello.html', controller: 'AcademyHelloWorldController'});
    }]);
}());
