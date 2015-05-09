package skull.domain;

import com.google.common.collect.Lists;

import javax.persistence.*;
import java.util.List;

@Entity
public class PlayerState extends PersistableDomainObject{

    @ManyToOne(cascade = CascadeType.ALL)
    private Player player;

    @ManyToOne(cascade = CascadeType.ALL)
    private Hand hand;

    @ElementCollection(targetClass=Card.class)
    @Enumerated(EnumType.STRING)
    private List<Card> cardsOnTable;
    private boolean outOfBidding;
    private int bid;

    public PlayerState(){

    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Hand getHand() {
        return hand;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
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

    public static PlayerState create(Player player){
        PlayerState playerState = new PlayerState();
        playerState.setBid(0);
        playerState.setCardsOnTable(Lists.newArrayList());
        playerState.setOutOfBidding(false);
        playerState.setPlayer(player);
        playerState.setHand(Hand.create(player));
        return playerState;
    }

    public PlayerState copy(){
        PlayerState playerState = new PlayerState();
        playerState.setBid(this.bid);
        playerState.setCardsOnTable(this.cardsOnTable);
        playerState.setOutOfBidding(this.outOfBidding);
        playerState.setPlayer(this.player);
        playerState.setHand(this.hand.copy());
        return playerState;
    }
}
