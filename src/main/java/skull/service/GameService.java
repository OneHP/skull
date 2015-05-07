package skull.service;

import skull.domain.Game;
import skull.service.exception.InsufficientPlayersException;

public interface GameService {

    Game createGame(String playerName);

    Game getGame(Long id);

    Game addPlayer(Long gameId, String playerName);

    Game startGame(Long gameId) throws InsufficientPlayersException;
}
