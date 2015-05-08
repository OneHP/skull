package skull.service.exception;

public class BidTooLowException extends Exception {


    private int bid;
    private int maxBid;

    public BidTooLowException(int bid, int maxBid) {
        super(String.format("Bid of %s is too low. Bid should be in excess of %s.",bid,maxBid));
        this.bid = bid;
        this.maxBid = maxBid;
    }

    public int getBid() {
        return bid;
    }

    public int getMaxBid() {
        return maxBid;
    }
}
