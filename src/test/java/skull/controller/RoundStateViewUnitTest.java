package skull.controller;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import skull.controller.action.FlipOtherPlayersCardView;
import skull.domain.*;
import skull.domain.action.Action;
import skull.domain.action.FlipOtherPlayersCard;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@RunWith(BlockJUnit4ClassRunner.class)
public class RoundStateViewUnitTest {

    private static final Long PLAYER_1_ID = 737L;
    private static final Long PLAYER_1_STATE_ID = 8584L;
    private static final Long PLAYER_1_HAND_ID = 3934L;
    private static final String PLAYER_1_NAME = "Thomas";

    private static final Long PLAYER_2_ID = 932L;
    private static final Long PLAYER_2_STATE_ID = 7777L;
    private static final Long PLAYER_2_HAND_ID = 2340L;
    private static final String PLAYER_2_NAME = "Mike";

    private static final Long ROUND_STATE_ID = 76630L;
    private static final Long ACTION_ID = 11833L;

    @Test
    public void canConvertFromRoundState(){

        //Player 1 Domain Objects
        Hand player1Hand = new Hand();
        player1Hand.setId(PLAYER_1_HAND_ID);
        player1Hand.setRoses(2);
        player1Hand.setSkulls(0);

        Player player1 = new Player();
        player1.setId(PLAYER_1_ID);
        player1.setName(PLAYER_1_NAME);
        player1.setPoints(0);
        player1.setRoses(3);
        player1.setSkulls(1);

        PlayerState player1State = new PlayerState();
        player1State.setId(PLAYER_1_STATE_ID);
        player1State.setBid(2);
        player1State.setCardsOnTable(Lists.newArrayList(Card.SKULL, Card.ROSE));
        player1State.setNumberOfRevealedCards(1);
        player1State.setOutOfBidding(true);
        player1State.setHand(player1Hand);
        player1State.setPlayer(player1);

        //Player 2 Domain Objects
        Hand player2Hand = new Hand();
        player2Hand.setId(PLAYER_2_HAND_ID);
        player2Hand.setRoses(1);
        player2Hand.setSkulls(1);

        Player player2 = new Player();
        player2.setId(PLAYER_2_ID);
        player2.setName(PLAYER_2_NAME);
        player2.setPoints(0);
        player2.setRoses(3);
        player2.setSkulls(1);

        PlayerState player2State = new PlayerState();
        player2State.setId(PLAYER_2_STATE_ID);
        player2State.setBid(4);
        player2State.setCardsOnTable(Lists.newArrayList(Card.ROSE, Card.ROSE));
        player2State.setNumberOfRevealedCards(2);
        player2State.setOutOfBidding(false);
        player2State.setHand(player2Hand);
        player2State.setPlayer(player2);

        //Round State Domain Objects
        Action action = FlipOtherPlayersCard.create(player2,player1,1);
        action.setId(ACTION_ID);

        RoundState roundState = new RoundState();
        roundState.setId(ROUND_STATE_ID);
        roundState.setMaxBid(4);
        roundState.setPlayerToAct(player2);
        roundState.setRoundPhase(RoundPhase.RESOLUTION);
        roundState.setPreviousAction(action);
        roundState.setPlayerStates(Lists.newArrayList(player1State,player2State));

        //Player 1 View Objects
        PlayerView player1View = new PlayerView();
        player1View.setId(PLAYER_1_ID);
        player1View.setName(PLAYER_1_NAME);

        PlayerStateView player1StateView = new PlayerStateView();
        player1StateView.setBid(2);
        player1StateView.setCardsInHand(2);
        player1StateView.setCardsOnTable(Lists.newArrayList(Card.UNKNOWN, Card.ROSE));
        player1StateView.setOutOfBidding(true);
        player1StateView.setPlayer(player1View);

        //Player 2 View Objects
        PlayerView player2View = new PlayerView();
        player2View.setId(PLAYER_2_ID);
        player2View.setName(PLAYER_2_NAME);

        PlayerStateView player2StateView = new PlayerStateView();
        player2StateView.setBid(4);
        player2StateView.setCardsInHand(2);
        player2StateView.setCardsOnTable(Lists.newArrayList(Card.ROSE, Card.ROSE));
        player2StateView.setOutOfBidding(false);
        player2StateView.setPlayer(player2View);

        //Round State View Objects
        FlipOtherPlayersCardView actionView = new FlipOtherPlayersCardView();
        actionView.setAction("flip_other_players_card");
        actionView.setIndex(1);
        actionView.setTargetPlayer(player1View);
        actionView.setPlayer(player2View);

        RoundStateView player1ExpectedView = new RoundStateView();
        player1ExpectedView.setHand(Lists.newArrayList(Card.ROSE, Card.ROSE));
        player1ExpectedView.setMaxBid(4);
        player1ExpectedView.setPlayedCards(Lists.newArrayList(Card.SKULL, Card.ROSE));
        player1ExpectedView.setPlayerStates(Lists.newArrayList(player1StateView, player2StateView));
        player1ExpectedView.setPlayerToAct(player2View);
        player1ExpectedView.setPreviousAction(actionView);
        player1ExpectedView.setRoundPhase(RoundPhase.RESOLUTION);

        RoundStateView player2ExpectedView = new RoundStateView();
        player2ExpectedView.setHand(Lists.newArrayList(Card.SKULL, Card.ROSE));
        player2ExpectedView.setMaxBid(4);
        player2ExpectedView.setPlayedCards(Lists.newArrayList(Card.ROSE, Card.ROSE));
        player2ExpectedView.setPlayerStates(Lists.newArrayList(player1StateView, player2StateView));
        player2ExpectedView.setPlayerToAct(player2View);
        player2ExpectedView.setPreviousAction(actionView);
        player2ExpectedView.setRoundPhase(RoundPhase.RESOLUTION);

        assertThat(RoundStateView.fromRoundState(roundState, PLAYER_1_ID), is(player1ExpectedView));
        assertThat(RoundStateView.fromRoundState(roundState,PLAYER_2_ID),is(player2ExpectedView));
    }
}
