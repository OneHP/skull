package skull.service.exception;

import skull.domain.Player;

public class PlayerActingOutOfTurnException extends Exception {

    private Player actingPlayer;
    private Player expectedPlayer;

    public PlayerActingOutOfTurnException(Player actingPlayer, Player expectedPlayer) {
        super(String.format("Player acting out of turn: %s. Expected player %s to act", actingPlayer.getName()
                , expectedPlayer.getName()));
        this.actingPlayer = actingPlayer;
        this.expectedPlayer = expectedPlayer;
    }

    public Player getActingPlayer() {
        return actingPlayer;
    }

    public Player getExpectedPlayer() {
        return expectedPlayer;
    }
}
