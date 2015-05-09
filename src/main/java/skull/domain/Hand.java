package skull.domain;

import javax.persistence.Entity;

@Entity
public class Hand extends PersistableDomainObject{

    private int skulls;
    private int roses;

    public Hand() {

    }

    public int getSkulls() {
        return skulls;
    }

    public void setSkulls(int skulls) {
        this.skulls = skulls;
    }

    public int getRoses() {
        return roses;
    }

    public void setRoses(int roses) {
        this.roses = roses;
    }

    public static Hand create(Player player){
        Hand hand = new Hand();
        hand.skulls = player.getSkulls();
        hand.roses = player.getRoses();
        return hand;
    }

    public Hand copy() {
        Hand hand = new Hand();
        hand.skulls = this.skulls;
        hand.roses = this.roses;
        return hand;
    }

    public void playCard(Card card){
        if(card.equals(Card.SKULL)){
            this.skulls--;
        }else {
            this.roses--;
        }
    }
}
