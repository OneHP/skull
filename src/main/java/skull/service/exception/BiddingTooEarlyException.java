package skull.service.exception;

import skull.domain.Player;

public class BiddingTooEarlyException extends Exception {

    private Player biddingPlayer;

    public BiddingTooEarlyException(Player biddingPlayer) {
        super(String.format("Player %s is bidding too early. Not every player has played a card yet.",
                biddingPlayer.getName()));
        this.biddingPlayer = biddingPlayer;
    }

    public Player getBiddingPlayer() {
        return biddingPlayer;
    }
}
