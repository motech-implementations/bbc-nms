(function () {
    'use strict';

    /* Services */

    var services = angular.module('academy.services', ['ngResource']);

    services.factory('HelloWorld', function($resource) {
        return $resource('../academy/sayHello');
    });
}());
