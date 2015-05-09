package skull.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import skull.SkullApplication;
import skull.domain.Card;
import skull.domain.Game;
import skull.domain.Player;
import skull.service.exception.*;

import javax.transaction.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkullApplication.class)
@WebAppConfiguration
public class GameServiceImplIntTest {

    private static String HOST_PLAYER_NAME = "Thomas";
    private static String SECOND_PLAYER_NAME = "Mike";
    private static String THIRD_PLAYER_NAME = "Marie";

    @Autowired
    private GameService serviceUnderTest;

    @Test
    public void canCreateGame(){
        this.serviceUnderTest.createGame(HOST_PLAYER_NAME);
    }

    @Test
    public void canGetGame(){
        final Game game = this.serviceUnderTest.createGame(HOST_PLAYER_NAME);
        this.serviceUnderTest.getGame(game.getId());
    }

    @Test
    public void canAddPlayer(){
        final Game game = this.serviceUnderTest.createGame(HOST_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getId(), SECOND_PLAYER_NAME);
    }

    @Test
    public void canStartGame() throws InsufficientPlayersException {
        final Game game = this.serviceUnderTest.createGame(HOST_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getId(), SECOND_PLAYER_NAME);
        this.serviceUnderTest.startGame(game.getId());
    }

    @Test(expected = InsufficientPlayersException.class)
    public void failsToStartGameWithOnePlayer() throws InsufficientPlayersException {
        final Game game = this.serviceUnderTest.createGame(HOST_PLAYER_NAME);
        this.serviceUnderTest.startGame(game.getId());
    }

    @Test
    @Transactional
    public void canLayCard() throws Exception {
        final Game game = this.serviceUnderTest.createGame(HOST_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getId(), SECOND_PLAYER_NAME);
        this.serviceUnderTest.startGame(game.getId());

        Player startingPlayer = game.getRounds().get(0).getStartingPlayer();
        this.serviceUnderTest.layCard(game.getId(), startingPlayer.getId(), Card.SKULL);
    }

    @Test(expected = GameNotStartedException.class)
    @Transactional
    public void cannotLayCardAsGameNotYetStarted() throws Exception {
        final Game game = this.serviceUnderTest.createGame(HOST_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getId(), SECOND_PLAYER_NAME);

        Player startingPlayer = game.getPlayers().get(0);
        this.serviceUnderTest.layCard(game.getId(),startingPlayer.getId(), Card.SKULL);
    }

    @Test(expected = PlayerActingOutOfTurnException.class)
    @Transactional
    public void cannotLayCardAsActingOutOfTurn() throws Exception {
        final Game game = this.serviceUnderTest.createGame(HOST_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getId(), SECOND_PLAYER_NAME);
        this.serviceUnderTest.startGame(game.getId());

        Player startingPlayer = game.getRounds().get(0).getStartingPlayer();
        Player otherPlayer = game.getPlayers().stream()
                .filter(player -> !player.equals(startingPlayer))
                .findAny().get();
        this.serviceUnderTest.layCard(game.getId(), otherPlayer.getId(), Card.SKULL);
    }

    @Test(expected = CardNotInHandException.class)
    @Transactional
    public void cannotLayCardAsCardNotInHand() throws Exception {
        final Game game = this.serviceUnderTest.createGame(HOST_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getId(), SECOND_PLAYER_NAME);
        this.serviceUnderTest.startGame(game.getId());

        Player startingPlayer = game.getRounds().get(0).getStartingPlayer();
        Player otherPlayer = game.getPlayers().stream()
                .filter(player -> !player.equals(startingPlayer))
                .findAny().get();
        this.serviceUnderTest.layCard(game.getId(), startingPlayer.getId(), Card.SKULL);
        this.serviceUnderTest.layCard(game.getId(), otherPlayer.getId(), Card.SKULL);
        this.serviceUnderTest.layCard(game.getId(), startingPlayer.getId(), Card.SKULL);
    }

    @Test
    @Transactional
    public void canBid() throws Exception {
        final Game game = this.serviceUnderTest.createGame(HOST_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getId(), SECOND_PLAYER_NAME);
        this.serviceUnderTest.startGame(game.getId());

        Player startingPlayer = game.getRounds().get(0).getStartingPlayer();
        Player otherPlayer = game.getPlayers().stream()
                .filter(player -> !player.equals(startingPlayer))
                .findAny().get();
        this.serviceUnderTest.layCard(game.getId(), startingPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), otherPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.bid(game.getId(), startingPlayer.getId(), 1);
        this.serviceUnderTest.bid(game.getId(), otherPlayer.getId(), 2);
    }

