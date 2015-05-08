package skull.domain;

import com.google.common.collect.Lists;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class RoundState extends PersistableDomainObject{

    private int maxBid;

    @OneToMany(cascade = CascadeType.ALL)
    private List<PlayerState> playerStates;

    @Enumerated(EnumType.STRING)
    private RoundPhase roundPhase;

    @ManyToOne(cascade = CascadeType.ALL)
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

    public RoundState copy(){
        RoundState copy = new RoundState();
        copy.setMaxBid(this.maxBid);
        copy.setRoundPhase(this.roundPhase);
        copy.setPlayerToAct(this.playerToAct);
        copy.setPlayerStates(this.playerStates.stream().map(PlayerState::copy).collect(Collectors.toList()));
        return copy;
    }
}
