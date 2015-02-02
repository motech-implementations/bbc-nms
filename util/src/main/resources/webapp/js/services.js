(function () {
    'use strict';

    /* Services */

    var services = angular.module('helloWorld.services', ['ngResource']);

    services.factory('HelloWorld', function($resource) {
        return $resource('../util/sayHello');
    });
}());
