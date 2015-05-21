// Generated by CoffeeScript 1.9.2
(function() {
  angular.module('controllers').controller('NewGameController', [
    '$scope', '$location', 'HomeService', function($scope, $location, HomeService) {
      $scope.player = {
        name: ''
      };
      $scope.creatingGame = false;
      $scope.back = function() {
        return $location.path('home');
      };
      $scope.createIsDisabled = function() {
        return $scope.creatingGame || $scope.player.name === '';
      };
      return $scope.create = function() {
        if ($scope.createIsDisabled()) {
          return;
        }
        $scope.creatingGame = true;
        return HomeService.newGame($scope.player.name, function() {
          return $scope.creatingGame = false;
        });
      };
    }
  ]);

}).call(this);

//# sourceMappingURL=NewGameController.js.map