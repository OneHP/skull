package skull.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import skull.domain.Round;

import java.util.List;
import java.util.stream.Collectors;

@JsonAutoDetect
public class RoundView {

    private List<RoundStateView> roundStates;

    public RoundView() {
    }

    public static RoundView fromRound(Round round){
        RoundView view = new RoundView();
        view.roundStates = round.getRoundStates().stream()
                .map(RoundStateView::fromRoundState)
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
