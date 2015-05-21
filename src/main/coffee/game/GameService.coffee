angular.module('services')
.factory('GameModel',[ ->
    model = {game:null,player:null}

    return model
  ])

angular.module('services')
.factory('GameService',['$http','GameModel',($http, GameModel) ->
    service = {}

    return service
  ])