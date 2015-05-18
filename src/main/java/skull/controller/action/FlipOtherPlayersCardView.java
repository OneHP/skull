package skull.controller.action;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import skull.controller.PlayerView;
import skull.domain.action.Action;
import skull.domain.action.FlipOtherPlayersCard;

@JsonAutoDetect
public class FlipOtherPlayersCardView extends ActionView {

    private PlayerView targetPlayer;
    private int index;

    public FlipOtherPlayersCardView() {
    }

    public PlayerView getTargetPlayer() {
        return targetPlayer;
    }

    public void setTargetPlayer(PlayerView targetPlayer) {
        this.targetPlayer = targetPlayer;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public static FlipOtherPlayersCardView fromFlipOtherPlayersCard(FlipOtherPlayersCard flipOtherPlayersCard){
        FlipOtherPlayersCardView view = new FlipOtherPlayersCardView();
        view.setAction("flip_other_players_card");
        view.setPlayer(PlayerView.fromPlayer(flipOtherPlayersCard.getActingPlayer()));
        view.index = flipOtherPlayersCard.getIndex();
        view.targetPlayer = PlayerView.fromPlayer(flipOtherPlayersCard.getTargetPlayer());
        return view;
    }
}
