package skull.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.google.common.collect.Lists;
import skull.controller.action.ActionView;
import skull.controller.action.BidView;
import skull.controller.action.FlipOtherPlayersCardView;
import skull.domain.Card;
import skull.domain.PlayerState;
import skull.domain.RoundPhase;
import skull.domain.RoundState;
import skull.domain.action.*;

import java.util.List;
import java.util.stream.Collectors;

@JsonAutoDetect
public class RoundStateView extends ViewSupport{

    private int maxBid;

    private List<Card> hand;
    private List<Card> playedCards;

    private List<PlayerStateView> playerStates;

    private RoundPhase roundPhase;

    private PlayerView playerToAct;

    private ActionView previousAction;

    public RoundStateView() {
    }

    public static RoundStateView fromRoundState(RoundState roundState, Long playerId){
        RoundStateView view = new RoundStateView();
        view.maxBid = roundState.getMaxBid();

        PlayerState playerState = roundState.getPlayerStates().stream()
                .filter(state -> state.getPlayer().getId().equals(playerId))
                .findFirst().get();
        view.hand = Lists.newArrayList();
        for(int i = 0; i < playerState.getHand().getSkulls(); i++){
            view.hand.add(Card.SKULL);
        }
        for(int i = 0; i < playerState.getHand().getRoses(); i++){
            view.hand.add(Card.ROSE);
        }
        view.playedCards = playerState.getCardsOnTable();

        view.playerStates = roundState.getPlayerStates().stream()
                .map(PlayerStateView::fromPlayerState)
                .collect(Collectors.toList());
        view.roundPhase = roundState.getRoundPhase();
        view.playerToAct = PlayerView.fromPlayer(roundState.getPlayerToAct());
        view.previousAction = createPreviousAction(roundState.getPreviousAction());
        return view;
    }

    private static ActionView createPreviousAction(Action previousAction) {
        if(null==previousAction){
            return null;
        }
        if(previousAction instanceof Bid){
            return BidView.fromBid((Bid) previousAction);
        }
        if(previousAction instanceof FlipOtherPlayersCard){
            return FlipOtherPlayersCardView.fromFlipOtherPlayersCard((FlipOtherPlayersCard) previousAction);
        }
        if(previousAction instanceof LayCard){
            return ActionView.fromAction(previousAction,"lay_card");
        }
        if(previousAction instanceof FlipOwnCards){
            return ActionView.fromAction(previousAction,"flip_own_cards");
        }
        if(previousAction instanceof OptOutOfBidding){
            return ActionView.fromAction(previousAction,"opt_out_of_bidding");
        }
        return null;
    }

    public int getMaxBid() {
        return maxBid;
    }

    public void setMaxBid(int maxBid) {
        this.maxBid = maxBid;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void setHand(List<Card> hand) {
        this.hand = hand;
    }

    public List<Card> getPlayedCards() {
        return playedCards;
    }

    public void setPlayedCards(List<Card> playedCards) {
        this.playedCards = playedCards;
    }

    public List<PlayerStateView> getPlayerStates() {
        return playerStates;
    }

    public void setPlayerStates(List<PlayerStateView> playerStates) {
        this.playerStates = playerStates;
    }

    public RoundPhase getRoundPhase() {
        return roundPhase;
    }

    public void setRoundPhase(RoundPhase roundPhase) {
        this.roundPhase = roundPhase;
    }

    public PlayerView getPlayerToAct() {
        return playerToAct;
    }

    public void setPlayerToAct(PlayerView playerToAct) {
        this.playerToAct = playerToAct;
    }

    public ActionView getPreviousAction() {
        return previousAction;
    }

    public void setPreviousAction(ActionView previousAction) {
        this.previousAction = previousAction;
    }
}
