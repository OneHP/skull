package skull.service;

import skull.domain.Card;
import skull.domain.Game;
import skull.domain.Player;
import skull.service.exception.*;

public interface GameService {

    Game createGame(String playerName);

    Game getGame(Long id);

    Game addPlayer(String key, String playerName);

    Game startGame(Long gameId) throws InsufficientPlayersException;

    Game layCard(Long gameId, Long playerId, Card card) throws PlayerActingOutOfTurnException, IncorrectRoundPhaseException, CardNotInHandException, GameNotStartedException;

    Game bid(Long gameId, Long playerId, int bid) throws GameNotStartedException, PlayerActingOutOfTurnException, IncorrectRoundPhaseException, BiddingTooEarlyException, BidTooLowException;

    Game optOutOfBidding(Long gameId, Long playerId) throws GameNotStartedException, PlayerActingOutOfTurnException, IncorrectRoundPhaseException;

    Game flipOwnCards(Long gameId, Long playerId) throws GameNotStartedException, PlayerActingOutOfTurnException, IncorrectRoundPhaseException, AlreadyRevealedCardException;

    /**
     *
     * @param gameId
     * @param playerId
     * @param otherPlayerId
     * @param index Index of the card to be flipped. 0 indexed where 0 is the first card laid (bottom of pile, if there is more than one card).
     * @return
     */
    Game flipOtherPlayerCard(Long gameId, Long playerId, Long otherPlayerId, int index) throws GameNotStartedException, PlayerActingOutOfTurnException, IncorrectRoundPhaseException, NotYetRevealedOwnCardsException, AlreadyRevealedCardException;

}
