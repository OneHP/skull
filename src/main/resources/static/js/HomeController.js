// Generated by CoffeeScript 1.9.2
(function() {
  angular.module('controllers').controller('HomeController', [
    '$scope', '$location', function($scope, $location) {
      $scope.newGame = function() {
        return $location.path('new-game');
      };
      return $scope.joinGame = function() {
        return $location.path('join-game');
      };
    }
  ]);

}).call(this);

//# sourceMappingURL=HomeController.js.map
