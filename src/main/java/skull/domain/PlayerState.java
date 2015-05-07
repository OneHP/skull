package skull.domain;

import com.google.common.collect.Lists;

import javax.persistence.*;
import java.util.List;

@Entity
public class PlayerState {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Player player;

    @ElementCollection(targetClass=Card.class)
    @Enumerated(EnumType.STRING)
    private List<Card> cardsOnTable;
    private boolean outOfBidding;
    private int bid;

    public PlayerState(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
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
        return playerState;
    }
}
