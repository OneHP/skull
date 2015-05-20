package skull.controller;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import skull.domain.Card;
import skull.domain.Hand;
import skull.domain.Player;
import skull.domain.PlayerState;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@RunWith(BlockJUnit4ClassRunner.class)
public class PlayerStateViewUnitTest {

    private static final Long PLAYER_ID = 737L;
    private static final Long PLAYER_STATE_ID = 8584L;
    private static final Long HAND_ID = 3934L;
    private static final String PLAYER_NAME = "Thomas";


    @Test
    public void canConvertFromPlayerState(){

        Hand hand = new Hand();
        hand.setId(HAND_ID);
        hand.setRoses(2);
        hand.setSkulls(0);

        Player player = new Player();
        player.setId(PLAYER_ID);
        player.setName(PLAYER_NAME);
        player.setPoints(0);
        player.setRoses(3);
        player.setSkulls(1);

        PlayerState input = new PlayerState();
        input.setId(PLAYER_STATE_ID);
        input.setBid(2);
        input.setCardsOnTable(Lists.newArrayList(Card.SKULL, Card.ROSE));
        input.setNumberOfRevealedCards(1);
        input.setOutOfBidding(true);
        input.setHand(hand);
        input.setPlayer(player);


        PlayerView expectedPlayerView = new PlayerView();
        expectedPlayerView.setId(PLAYER_ID);
        expectedPlayerView.setName(PLAYER_NAME);

        PlayerStateView expected = new PlayerStateView();
        expected.setBid(2);
        expected.setCardsInHand(2);
        expected.setCardsOnTable(Lists.newArrayList(Card.UNKNOWN, Card.ROSE));
        expected.setOutOfBidding(true);
        expected.setPlayer(expectedPlayerView);


        assertThat(PlayerStateView.fromPlayerState(input), is(expected));
    }
}
