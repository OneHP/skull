package skull.domain.action;

import skull.domain.Player;

import javax.persistence.Entity;

@Entity
public class FlipOwnCards extends Action{

    public FlipOwnCards() {
    }

    public static FlipOwnCards create(Player player){
        FlipOwnCards flipOwnCards = new FlipOwnCards();
        flipOwnCards.setActingPlayer(player);
        return flipOwnCards;
    }
}
