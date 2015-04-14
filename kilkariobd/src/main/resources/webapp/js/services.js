(function () {
    'use strict';

    /* Services */

    var services = angular.module('kilkariobd.services', ['ngResource']);

    services.factory('HelloWorld', function($resource) {
        return $resource('../kilkariobd/sayHello');
    });
}());
