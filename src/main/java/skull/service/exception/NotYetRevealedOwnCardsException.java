package skull.service.exception;

public class NotYetRevealedOwnCardsException extends Exception {

    public NotYetRevealedOwnCardsException() {
        super("Player has not yet revealed their own cards");
    }
}
