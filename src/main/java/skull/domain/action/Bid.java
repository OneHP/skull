package skull.domain.action;

import skull.domain.Player;

import javax.persistence.Entity;

@Entity
public class Bid extends Action {

    private int value;

    public Bid() {
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static Bid create(Player player, int value){
        Bid bid = new Bid();
        bid.setActingPlayer(player);
        bid.value = value;
        return bid;
    }
}
