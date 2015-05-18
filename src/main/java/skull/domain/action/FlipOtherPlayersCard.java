package skull.domain.action;

import skull.domain.Player;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class FlipOtherPlayersCard extends Action {

    @ManyToOne
    private Player targetPlayer;

    private int index;

    public FlipOtherPlayersCard() {
    }

    public Player getTargetPlayer() {
        return targetPlayer;
    }

    public void setTargetPlayer(Player targetPlayer) {
        this.targetPlayer = targetPlayer;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public static FlipOtherPlayersCard create(Player player, Player targetPlayer, int index){
        FlipOtherPlayersCard flipOtherPlayersCard = new FlipOtherPlayersCard();
        flipOtherPlayersCard.setActingPlayer(player);
        flipOtherPlayersCard.targetPlayer = targetPlayer;
        flipOtherPlayersCard.index = index;
        return flipOtherPlayersCard;
    }
}
