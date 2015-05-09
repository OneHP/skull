package skull.service.exception;

public class BidTooHighException extends Exception {

    private int bid;
    private int cardsOnTable;

    public BidTooHighException(int bid, int cardsOnTable) {
        super(String.format("Bid %s is too high. Only %s cards on table.",bid,cardsOnTable));
        this.bid = bid;
        this.cardsOnTable = cardsOnTable;
    }
}
