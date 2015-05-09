package skull.service;

import skull.domain.Card;
import skull.domain.Game;
import skull.domain.Player;
import skull.service.exception.*;

public interface GameService {

    Game createGame(String playerName);

    Game getGame(Long id);

    Game addPlayer(Long gameId, String playerName);

    Game startGame(Long gameId) throws InsufficientPlayersException;

    Game layCard(Long gameId, Long playerId, Card card) throws PlayerActingOutOfTurnException, IncorrectRoundPhaseException, CardNotInHandException, GameNotStartedException;

    Game bid(Long gameId, Long playerId, int bid) throws GameNotStartedException, PlayerActingOutOfTurnException, IncorrectRoundPhaseException, BiddingTooEarlyException, BidTooLowException, BidTooHighException;

    Game optOutOfBidding(Long gameId, Long playerId) throws GameNotStartedException, PlayerActingOutOfTurnException, IncorrectRoundPhaseException;

}
