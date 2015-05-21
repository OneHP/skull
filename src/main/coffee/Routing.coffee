angular.module('skull').config(['$routeProvider', ($routeProvider) ->
  $routeProvider
  .when('/home',{
    templateUrl: 'partials/home.html',
    controller: 'HomeController'
  })
  .otherwise({
    redirectTo: '/home'
  })
])