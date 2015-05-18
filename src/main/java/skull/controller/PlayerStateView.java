package skull.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.google.common.collect.Lists;
import skull.domain.Card;
import skull.domain.Hand;
import skull.domain.PlayerState;

import java.util.List;

@JsonAutoDetect
public class PlayerStateView {

    private PlayerView player;

    private int cardsInHand;

    private List<Card> cardsOnTable;

    private boolean outOfBidding;
    private int bid;

    public PlayerStateView() {
    }

    public static PlayerStateView fromPlayerState(PlayerState playerState){
        PlayerStateView view = new PlayerStateView();
        view.player = PlayerView.fromPlayer(playerState.getPlayer());
        Hand hand = playerState.getHand();
        List<Card> playedCards = playerState.getCardsOnTable();
        int numberOfPlayedCards = playedCards.size();
        view.cardsInHand = (hand.getRoses() + hand.getSkulls()) - numberOfPlayedCards;
        view.cardsOnTable = Lists.newArrayList();
        for(int i = 0; i < numberOfPlayedCards; i++){
            view.cardsOnTable.add(i >= (numberOfPlayedCards - playerState.getNumberOfRevealedCards())
                    ? playedCards.get(i)
                    : Card.UNKNOWN);
        }
        view.outOfBidding = playerState.getOutOfBidding();
        view.bid = playerState.getBid();
        return view;
    }

    public PlayerView getPlayer() {
        return player;
    }

    public void setPlayer(PlayerView player) {
        this.player = player;
    }

    public int getCardsInHand() {
        return cardsInHand;
    }

    public void setCardsInHand(int cardsInHand) {
        this.cardsInHand = cardsInHand;
    }

    public List<Card> getCardsOnTable() {
        return cardsOnTable;
    }

    public void setCardsOnTable(List<Card> cardsOnTable) {
        this.cardsOnTable = cardsOnTable;
    }

    public boolean getOutOfBidding() {
        return outOfBidding;
    }

    public void setOutOfBidding(boolean outOfBidding) {
        this.outOfBidding = outOfBidding;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }
}
