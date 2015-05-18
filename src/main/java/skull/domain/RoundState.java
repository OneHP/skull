package skull.domain;

import com.google.common.collect.Lists;
import skull.domain.action.Action;

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

    @OneToOne(cascade = CascadeType.ALL)
    private Action previousAction;

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

    public Action getPreviousAction() {
        return previousAction;
    }

    public void setPreviousAction(Action previousAction) {
        this.previousAction = previousAction;
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
