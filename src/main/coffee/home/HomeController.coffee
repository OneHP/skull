angular.module('controllers')
.controller('HomeController',['$scope','$location',($scope,$location) ->

    $scope.newGame = ->
      $location.path('new-game')

    $scope.joinGame = ->
      $location.path('join-game')

])