package skull.domain.action;

import skull.domain.Player;

import javax.persistence.Entity;

@Entity
public class OptOutOfBidding extends Action {

    public OptOutOfBidding() {
    }

    public static OptOutOfBidding create(Player player){
        OptOutOfBidding optOutOfBidding = new OptOutOfBidding();
        optOutOfBidding.setActingPlayer(player);
        return optOutOfBidding;
    }
}
