angular.module('skull').config(['$routeProvider', ($routeProvider) ->
  $routeProvider
  .when('/home',{
    templateUrl: 'partials/home/home.html',
    controller: 'HomeController'
  })
  .when('/new-game',{
    templateUrl: 'partials/home/new-game.html',
    controller: 'NewGameController'
  })
  .when('/join-game',{
    templateUrl: 'partials/home/join-game.html',
    controller: 'JoinGameController'
  })
  .otherwise({
    redirectTo: '/home'
  })
])