angular.module('services')
.factory('HomeService',['$http','GameModel',($http,GameModel) ->
  service = {}

  service.newGame = (playerName, failureCallback) ->
    $http.post('/game',{name:playerName})
      .success((data, status, headers, config) ->
        GameModel.game = data
        GameModel.player = data.players[data.players.length - 1]
      )
      .error((data, status, headers, config) ->
        failureCallback();
      )

  return service
])