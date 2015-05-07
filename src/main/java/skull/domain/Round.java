package skull.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

@Entity
public class Round extends PersistableDomainObject{

    @OneToOne
    private Player startingPlayer;

    @OneToMany(cascade = CascadeType.PERSIST)
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
