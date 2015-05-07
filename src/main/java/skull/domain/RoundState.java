package skull.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class RoundState extends PersistableDomainObject{

    private int maxBid;

    @OneToMany(cascade = CascadeType.PERSIST)
    private List<PlayerState> playerStates;

    @Enumerated(EnumType.STRING)
    private RoundPhase roundPhase;

    @ManyToOne
    private Player playerToAct;

    public RoundState(){

    }

    public int getMaxBid() {
        return maxBid;
    }

    public void setMaxBid(int maxBid) {
        this.maxBid = maxBid;
    }

    public List<PlayerState> getPlayerStates() {
        return playerStates;
    }

    public void setPlayerStates(List<PlayerState> playerStates) {
        this.playerStates = playerStates;
    }

    public RoundPhase getRoundPhase() {
        return roundPhase;
    }

    public void setRoundPhase(RoundPhase roundPhase) {
        this.roundPhase = roundPhase;
    }

    public Player getPlayerToAct() {
        return playerToAct;
    }

    public void setPlayerToAct(Player playerToAct) {
        this.playerToAct = playerToAct;
    }
}
