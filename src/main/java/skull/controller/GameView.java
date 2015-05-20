package skull.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import skull.domain.Game;

import java.util.List;
import java.util.stream.Collectors;

@JsonAutoDetect
public class GameView extends ViewSupport{

    private Long id;
    private String key;
    private boolean started;
    private PlayerView winner;
    private List<PlayerView> players;
    private List<RoundView> rounds;

    public GameView() {

    }

    /**
     * View of the game from the perspective of the given player. This view represents only what the player may know about the game; the size of all players hands, how many cards are on the table, the type of their own cards etc.. It must not contain knowledge that could not have; the types of other players face down cards etc..
     *
     * @param game
     * @param playerId
     * @return
     */
    public static GameView fromGame(Game game, Long playerId){
        GameView view = new GameView();
        view.id = game.getId();
        view.key = game.getKey();
        view.started = game.getStarted();
        view.winner = PlayerView.fromPlayer(game.getWinner());
        view.players = game.getPlayers().stream()
                .map(PlayerView::fromPlayer)
                .collect(Collectors.toList());
        if(null!=game.getRounds()) {
            view.rounds = game.getRounds().stream()
                    .map(round -> RoundView.fromRound(round,playerId))
                    .collect(Collectors.toList());
        }
        return view;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean getStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public PlayerView getWinner() {
        return winner;
    }

    public void setWinner(PlayerView winner) {
        this.winner = winner;
    }

    public List<PlayerView> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerView> players) {
        this.players = players;
    }

    public List<RoundView> getRounds() {
        return rounds;
    }

    public void setRounds(List<RoundView> rounds) {
        this.rounds = rounds;
    }
}
