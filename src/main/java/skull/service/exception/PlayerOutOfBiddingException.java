package skull.service.exception;

import skull.domain.Player;

public class PlayerOutOfBiddingException extends Exception {

    private Player playerBidding;

    public PlayerOutOfBiddingException(Player playerBidding) {
        super(String.format("Player %s has opted out of bidding already.", playerBidding.getName()));
        this.playerBidding = playerBidding;
    }

    public Player getPlayerBidding() {
        return playerBidding;
    }
}
