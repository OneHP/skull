package skull.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class Round extends PersistableDomainObject{

    @OneToOne(cascade = CascadeType.ALL)
    private Player startingPlayer;

    @OneToMany(cascade = CascadeType.ALL)
    private List<RoundState> roundStates;

    public Round() {

    }

    public Player getStartingPlayer() {
        return startingPlayer;
    }

    public void setStartingPlayer(Player startingPlayer) {
        this.startingPlayer = startingPlayer;
    }

    public List<RoundState> getRoundStates() {
        return roundStates;
    }

    public void setRoundStates(List<RoundState> roundStates) {
        this.roundStates = roundStates;
    }
}
