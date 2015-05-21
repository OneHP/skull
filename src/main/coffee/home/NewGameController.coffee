angular.module('controllers')
.controller('NewGameController',['$scope','$location','HomeService',($scope,$location,HomeService) ->

    $scope.player = {name:''}
    $scope.creatingGame = false;

    $scope.back = ->
      $location.path('home')

    $scope.createIsDisabled = ->
      $scope.creatingGame or $scope.player.name is ''

    $scope.create = ->
      return if $scope.createIsDisabled()
      $scope.creatingGame = true
      HomeService.newGame($scope.player.name, -> $scope.creatingGame = false)

  ])