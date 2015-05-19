package skull.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import skull.SkullApplication;
import skull.domain.Card;
import skull.domain.Game;
import skull.domain.PersistableDomainObjectMatcher;
import skull.domain.Player;
import skull.service.exception.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import javax.transaction.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkullApplication.class)
@WebAppConfiguration
@ActiveProfiles("testing")
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
        this.serviceUnderTest.addPlayer(game.getKey(), SECOND_PLAYER_NAME);
    }

    @Test
    public void canStartGame() throws Exception {
        final Game game = this.serviceUnderTest.createGame(HOST_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getKey(), SECOND_PLAYER_NAME);
        this.serviceUnderTest.startGame(game.getId());
    }

    @Test(expected = InsufficientPlayersException.class)
    public void failsToStartGameWithOnePlayer() throws Exception {
        final Game game = this.serviceUnderTest.createGame(HOST_PLAYER_NAME);
        this.serviceUnderTest.startGame(game.getId());
    }

    @Test
    @Transactional
    public void canLayCard() throws Exception {
        final Game game = this.serviceUnderTest.createGame(HOST_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getKey(), SECOND_PLAYER_NAME);
        this.serviceUnderTest.startGame(game.getId());

        Player startingPlayer = game.getPlayers().get(0);
        this.serviceUnderTest.layCard(game.getId(), startingPlayer.getId(), Card.SKULL);
    }

    @Test(expected = GameNotStartedException.class)
    @Transactional
    public void cannotLayCardAsGameNotYetStarted() throws Exception {
        final Game game = this.serviceUnderTest.createGame(HOST_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getKey(), SECOND_PLAYER_NAME);

        Player startingPlayer = game.getPlayers().get(0);
        this.serviceUnderTest.layCard(game.getId(), startingPlayer.getId(), Card.SKULL);
    }

    @Test(expected = PlayerActingOutOfTurnException.class)
    @Transactional
    public void cannotLayCardAsActingOutOfTurn() throws Exception {
        final Game game = this.serviceUnderTest.createGame(HOST_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getKey(), SECOND_PLAYER_NAME);
        this.serviceUnderTest.startGame(game.getId());

        Player otherPlayer = game.getPlayers().get(1);
        this.serviceUnderTest.layCard(game.getId(), otherPlayer.getId(), Card.SKULL);
    }

    @Test(expected = CardNotInHandException.class)
    @Transactional
    public void cannotLayCardAsCardNotInHand() throws Exception {
        final Game game = this.serviceUnderTest.createGame(HOST_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getKey(), SECOND_PLAYER_NAME);
        this.serviceUnderTest.startGame(game.getId());

        Player startingPlayer = game.getPlayers().get(0);
        Player otherPlayer = game.getPlayers().get(1);
        this.serviceUnderTest.layCard(game.getId(), startingPlayer.getId(), Card.SKULL);
        this.serviceUnderTest.layCard(game.getId(), otherPlayer.getId(), Card.SKULL);
        this.serviceUnderTest.layCard(game.getId(), startingPlayer.getId(), Card.SKULL);
    }

    @Test(expected = IncorrectRoundPhaseException.class)
    @Transactional
    public void cannotLayCardAsWrongPhase() throws Exception {
        final Game game = this.serviceUnderTest.createGame(HOST_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getKey(), SECOND_PLAYER_NAME);
        this.serviceUnderTest.startGame(game.getId());

        Player startingPlayer = game.getPlayers().get(0);
        Player otherPlayer = game.getPlayers().get(1);
        this.serviceUnderTest.layCard(game.getId(), startingPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), otherPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.bid(game.getId(), startingPlayer.getId(), 1);
        this.serviceUnderTest.layCard(game.getId(), otherPlayer.getId(), Card.ROSE);
    }

    @Test
    @Transactional
    public void canBid() throws Exception {
        final Game game = this.serviceUnderTest.createGame(HOST_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getKey(), SECOND_PLAYER_NAME);
        this.serviceUnderTest.startGame(game.getId());

        Player startingPlayer = game.getPlayers().get(0);
        Player otherPlayer = game.getPlayers().get(1);
        this.serviceUnderTest.layCard(game.getId(), startingPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), otherPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.bid(game.getId(), startingPlayer.getId(), 1);
        this.serviceUnderTest.bid(game.getId(), otherPlayer.getId(), 2);
    }

    @Test(expected = GameNotStartedException.class)
    @Transactional
    public void cannotBidAsGameNotYetStarted() throws Exception {
        final Game game = this.serviceUnderTest.createGame(HOST_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getKey(), SECOND_PLAYER_NAME);

        Player startingPlayer = game.getPlayers().get(0);
        this.serviceUnderTest.bid(game.getId(), startingPlayer.getId(), 1);
    }

    @Test(expected = PlayerActingOutOfTurnException.class)
    @Transactional
    public void cannotBidAsActingOutOfTurn() throws Exception {
        final Game game = this.serviceUnderTest.createGame(HOST_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getKey(), SECOND_PLAYER_NAME);
        this.serviceUnderTest.startGame(game.getId());

        Player startingPlayer = game.getPlayers().get(0);
        Player otherPlayer = game.getPlayers().get(1);
        this.serviceUnderTest.layCard(game.getId(), startingPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), otherPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.bid(game.getId(), otherPlayer.getId(), 1);
    }

    @Test(expected = BidTooLowException.class)
    @Transactional
    public void cannotBidAsBidTooLow() throws Exception {
        final Game game = this.serviceUnderTest.createGame(HOST_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getKey(), SECOND_PLAYER_NAME);
        this.serviceUnderTest.startGame(game.getId());

        Player startingPlayer = game.getPlayers().get(0);
        Player otherPlayer = game.getPlayers().get(1);
        this.serviceUnderTest.layCard(game.getId(), startingPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), otherPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.bid(game.getId(), startingPlayer.getId(), 1);
        this.serviceUnderTest.bid(game.getId(), otherPlayer.getId(), 1);
    }

    @Test(expected = IncorrectRoundPhaseException.class)
    @Transactional
    public void cannotBidAsWrongPhase() throws Exception {
        final Game game = this.serviceUnderTest.createGame(HOST_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getKey(), SECOND_PLAYER_NAME);
        this.serviceUnderTest.startGame(game.getId());

        Player startingPlayer = game.getPlayers().get(0);
        Player otherPlayer = game.getPlayers().get(1);
        this.serviceUnderTest.layCard(game.getId(), startingPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), otherPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.bid(game.getId(), startingPlayer.getId(), 1);
        this.serviceUnderTest.optOutOfBidding(game.getId(), otherPlayer.getId());
        this.serviceUnderTest.bid(game.getId(), startingPlayer.getId(), 2);
    }

    @Test
    @Transactional
    public void canOptOutOfBidding() throws Exception {
        final Game game = this.serviceUnderTest.createGame(HOST_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getKey(), SECOND_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getKey(), THIRD_PLAYER_NAME);
        this.serviceUnderTest.startGame(game.getId());

        Player startingPlayer = game.getPlayers().get(0);
        Player secondPlayer = game.getPlayers().get(1);
        Player thirdPlayer = game.getPlayers().get(2);
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
        this.serviceUnderTest.addPlayer(game.getKey(), SECOND_PLAYER_NAME);

        Player startingPlayer = game.getPlayers().get(0);
        this.serviceUnderTest.optOutOfBidding(game.getId(), startingPlayer.getId());
    }

    @Test(expected = PlayerActingOutOfTurnException.class)
    @Transactional
    public void cannotOptOutOfBiddingAsActingOutOfTurn() throws Exception {
        final Game game = this.serviceUnderTest.createGame(HOST_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getKey(), SECOND_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getKey(), THIRD_PLAYER_NAME);
        this.serviceUnderTest.startGame(game.getId());

        Player startingPlayer = game.getPlayers().get(0);
        Player secondPlayer = game.getPlayers().get(1);
        Player thirdPlayer = game.getPlayers().get(2);
        this.serviceUnderTest.layCard(game.getId(), startingPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), secondPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), thirdPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.bid(game.getId(), startingPlayer.getId(), 1);
        this.serviceUnderTest.optOutOfBidding(game.getId(), thirdPlayer.getId());
    }

    @Test
    @Transactional
    public void canOptOutOfBiddingAndPlayContinues() throws Exception {
        final Game game = this.serviceUnderTest.createGame(HOST_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getKey(), SECOND_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getKey(), THIRD_PLAYER_NAME);
        this.serviceUnderTest.startGame(game.getId());

        Player startingPlayer = game.getPlayers().get(0);
        Player secondPlayer = game.getPlayers().get(1);
        Player thirdPlayer = game.getPlayers().get(2);

        this.serviceUnderTest.layCard(game.getId(), startingPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), secondPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), thirdPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), startingPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), secondPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), thirdPlayer.getId(), Card.ROSE);

        this.serviceUnderTest.bid(game.getId(), startingPlayer.getId(), 1);
        this.serviceUnderTest.bid(game.getId(), secondPlayer.getId(), 2);
        this.serviceUnderTest.optOutOfBidding(game.getId(), thirdPlayer.getId());
        this.serviceUnderTest.bid(game.getId(), startingPlayer.getId(), 3);
        this.serviceUnderTest.bid(game.getId(), secondPlayer.getId(), 4);
        this.serviceUnderTest.bid(game.getId(), startingPlayer.getId(), 5);
        this.serviceUnderTest.bid(game.getId(), secondPlayer.getId(), 6);
    }

    @Test(expected = IncorrectRoundPhaseException.class)
    @Transactional
    public void cannotOptOutOfBiddingAsWrongPhase() throws Exception {
        final Game game = this.serviceUnderTest.createGame(HOST_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getKey(), SECOND_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getKey(), THIRD_PLAYER_NAME);
        this.serviceUnderTest.startGame(game.getId());

        Player startingPlayer = game.getPlayers().get(0);
        Player secondPlayer = game.getPlayers().get(1);
        Player thirdPlayer = game.getPlayers().get(2);
        this.serviceUnderTest.layCard(game.getId(), startingPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), secondPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), thirdPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.bid(game.getId(), startingPlayer.getId(), 1);
        this.serviceUnderTest.optOutOfBidding(game.getId(), secondPlayer.getId());
        this.serviceUnderTest.optOutOfBidding(game.getId(), thirdPlayer.getId());
        this.serviceUnderTest.bid(game.getId(), startingPlayer.getId(), 2);
    }

    @Test
    @Transactional
    public void canFlipOwnCards() throws Exception {
        final Game game = this.serviceUnderTest.createGame(HOST_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getKey(), SECOND_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getKey(), THIRD_PLAYER_NAME);
        this.serviceUnderTest.startGame(game.getId());

        Player startingPlayer = game.getPlayers().get(0);
        Player secondPlayer = game.getPlayers().get(1);
        Player thirdPlayer = game.getPlayers().get(2);

        this.serviceUnderTest.layCard(game.getId(), startingPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), secondPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), thirdPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), startingPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), secondPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), thirdPlayer.getId(), Card.ROSE);

        this.serviceUnderTest.bid(game.getId(), startingPlayer.getId(), 1);
        this.serviceUnderTest.bid(game.getId(), secondPlayer.getId(), 2);
        this.serviceUnderTest.optOutOfBidding(game.getId(), thirdPlayer.getId());
        this.serviceUnderTest.bid(game.getId(), startingPlayer.getId(), 4);
        this.serviceUnderTest.bid(game.getId(), secondPlayer.getId(), 5);
        this.serviceUnderTest.optOutOfBidding(game.getId(), startingPlayer.getId());
        this.serviceUnderTest.flipOwnCards(game.getId(), secondPlayer.getId());
    }

    @Test(expected = GameNotStartedException.class)
    @Transactional
    public void cannotFlipOwnCardsAsGameNotYetStarted() throws Exception {
        final Game game = this.serviceUnderTest.createGame(HOST_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getKey(), SECOND_PLAYER_NAME);

        Player startingPlayer = game.getPlayers().get(0);
        this.serviceUnderTest.flipOwnCards(game.getId(), startingPlayer.getId());
    }

    @Test(expected = PlayerActingOutOfTurnException.class)
    @Transactional
    public void cannotFlipOwnCardsAsActingOutOfTurn() throws Exception {
        final Game game = this.serviceUnderTest.createGame(HOST_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getKey(), SECOND_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getKey(), THIRD_PLAYER_NAME);
        this.serviceUnderTest.startGame(game.getId());

        Player startingPlayer = game.getPlayers().get(0);
        Player secondPlayer = game.getPlayers().get(1);
        Player thirdPlayer = game.getPlayers().get(2);

        this.serviceUnderTest.layCard(game.getId(), startingPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), secondPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), thirdPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), startingPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), secondPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), thirdPlayer.getId(), Card.ROSE);

        this.serviceUnderTest.bid(game.getId(), startingPlayer.getId(), 1);
        this.serviceUnderTest.bid(game.getId(), secondPlayer.getId(), 2);
        this.serviceUnderTest.optOutOfBidding(game.getId(), thirdPlayer.getId());
        this.serviceUnderTest.bid(game.getId(), startingPlayer.getId(), 4);
        this.serviceUnderTest.bid(game.getId(), secondPlayer.getId(), 5);
        this.serviceUnderTest.optOutOfBidding(game.getId(), startingPlayer.getId());
        this.serviceUnderTest.flipOwnCards(game.getId(), thirdPlayer.getId());
    }

    @Test(expected = IncorrectRoundPhaseException.class)
    @Transactional
    public void cannotFlipOwnCardsAsWrongPhase() throws Exception {
        final Game game = this.serviceUnderTest.createGame(HOST_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getKey(), SECOND_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getKey(), THIRD_PLAYER_NAME);
        this.serviceUnderTest.startGame(game.getId());

        Player startingPlayer = game.getPlayers().get(0);
        Player secondPlayer = game.getPlayers().get(1);
        Player thirdPlayer = game.getPlayers().get(2);

        this.serviceUnderTest.layCard(game.getId(), startingPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), secondPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), thirdPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), startingPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), secondPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), thirdPlayer.getId(), Card.ROSE);

        this.serviceUnderTest.bid(game.getId(), startingPlayer.getId(), 1);
        this.serviceUnderTest.bid(game.getId(), secondPlayer.getId(), 2);
        this.serviceUnderTest.optOutOfBidding(game.getId(), thirdPlayer.getId());
        this.serviceUnderTest.bid(game.getId(), startingPlayer.getId(), 4);
        this.serviceUnderTest.bid(game.getId(), secondPlayer.getId(), 5);
        this.serviceUnderTest.flipOwnCards(game.getId(), startingPlayer.getId());
    }

    @Test(expected = AlreadyRevealedCardException.class)
    @Transactional
    public void cannotFlipOwnCardsAsAlreadyFlipped() throws Exception {
        final Game game = this.serviceUnderTest.createGame(HOST_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getKey(), SECOND_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getKey(), THIRD_PLAYER_NAME);
        this.serviceUnderTest.startGame(game.getId());

        Player startingPlayer = game.getPlayers().get(0);
        Player secondPlayer = game.getPlayers().get(1);
        Player thirdPlayer = game.getPlayers().get(2);

        this.serviceUnderTest.layCard(game.getId(), startingPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), secondPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), thirdPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), startingPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), secondPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), thirdPlayer.getId(), Card.ROSE);

        this.serviceUnderTest.bid(game.getId(), startingPlayer.getId(), 1);
        this.serviceUnderTest.bid(game.getId(), secondPlayer.getId(), 2);
        this.serviceUnderTest.optOutOfBidding(game.getId(), thirdPlayer.getId());
        this.serviceUnderTest.bid(game.getId(), startingPlayer.getId(), 4);
        this.serviceUnderTest.bid(game.getId(), secondPlayer.getId(), 5);
        this.serviceUnderTest.optOutOfBidding(game.getId(), startingPlayer.getId());
        this.serviceUnderTest.flipOwnCards(game.getId(), secondPlayer.getId());
        this.serviceUnderTest.flipOwnCards(game.getId(), secondPlayer.getId());
    }

    @Test
    @Transactional
    public void canFlipOtherPlayerCard() throws Exception {
        final Game game = this.serviceUnderTest.createGame(HOST_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getKey(), SECOND_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getKey(), THIRD_PLAYER_NAME);
        this.serviceUnderTest.startGame(game.getId());

        Player startingPlayer = game.getPlayers().get(0);
        Player secondPlayer = game.getPlayers().get(1);
        Player thirdPlayer = game.getPlayers().get(2);

        this.serviceUnderTest.layCard(game.getId(), startingPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), secondPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), thirdPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), startingPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), secondPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), thirdPlayer.getId(), Card.ROSE);

        this.serviceUnderTest.bid(game.getId(), startingPlayer.getId(), 1);
        this.serviceUnderTest.bid(game.getId(), secondPlayer.getId(), 2);
        this.serviceUnderTest.optOutOfBidding(game.getId(), thirdPlayer.getId());
        this.serviceUnderTest.bid(game.getId(), startingPlayer.getId(), 5);
        this.serviceUnderTest.bid(game.getId(), secondPlayer.getId(), 6);
        this.serviceUnderTest.optOutOfBidding(game.getId(), startingPlayer.getId());
        this.serviceUnderTest.flipOwnCards(game.getId(), secondPlayer.getId());
        this.serviceUnderTest.flipOtherPlayerCard(game.getId(), secondPlayer.getId(), thirdPlayer.getId(), 1);
        this.serviceUnderTest.flipOtherPlayerCard(game.getId(), secondPlayer.getId(), startingPlayer.getId(), 1);
        this.serviceUnderTest.flipOtherPlayerCard(game.getId(), secondPlayer.getId(), thirdPlayer.getId(), 0);
    }

    @Test(expected = GameNotStartedException.class)
    @Transactional
    public void cannotFlipOtherPlayerCardAsGameNotYetStarted() throws Exception {
        final Game game = this.serviceUnderTest.createGame(HOST_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getKey(), SECOND_PLAYER_NAME);

        Player startingPlayer = game.getPlayers().get(0);
        Player secondPlayer = game.getPlayers().get(1);
        this.serviceUnderTest.flipOtherPlayerCard(game.getId(), startingPlayer.getId(), secondPlayer.getId(), 0);
    }

    @Test(expected = PlayerActingOutOfTurnException.class)
    @Transactional
    public void cannotFlipOtherPlayerCardAsActingOutOfTurn() throws Exception {
        final Game game = this.serviceUnderTest.createGame(HOST_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getKey(), SECOND_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getKey(), THIRD_PLAYER_NAME);
        this.serviceUnderTest.startGame(game.getId());

        Player startingPlayer = game.getPlayers().get(0);
        Player secondPlayer = game.getPlayers().get(1);
        Player thirdPlayer = game.getPlayers().get(2);

        this.serviceUnderTest.layCard(game.getId(), startingPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), secondPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), thirdPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), startingPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), secondPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), thirdPlayer.getId(), Card.ROSE);

        this.serviceUnderTest.bid(game.getId(), startingPlayer.getId(), 1);
        this.serviceUnderTest.bid(game.getId(), secondPlayer.getId(), 2);
        this.serviceUnderTest.optOutOfBidding(game.getId(), thirdPlayer.getId());
        this.serviceUnderTest.bid(game.getId(), startingPlayer.getId(), 4);
        this.serviceUnderTest.bid(game.getId(), secondPlayer.getId(), 5);
        this.serviceUnderTest.optOutOfBidding(game.getId(), startingPlayer.getId());
        this.serviceUnderTest.flipOtherPlayerCard(game.getId(), thirdPlayer.getId(), startingPlayer.getId(), 1);
    }

    @Test(expected = IncorrectRoundPhaseException.class)
    @Transactional
    public void cannotFlipOtherPlayerCardAsWrongPhase() throws Exception {
        final Game game = this.serviceUnderTest.createGame(HOST_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getKey(), SECOND_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getKey(), THIRD_PLAYER_NAME);
        this.serviceUnderTest.startGame(game.getId());

        Player startingPlayer = game.getPlayers().get(0);
        Player secondPlayer = game.getPlayers().get(1);
        Player thirdPlayer = game.getPlayers().get(2);

        this.serviceUnderTest.layCard(game.getId(), startingPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), secondPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), thirdPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), startingPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), secondPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), thirdPlayer.getId(), Card.ROSE);

        this.serviceUnderTest.bid(game.getId(), startingPlayer.getId(), 1);
        this.serviceUnderTest.bid(game.getId(), secondPlayer.getId(), 2);
        this.serviceUnderTest.optOutOfBidding(game.getId(), thirdPlayer.getId());
        this.serviceUnderTest.bid(game.getId(), startingPlayer.getId(), 4);
        this.serviceUnderTest.bid(game.getId(), secondPlayer.getId(), 5);
        this.serviceUnderTest.flipOtherPlayerCard(game.getId(), startingPlayer.getId(), secondPlayer.getId(), 1);
    }

    @Test(expected = NotYetRevealedOwnCardsException.class)
    @Transactional
    public void cannotFlipOtherPlayerCardAsNotFlippedOwnCards() throws Exception {
        final Game game = this.serviceUnderTest.createGame(HOST_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getKey(), SECOND_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getKey(), THIRD_PLAYER_NAME);
        this.serviceUnderTest.startGame(game.getId());

        Player startingPlayer = game.getPlayers().get(0);
        Player secondPlayer = game.getPlayers().get(1);
        Player thirdPlayer = game.getPlayers().get(2);

        this.serviceUnderTest.layCard(game.getId(), startingPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), secondPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), thirdPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), startingPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), secondPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), thirdPlayer.getId(), Card.ROSE);

        this.serviceUnderTest.bid(game.getId(), startingPlayer.getId(), 1);
        this.serviceUnderTest.bid(game.getId(), secondPlayer.getId(), 2);
        this.serviceUnderTest.optOutOfBidding(game.getId(), thirdPlayer.getId());
        this.serviceUnderTest.bid(game.getId(), startingPlayer.getId(), 4);
        this.serviceUnderTest.bid(game.getId(), secondPlayer.getId(), 5);
        this.serviceUnderTest.optOutOfBidding(game.getId(), startingPlayer.getId());
        this.serviceUnderTest.flipOtherPlayerCard(game.getId(), secondPlayer.getId(), thirdPlayer.getId(), 1);
    }

    @Test(expected = AlreadyRevealedCardException.class)
     @Transactional
     public void cannotFlipOtherPlayerCardAsAlreadyFlipped() throws Exception {
        final Game game = this.serviceUnderTest.createGame(HOST_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getKey(), SECOND_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getKey(), THIRD_PLAYER_NAME);
        this.serviceUnderTest.startGame(game.getId());

        Player startingPlayer = game.getPlayers().get(0);
        Player secondPlayer = game.getPlayers().get(1);
        Player thirdPlayer = game.getPlayers().get(2);

        this.serviceUnderTest.layCard(game.getId(), startingPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), secondPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), thirdPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), startingPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), secondPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), thirdPlayer.getId(), Card.ROSE);

        this.serviceUnderTest.bid(game.getId(), startingPlayer.getId(), 1);
        this.serviceUnderTest.bid(game.getId(), secondPlayer.getId(), 2);
        this.serviceUnderTest.optOutOfBidding(game.getId(), thirdPlayer.getId());
        this.serviceUnderTest.bid(game.getId(), startingPlayer.getId(), 4);
        this.serviceUnderTest.bid(game.getId(), secondPlayer.getId(), 5);
        this.serviceUnderTest.optOutOfBidding(game.getId(), startingPlayer.getId());
        this.serviceUnderTest.flipOwnCards(game.getId(), secondPlayer.getId());
        this.serviceUnderTest.flipOtherPlayerCard(game.getId(), secondPlayer.getId(), thirdPlayer.getId(), 1);
        this.serviceUnderTest.flipOtherPlayerCard(game.getId(), secondPlayer.getId(), thirdPlayer.getId(), 1);
    }

    @Test
    @Transactional
    public void canLoseGameByLosingSeveralRounds() throws Exception {
        final Game game = this.serviceUnderTest.createGame(HOST_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getKey(), SECOND_PLAYER_NAME);
        this.serviceUnderTest.startGame(game.getId());

        Player startingPlayer = game.getPlayers().get(0);
        Player secondPlayer = game.getPlayers().get(1);

        this.serviceUnderTest.layCard(game.getId(), startingPlayer.getId(), Card.SKULL);
        this.serviceUnderTest.layCard(game.getId(), secondPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.bid(game.getId(), startingPlayer.getId(), 1);
        this.serviceUnderTest.bid(game.getId(), secondPlayer.getId(), 2);
        this.serviceUnderTest.optOutOfBidding(game.getId(), startingPlayer.getId());
        this.serviceUnderTest.flipOwnCards(game.getId(), secondPlayer.getId());
        this.serviceUnderTest.flipOtherPlayerCard(game.getId(), secondPlayer.getId(), startingPlayer.getId(), 0);

        this.serviceUnderTest.layCard(game.getId(), secondPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), startingPlayer.getId(), Card.SKULL);
        this.serviceUnderTest.bid(game.getId(), secondPlayer.getId(), 2);
        this.serviceUnderTest.optOutOfBidding(game.getId(), startingPlayer.getId());
        this.serviceUnderTest.flipOwnCards(game.getId(), secondPlayer.getId());
        this.serviceUnderTest.flipOtherPlayerCard(game.getId(), secondPlayer.getId(), startingPlayer.getId(), 0);

        this.serviceUnderTest.layCard(game.getId(), secondPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), startingPlayer.getId(), Card.SKULL);
        this.serviceUnderTest.bid(game.getId(), secondPlayer.getId(), 2);
        this.serviceUnderTest.optOutOfBidding(game.getId(), startingPlayer.getId());
        this.serviceUnderTest.flipOwnCards(game.getId(), secondPlayer.getId());
        this.serviceUnderTest.flipOtherPlayerCard(game.getId(), secondPlayer.getId(), startingPlayer.getId(), 0);

        this.serviceUnderTest.layCard(game.getId(), secondPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), startingPlayer.getId(), Card.SKULL);
        this.serviceUnderTest.bid(game.getId(), secondPlayer.getId(), 2);
        this.serviceUnderTest.optOutOfBidding(game.getId(), startingPlayer.getId());
        this.serviceUnderTest.flipOwnCards(game.getId(), secondPlayer.getId());
        this.serviceUnderTest.flipOtherPlayerCard(game.getId(), secondPlayer.getId(), startingPlayer.getId(), 0);

        assertThat(game.getStarted(), is(false));
        assertThat(game.getWinner(), is(new PersistableDomainObjectMatcher(startingPlayer)));
    }

    @Test
    @Transactional
    public void canWinGameByWinningSeveralRounds() throws Exception {
        final Game game = this.serviceUnderTest.createGame(HOST_PLAYER_NAME);
        this.serviceUnderTest.addPlayer(game.getKey(), SECOND_PLAYER_NAME);
        this.serviceUnderTest.startGame(game.getId());

        Player startingPlayer = game.getPlayers().get(0);
        Player secondPlayer = game.getPlayers().get(1);

        this.serviceUnderTest.layCard(game.getId(), startingPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), secondPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.bid(game.getId(), startingPlayer.getId(), 2);
        this.serviceUnderTest.optOutOfBidding(game.getId(), secondPlayer.getId());
        this.serviceUnderTest.flipOwnCards(game.getId(), startingPlayer.getId());
        this.serviceUnderTest.flipOtherPlayerCard(game.getId(), startingPlayer.getId(), secondPlayer.getId(), 0);

        this.serviceUnderTest.layCard(game.getId(), startingPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.layCard(game.getId(), secondPlayer.getId(), Card.ROSE);
        this.serviceUnderTest.bid(game.getId(), startingPlayer.getId(), 2);
        this.serviceUnderTest.optOutOfBidding(game.getId(), secondPlayer.getId());
        this.serviceUnderTest.flipOwnCards(game.getId(), startingPlayer.getId());
        this.serviceUnderTest.flipOtherPlayerCard(game.getId(), startingPlayer.getId(), secondPlayer.getId(), 0);

        assertThat(game.getStarted(), is(false));
        assertThat(game.getWinner(),is(new PersistableDomainObjectMatcher(startingPlayer)));
    }
}
