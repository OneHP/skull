package skull.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class Round {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Player startingPlayer;

    @OneToMany
    private List<RoundState> roundStates;

    public Round() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
