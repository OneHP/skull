package skull.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import skull.domain.Player;

@JsonAutoDetect
public class PlayerView extends ViewSupport{

    private Long id;
    private String name;

    public PlayerView(){

    }

    public static PlayerView fromPlayer(Player player){
        if(null==player){
            return null;
        }
        PlayerView view = new PlayerView();
        view.id = player.getId();
        view.name = player.getName();
        return view;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
