package skull.service.exception;

import skull.domain.RoundPhase;

public class IncorrectRoundPhaseException extends Exception {

    private RoundPhase roundPhase;
    private RoundPhase expectedRoundPhase;

    public IncorrectRoundPhaseException(RoundPhase roundPhase, RoundPhase expectedRoundPhase) {
        super(String.format("Incorrect round phase %s. Expected round phase: %s",roundPhase,expectedRoundPhase));
        this.roundPhase = roundPhase;
        this.expectedRoundPhase = expectedRoundPhase;
    }

    public RoundPhase getRoundPhase() {
        return roundPhase;
    }

    public RoundPhase getExpectedRoundPhase() {
        return expectedRoundPhase;
    }
}
