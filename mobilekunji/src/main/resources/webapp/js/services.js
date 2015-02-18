(function () {
    'use strict';

    /* Services */

    var services = angular.module('kunji.services', ['ngResource']);

    services.factory('HelloWorld', function($resource) {
        return $resource('../kunji/sayHello');
    });
}());
