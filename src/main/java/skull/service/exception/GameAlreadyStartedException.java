package skull.service.exception;

public class GameAlreadyStartedException extends Exception {

    public GameAlreadyStartedException() {
        super("Game has already started");
    }
}
