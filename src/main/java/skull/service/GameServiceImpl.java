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
import java.util.stream.Collectors;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private RandomService randomService;

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
        round.setStartingPlayer(game.getPlayers().get(this.randomService.randomInt(numberOfPlayers)));

        RoundState initialRoundState = new RoundState();
        initialRoundState.setMaxBid(0);
        initialRoundState.setRoundPhase(RoundPhase.LAYING);
        initialRoundState.setPlayerToAct(round.getStartingPlayer());
        initialRoundState.setPlayerStates(game.getPlayers().stream()
                .map(player -> PlayerState.create(player))
                .collect(Collectors.toList()));

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
        final PlayerState nextRoundPlayerState = getPlayerState(nextRoundState, playerActing);
        nextRoundPlayerState.getCardsOnTable().add(card);
        nextRoundPlayerState.getHand().playCard(card);

        nextRoundState.setPlayerToAct(nextPlayerInTurn(game, nextRoundState, playerActing));
        round.getRoundStates().add(nextRoundState);

        return this.gameRepository.save(game);
    }

    @Override
    public Game bid(Long gameId, Long playerId, int bid) throws GameNotStartedException, PlayerActingOutOfTurnException, IncorrectRoundPhaseException, BiddingTooEarlyException, BidTooLowException {
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

        if(roundState.getRoundPhase().equals(RoundPhase.RESOLUTION)){
            throw new IncorrectRoundPhaseException(roundState.getRoundPhase(), RoundPhase.BIDDING);
        }

        final RoundState nextRoundState = roundState.copy();

        if(roundState.getRoundPhase().equals(RoundPhase.LAYING)){
            if(roundState.getPlayerStates().stream()
                    .anyMatch(playerState -> playerState.getCardsOnTable().isEmpty())){
                throw new BiddingTooEarlyException(playerActing);
            }else {
                nextRoundState.setRoundPhase(RoundPhase.BIDDING);
            }
        }

        if(bid <= roundState.getMaxBid()){
            throw new BidTooLowException(bid, roundState.getMaxBid());
        }

        final PlayerState nextRoundPlayerState = getPlayerState(nextRoundState, playerActing);

        nextRoundState.setMaxBid(bid);
        nextRoundPlayerState.setBid(bid);

        nextRoundState.setPlayerToAct(nextPlayerInTurn(game, nextRoundState, playerActing));
        round.getRoundStates().add(nextRoundState);

        return this.gameRepository.save(game);
    }

    @Override
    @Transactional
    public Game optOutOfBidding(Long gameId, Long playerId) throws GameNotStartedException, PlayerActingOutOfTurnException, IncorrectRoundPhaseException {
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

        if(!roundState.getRoundPhase().equals(RoundPhase.BIDDING)){
            throw new IncorrectRoundPhaseException(roundState.getRoundPhase(), RoundPhase.BIDDING);
        }

        final RoundState nextRoundState = roundState.copy();
        final PlayerState nextRoundPlayerState = getPlayerState(nextRoundState, playerActing);

        nextRoundPlayerState.setOutOfBidding(true);

        nextRoundState.setPlayerToAct(nextPlayerInTurn(game, nextRoundState, playerActing));

        if(nextRoundState.getPlayerStates().stream()
                .filter(playerState -> !playerState.getOutOfBidding())
                .count() < 2){
           nextRoundState.setRoundPhase(RoundPhase.RESOLUTION);
        }

        round.getRoundStates().add(nextRoundState);

        return this.gameRepository.save(game);
    }

    @Override
    public Game flipOwnCards(Long gameId, Long playerId) throws GameNotStartedException, PlayerActingOutOfTurnException, IncorrectRoundPhaseException, AlreadyRevealedCardException {
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

        if(!roundState.getRoundPhase().equals(RoundPhase.RESOLUTION)){
            throw new IncorrectRoundPhaseException(roundState.getRoundPhase(), RoundPhase.RESOLUTION);
        }

        if(getPlayerState(roundState,playerActing).getNumberOfRevealedCards() > 0){
            throw new AlreadyRevealedCardException();
        }

        final RoundState nextRoundState = roundState.copy();
        final PlayerState nextRoundPlayerState = getPlayerState(nextRoundState, playerActing);

        nextRoundPlayerState.setNumberOfRevealedCards(nextRoundPlayerState.getCardsOnTable().size());

        if (nextRoundPlayerState.getCardsOnTable().stream().anyMatch(card -> Card.SKULL.equals(card))){
            loseRound(game, playerActing);
        }else if(nextRoundPlayerState.getNumberOfRevealedCards() >= roundState.getMaxBid()){
            winRound(game, playerActing);
        }

        round.getRoundStates().add(nextRoundState);

        return this.gameRepository.save(game);
    }

    @Override
    public Game flipOtherPlayerCard(Long gameId, Long playerId, Long otherPlayerId, int index) throws GameNotStartedException, PlayerActingOutOfTurnException, IncorrectRoundPhaseException, NotYetRevealedOwnCardsException, AlreadyRevealedCardException {
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

        if(!roundState.getRoundPhase().equals(RoundPhase.RESOLUTION)){
            throw new IncorrectRoundPhaseException(roundState.getRoundPhase(), RoundPhase.RESOLUTION);
        }

        if(getPlayerState(roundState,playerActing).getNumberOfRevealedCards() == 0){
            throw new NotYetRevealedOwnCardsException();
        }

        final Player otherPlayer = game.getPlayers().stream().filter(player -> otherPlayerId.equals(player.getId())).findAny().get();
        final PlayerState otherPlayerState = getPlayerState(roundState, otherPlayer);

        if(otherPlayerState.getNumberOfRevealedCards() >= otherPlayerState.getCardsOnTable().size() - index){
            throw new AlreadyRevealedCardException();
        }

        final RoundState nextRoundState = roundState.copy();
        final PlayerState nextRoundOtherPlayerState = getPlayerState(nextRoundState, otherPlayer);

        nextRoundOtherPlayerState.setNumberOfRevealedCards(
                nextRoundOtherPlayerState.getNumberOfRevealedCards() + 1
        );

        final Card card = otherPlayerState.getCardsOnTable().get(index);
        if(Card.SKULL.equals(card)){
            loseRound(game,playerActing);
        }else if(nextRoundState.getPlayerStates().stream()
                .map(playerState -> playerState.getNumberOfRevealedCards())
                .reduce((a,b) -> a + b).get() >= roundState.getMaxBid()){
            winRound(game,playerActing);
        }

        round.getRoundStates().add(nextRoundState);

        return this.gameRepository.save(game);
    }

    private void winRound(Game game, Player playerActing) {
        playerActing.setPoints(playerActing.getPoints() + 1);

        if(playerActing.getPoints() == 2){
            game.setStarted(false);
            game.setWinner(playerActing);
        }else{
            Round nextRound = new Round();
            nextRound.setStartingPlayer(playerActing);

            RoundState nextRoundState = new RoundState();
            nextRoundState.setMaxBid(0);
            nextRoundState.setRoundPhase(RoundPhase.LAYING);
            nextRoundState.setPlayerToAct(nextRound.getStartingPlayer());
            nextRoundState.setPlayerStates(game.getPlayers().stream()
                    .filter(fPlayer -> fPlayer.hasCards())
                    .map(player -> PlayerState.create(player))
                    .collect(Collectors.toList()));

            nextRound.setRoundStates(Lists.newArrayList(nextRoundState));
            game.getRounds().add(nextRound);
        }
    }

    private void loseRound(Game game, Player playerActing) {
        if(playerActing.getSkulls() > 0 && this.randomService.randomInt(playerActing.getRoses()) == 0){
            playerActing.setSkulls(0);
        }else{
            playerActing.setRoses(playerActing.getRoses() - 1);
        }

        if(game.getPlayers().stream()
                .filter(Player::hasCards)
                .count() < 2){
            game.setStarted(false);
            game.setWinner(game.getPlayers().stream()
                    .filter(Player::hasCards)
                    .findAny().get());
        }else {
            Round nextRound = new Round();
            nextRound.setStartingPlayer(playerActing.hasCards()
                    ? playerActing
                    : nextPlayerInGame(game, playerActing));

            RoundState nextRoundState = new RoundState();
            nextRoundState.setMaxBid(0);
            nextRoundState.setRoundPhase(RoundPhase.LAYING);
            nextRoundState.setPlayerToAct(nextRound.getStartingPlayer());
            nextRoundState.setPlayerStates(game.getPlayers().stream()
                    .filter(fPlayer -> fPlayer.hasCards())
                    .map(player -> PlayerState.create(player))
                    .collect(Collectors.toList()));

            nextRound.setRoundStates(Lists.newArrayList(nextRoundState));
            game.getRounds().add(nextRound);
        }
    }

    private PlayerState getPlayerState(RoundState roundState, Player playerActing) {
        return Iterables.find(
                roundState.getPlayerStates(), (state -> state.getPlayer().equals(playerActing))
        );
    }

    private boolean playerHasCardInHand(PlayerState playerState, Card card) {
        if(card.equals(Card.SKULL)){
            return playerState.getHand().getSkulls() > 0;
        }
        return playerState.getHand().getRoses() > 0;
    }

    private Player nextPlayerInTurn(Game game, RoundState roundState, Player playerActing) {
        Player nextPlayerInTurn = game.getPlayers().get(
                (game.getPlayers().indexOf(playerActing) + 1) % game.getPlayers().size());
        if(getPlayerState(roundState,nextPlayerInTurn).getOutOfBidding()){
            return nextPlayerInTurn(game, roundState, nextPlayerInTurn);
        }
        return nextPlayerInTurn;
    }

    private Player nextPlayerInGame(Game game,  Player playerActing) {
        Player nextPlayerInGame = game.getPlayers().get(
                (game.getPlayers().indexOf(playerActing) + 1) % game.getPlayers().size());
        if(nextPlayerInGame.hasCards()){
            return nextPlayerInGame;

        }
        return nextPlayerInGame(game, nextPlayerInGame);
    }

}
