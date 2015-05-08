package skull.service.exception;

public class InsufficientPlayersException extends Exception {

    private int playerCount;
    private int requiredPlayers;

    public InsufficientPlayersException(int playerCount, int requiredPlayers){
        super(String.format("Insufficient players: %s. Requires at least %s players.",playerCount,requiredPlayers));
        this.playerCount = playerCount;
        this.requiredPlayers = requiredPlayers;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public int getRequiredPlayers() {
        return requiredPlayers;
    }
}
