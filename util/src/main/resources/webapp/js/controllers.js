(function() {
    'use strict';

    /* Controllers */
    var controllers = angular.module('helloWorld.controllers', []);

    controllers.controller('HelloWorldController', function($scope, $http, HelloWorld) {

        $scope.sayHelloResult = '';
        $scope.sayHelloCount = 0;

        $scope.sayHello = function() {
            var messageKey = 'util.info.noResponse';
            $scope.sayHelloResult = $scope.msg(messageKey);
            HelloWorld.get({}, function(response) {
                $scope.sayHelloResult = response.message;
                messageKey = 'util.info.serviceResponse';
                motechAlert(response.message, messageKey);
                $scope.sayHelloCount++;
            });
        };

    });
}());
