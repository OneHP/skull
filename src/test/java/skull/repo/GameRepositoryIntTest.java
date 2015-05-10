package skull.repo;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import skull.SkullApplication;
import skull.domain.*;

import javax.transaction.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkullApplication.class)
@WebAppConfiguration
public class GameRepositoryIntTest {

    @Autowired
    private GameRepository repositoryUnderTest;

    @Test
    @Transactional
    public void canSaveAndRetreiveGame(){

        Game game = new Game();
        game.setKey("ASDFASDF7");
        game.setStarted(true);

        Player player1 = Player.create("Thomas");
        Player player2 = Player.create("Mike");

        Round round1 = new Round();
        round1.setStartingPlayer(player2);

        RoundState round1State1 = new RoundState();
        round1State1.setMaxBid(0);
        round1State1.setRoundPhase(RoundPhase.LAYING);
        round1State1.setPlayerToAct(player2);

        PlayerState round1State1Player1State = PlayerState.create(player1);
        PlayerState round1State1Player2State = PlayerState.create(player2);

        round1State1.setPlayerStates(Lists.newArrayList(round1State1Player1State, round1State1Player2State));
        round1.setRoundStates(Lists.newArrayList(round1State1));
        game.setRounds(Lists.newArrayList(round1));
        game.setPlayers(Lists.newArrayList(player1, player2));

        this.repositoryUnderTest.save(game);

        Game savedGame = this.repositoryUnderTest.findAll().iterator().next();
        assertThat(savedGame, is(new PersistableDomainObjectMatcher(game)));

    }
}