    @Test(expected = GameNotStartedException.class)
    @Transactional
    public void cannotBidAsGameNotYetStarted() throws Exception {
        final Game game = this.serviceUnderTest.createGame(HOST_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getId(), SECOND_PLAYER_NAME);

        Player startingPlayer = game.getPlayers().get(0);
        this.serviceUnderTest.bid(game.getId(), startingPlayer.getId(),1);
    }

    @Test(expected = PlayerActingOutOfTurnException.class)
    @Transactional
    public void cannotBidAsActingOutOfTurn() throws Exception {
        final Game game = this.serviceUnderTest.createGame(HOST_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getId(), SECOND_PLAYER_NAME);
        this.serviceUnderTest.startGame(game.getId());

        Player startingPlayer = game.getRounds().get(0).getStartingPlayer();
        Player otherPlayer = game.getPlayers().stream()
                .filter(player -> !player.equals(startingPlayer))
                .findAny().get();
        this.serviceUnderTest.layCard(game.getId(), startingPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), otherPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.bid(game.getId(), otherPlayer.getId(), 1);
    }

    @Test(expected = BidTooLowException.class)
    @Transactional
    public void cannotBidAsBidTooLow() throws Exception {
        final Game game = this.serviceUnderTest.createGame(HOST_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getId(), SECOND_PLAYER_NAME);
        this.serviceUnderTest.startGame(game.getId());

        Player startingPlayer = game.getRounds().get(0).getStartingPlayer();
        Player otherPlayer = game.getPlayers().stream()
                .filter(player -> !player.equals(startingPlayer))
                .findAny().get();
        this.serviceUnderTest.layCard(game.getId(), startingPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), otherPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.bid(game.getId(), startingPlayer.getId(), 1);
        this.serviceUnderTest.bid(game.getId(), otherPlayer.getId(), 1);
    }

    @Test(expected = BidTooHighException.class)
    @Transactional
    public void cannotBidAsBidTooHigh() throws Exception {
        final Game game = this.serviceUnderTest.createGame(HOST_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getId(), SECOND_PLAYER_NAME);
        this.serviceUnderTest.startGame(game.getId());

        Player startingPlayer = game.getRounds().get(0).getStartingPlayer();
        Player otherPlayer = game.getPlayers().stream()
                .filter(player -> !player.equals(startingPlayer))
                .findAny().get();
        this.serviceUnderTest.layCard(game.getId(), startingPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), otherPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.bid(game.getId(), startingPlayer.getId(), 1);
        this.serviceUnderTest.bid(game.getId(), otherPlayer.getId(), 2);
        this.serviceUnderTest.bid(game.getId(), startingPlayer.getId(), 3);
    }

    @Test
    @Transactional
    public void canOptOutOfBidding() throws Exception {
        final Game game = this.serviceUnderTest.createGame(HOST_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getId(), SECOND_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getId(), THIRD_PLAYER_NAME);
        this.serviceUnderTest.startGame(game.getId());

        Player startingPlayer = game.getRounds().get(0).getStartingPlayer();
        Player secondPlayer = game.getPlayers().get((game.getPlayers().indexOf(startingPlayer) + 1) % 3);
        Player thirdPlayer = game.getPlayers().get((game.getPlayers().indexOf(startingPlayer) + 2) % 3);
        this.serviceUnderTest.layCard(game.getId(), startingPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), secondPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), thirdPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.bid(game.getId(), startingPlayer.getId(), 1);
        this.serviceUnderTest.optOutOfBidding(game.getId(), secondPlayer.getId());
    }

    @Test(expected = GameNotStartedException.class)
    @Transactional
    public void cannotOptOutOfBiddingAsGameNotYetStarted() throws Exception {
        final Game game = this.serviceUnderTest.createGame(HOST_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getId(), SECOND_PLAYER_NAME);

        Player startingPlayer = game.getPlayers().get(0);
        this.serviceUnderTest.optOutOfBidding(game.getId(), startingPlayer.getId());
    }

    @Test(expected = PlayerActingOutOfTurnException.class)
    @Transactional
    public void cannotOptOutOfBiddingAsActingOutOfTurn() throws Exception {
        final Game game = this.serviceUnderTest.createGame(HOST_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getId(), SECOND_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getId(), THIRD_PLAYER_NAME);
        this.serviceUnderTest.startGame(game.getId());

        Player startingPlayer = game.getRounds().get(0).getStartingPlayer();
        Player secondPlayer = game.getPlayers().get((game.getPlayers().indexOf(startingPlayer) + 1) % 3);
        Player thirdPlayer = game.getPlayers().get((game.getPlayers().indexOf(startingPlayer) + 2) % 3);
        this.serviceUnderTest.layCard(game.getId(), startingPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), secondPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), thirdPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.bid(game.getId(), startingPlayer.getId(), 1);
        this.serviceUnderTest.optOutOfBidding(game.getId(), thirdPlayer.getId());
    }
}
