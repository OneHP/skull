package skull.controller;

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import skull.domain.Game;
import skull.service.GameService;

@RestController
public class GameController {

    @Autowired
    private GameService gameService;

    @RequestMapping("/game")
    public GameView createGame(@RequestBody PlayerRequest playerRequest){
        final Game game = this.gameService.createGame(playerRequest.getName());
        return GameView.fromGame(game,game.getPlayers().get(0).getId());
    }

    @RequestMapping(value = "/game/{id}", method = RequestMethod.GET)
    public GameView game(@PathVariable Long id, @RequestParam("playerId") Long playerId){
        return GameView.fromGame(this.gameService.getGame(id),playerId);
    }

    @RequestMapping("/game/{key}")
    public GameView addPlayer(@PathVariable String key, @RequestBody PlayerRequest playerRequest){
        final Game game = this.gameService.addPlayer(key, playerRequest.getName());
        return GameView.fromGame(game, Iterables.getLast(game.getPlayers()).getId());
    }


}
