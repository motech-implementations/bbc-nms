(function() {
    'use strict';

    /* Controllers */
    var controllers = angular.module('mobileacademy.controllers', []);

    controllers.controller('MobileAcademyController', function($scope, $http, MobileAcademy) {

        $scope.sayHelloResult = '';
        $scope.sayHelloCount = 0;

        $scope.sayHello = function() {
            var messageKey = 'mobileacademy.info.noResponse';
            $scope.sayHelloResult = $scope.msg(messageKey);
            HelloWorld.get({}, function(response) {
                $scope.sayHelloResult = response.message;
                messageKey = 'mobileacademy.info.serviceResponse';
                motechAlert(response.message, messageKey);
                $scope.sayHelloCount++;
            });
        };

    });
}());
