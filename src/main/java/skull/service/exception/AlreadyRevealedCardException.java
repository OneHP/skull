package skull.service.exception;

public class AlreadyRevealedCardException extends Exception {

    public AlreadyRevealedCardException() {
        super("Card has already been revealed");
    }
}
