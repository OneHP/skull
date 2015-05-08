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
import skull.service.exception.CardNotInHandException;
import skull.service.exception.GameNotStartedException;
import skull.service.exception.InsufficientPlayersException;
import skull.service.exception.PlayerActingOutOfTurnException;

import javax.transaction.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkullApplication.class)
@WebAppConfiguration
public class GameServiceImplIntTest {

    private static String HOST_PLAYER_NAME = "Thomas";
    private static String SECOND_PLAYER_NAME = "Mike";

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
}
