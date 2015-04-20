(function() {
    'use strict';

    /* Controllers */
    var controllers = angular.module('kilkariobd.controllers', []);

    controllers.controller('KilkariobdHelloWorldController', function($scope, $http, HelloWorld) {

        $scope.sayHelloResult = '';
        $scope.sayHelloCount = 0;

        $scope.sayHello = function() {
            var messageKey = 'kilkariobd.info.noResponse';
            $scope.sayHelloResult = $scope.msg(messageKey);
            HelloWorld.get({}, function(response) {
                $scope.sayHelloResult = response.message;
                messageKey = 'kilkariobd.info.serviceResponse';
                motechAlert(response.message, messageKey);
                $scope.sayHelloCount++;
            });
        };

    });
}());
