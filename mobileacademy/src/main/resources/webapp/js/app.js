(function () {
    'use strict';

    /* App Module */

    angular.module('mobileacademy', ['motech-dashboard', 'mobileacademy.controllers', 'mobileacademy.directives', 'mobileacademy.services', 'ngCookies'])
        .config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.
                when('/mobileacademy/', {templateUrl: '../mobileacademy/resources/partials/add-mobileacademy.html', controller: 'MobileAcademyController'});
    }]);
}());
