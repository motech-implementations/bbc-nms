(function () {
    'use strict';

    /* App Module */

    angular.module('kilkariobd', ['motech-dashboard', 'kilkariobd.controllers', 'kilkariobd.directives', 'kilkariobd.services', 'ngCookies'])
        .config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.
                when('/helloWorld/', {templateUrl: '../kilkariobd/resources/partials/say-hello.html', controller: 'KilkariobdHelloWorldController'});
    }]);
}());
