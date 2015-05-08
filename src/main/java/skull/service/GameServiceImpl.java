package skull.service;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import skull.domain.*;
import skull.repo.GameRepository;
import skull.repo.PlayerRepository;
import skull.service.exception.*;
import skull.util.RandomKeyUtil;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Random;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    @Transactional
    public Game createGame(String playerName) {

        Game game = new Game();
        game.setKey(RandomKeyUtil.generateKey());
        game.setPlayers(Lists.newArrayList(Player.create(playerName)));

        return this.gameRepository.save(game);
    }

    @Override
    @Transactional
    public Game getGame(Long id) {
        return this.gameRepository.findOne(id);
    }

    @Override
    @Transactional
    public Game addPlayer(Long gameId, String playerName) {

        final Game game = this.gameRepository.findOne(gameId);
        game.getPlayers().add(Player.create(playerName));

        return this.gameRepository.save(game);
    }

    @Override
    @Transactional
    public Game startGame(Long gameId) throws InsufficientPlayersException {
        final Game game = this.gameRepository.findOne(gameId);

        final int numberOfPlayers = game.getPlayers().size();
        if(numberOfPlayers < 2){
            throw new InsufficientPlayersException(numberOfPlayers,2);
        }

        game.setStarted(true);

        Round round = new Round();
        round.setStartingPlayer(game.getPlayers().get(new Random().nextInt(numberOfPlayers)));

        RoundState initialRoundState = new RoundState();
        initialRoundState.setMaxBid(0);
        initialRoundState.setRoundPhase(RoundPhase.LAYING);
        initialRoundState.setPlayerToAct(round.getStartingPlayer());
        List<PlayerState> playerStates = Lists.newArrayList();
        for(Player player:game.getPlayers()){
            playerStates.add(PlayerState.create(player));
        }
        initialRoundState.setPlayerStates(playerStates);

        round.setRoundStates(Lists.newArrayList(initialRoundState));
        game.setRounds(Lists.newArrayList(round));

        return this.gameRepository.save(game);
    }

    @Override
    @Transactional
    public Game layCard(Long gameId, Long playerId, Card card) throws PlayerActingOutOfTurnException, IncorrectRoundPhaseException, CardNotInHandException, GameNotStartedException {
        final Game game = this.gameRepository.findOne(gameId);

        if(!game.getStarted()){
            throw new GameNotStartedException();
        }

        final Round round = Iterables.getLast(game.getRounds());
        final RoundState roundState = Iterables.getLast(round.getRoundStates());

        final Player playerToAct = roundState.getPlayerToAct();
        final Player playerActing = this.playerRepository.findOne(playerId);

        if(!playerToAct.equals(playerActing)){
            throw new PlayerActingOutOfTurnException(playerActing,playerToAct);
        }

        if(!roundState.getRoundPhase().equals(RoundPhase.LAYING)){
            throw new IncorrectRoundPhaseException(roundState.getRoundPhase(), RoundPhase.LAYING);
        }

        final PlayerState playerState = getPlayerState(roundState, playerActing);

        if(!playerHasCardInHand(playerState, card)){
            throw new CardNotInHandException(card);
        }

        final RoundState nextRoundState = roundState.copy();
        final PlayerState nextRoundPlayerState = getPlayerState(nextRoundState,playerActing);
        nextRoundPlayerState.getCardsOnTable().add(card);

        nextRoundState.setPlayerToAct(nextPlayerInTurn(game, playerActing));
        round.getRoundStates().add(nextRoundState);

        return this.gameRepository.save(game);
    }

    private PlayerState getPlayerState(RoundState roundState, Player playerActing) {
        return Iterables.find(
                roundState.getPlayerStates(), (state -> state.getPlayer().equals(playerActing))
        );
    }

    private boolean playerHasCardInHand(PlayerState playerState, Card card) {
        if(card.equals(Card.SKULL)){
            return playerState.getCardsOnTable().stream().filter(c -> c.equals(Card.SKULL)).count() < playerState.getPlayer().getSkulls();
        }
        return playerState.getCardsOnTable().stream().filter(c -> c.equals(Card.ROSE)).count() < playerState.getPlayer().getRoses();
    }

    private Player nextPlayerInTurn(Game game, Player playerActing) {
        return game.getPlayers().get(
                (game.getPlayers().indexOf(playerActing) + 1) % game.getPlayers().size()
        );
    }


}
