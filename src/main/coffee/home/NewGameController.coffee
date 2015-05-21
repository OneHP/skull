angular.module('controllers')
.controller('NewGameController',['$scope','$location',($scope,$location) ->

    $scope.back = ->
      $location.path('home')

  ])