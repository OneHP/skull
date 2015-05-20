package skull.controller.action;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import skull.controller.PlayerView;
import skull.controller.ViewSupport;
import skull.domain.action.Action;

@JsonAutoDetect
public class ActionView extends ViewSupport{

    private PlayerView player;
    private String action;

    public ActionView() {
    }

    public PlayerView getPlayer() {
        return player;
    }

    public void setPlayer(PlayerView player) {
        this.player = player;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public static ActionView fromAction(Action action, String actionName){
        ActionView view = new ActionView();
        view.action = actionName;
        view.player = PlayerView.fromPlayer(action.getActingPlayer());
        return view;
    }
}
