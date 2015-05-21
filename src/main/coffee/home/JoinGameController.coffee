angular.module('controllers')
.controller('JoinGameController',['$scope','$location',($scope,$location) ->

    $scope.back = ->
      $location.path('home')

])