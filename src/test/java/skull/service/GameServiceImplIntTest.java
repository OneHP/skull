package skull.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import skull.SkullApplication;
import skull.domain.Game;
import skull.service.exception.InsufficientPlayersException;

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
}
