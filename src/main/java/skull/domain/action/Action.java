package skull.domain.action;

import skull.domain.PersistableDomainObject;
import skull.domain.Player;

import javax.persistence.*;

@Entity
@Inheritance(strategy= InheritanceType.JOINED)
public class Action extends PersistableDomainObject {

    @ManyToOne(cascade = CascadeType.ALL)
    private Player actingPlayer;

    public Action() {

    }

    public Player getActingPlayer() {
        return actingPlayer;
    }

    public void setActingPlayer(Player actingPlayer) {
        this.actingPlayer = actingPlayer;
    }

}
