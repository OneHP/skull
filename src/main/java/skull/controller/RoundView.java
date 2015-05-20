package skull.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import skull.domain.Round;

import javax.swing.text.View;
import java.util.List;
import java.util.stream.Collectors;

@JsonAutoDetect
public class RoundView extends ViewSupport{

    private List<RoundStateView> roundStates;

    public RoundView() {
    }

    public static RoundView fromRound(Round round,Long playerId){
        RoundView view = new RoundView();
        view.roundStates = round.getRoundStates().stream()
                .map(state -> RoundStateView.fromRoundState(state,playerId))
                .collect(Collectors.toList());
        return view;
    }

    public List<RoundStateView> getRoundStates() {
        return roundStates;
    }

    public void setRoundStates(List<RoundStateView> roundStates) {
        this.roundStates = roundStates;
    }
}
