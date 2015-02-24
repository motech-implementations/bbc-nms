(function () {
    'use strict';

    /* Services */

    var services = angular.module('mobileacademy.services', ['ngResource']);

    services.factory('MobileAcademyWorld', function($resource) {
        return $resource('../mobileacademy/sayHello');
    });
}());
