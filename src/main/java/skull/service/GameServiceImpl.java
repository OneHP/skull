package skull.service;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import skull.domain.*;
import skull.repo.GameRepository;
import skull.service.exception.InsufficientPlayersException;
import skull.util.RandomKeyUtil;

import javax.transaction.Transactional;
import java.util.Random;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private GameRepository gameRepository;

    @Override
    @Transactional
    public Game createGame(String playerName) {

        Player host = new Player();
        host.setName(playerName);

        Game game = new Game();
        game.setKey(RandomKeyUtil.generateKey());
        game.setPlayers(Lists.newArrayList(host));

        return this.gameRepository.save(game);
    }

    @Override
    public Game getGame(Long id) {
        return this.gameRepository.findOne(id);
    }

    @Override
    @Transactional
    public Game addPlayer(Long gameId, String playerName) {

        Player player = new Player();
        player.setName(playerName);

        final Game game = this.gameRepository.findOne(gameId);
        game.getPlayers().add(player);

        return this.gameRepository.save(game);
    }

    @Override
    @Transactional
    public Game startGame(Long gameId) throws InsufficientPlayersException {
        final Game game = this.gameRepository.findOne(gameId);

        final int numberOfPlayers = game.getPlayers().size();
        if(numberOfPlayers < 2){
            throw new InsufficientPlayersException(numberOfPlayers,2);
        }

        game.setStarted(true);

        Round round = new Round();
        round.setStartingPlayer(game.getPlayers().get(new Random().nextInt(numberOfPlayers)));

        RoundState initialRoundState = new RoundState();
        initialRoundState.setMaxBid(-1);
        initialRoundState.setRoundPhase(RoundPhase.LAYING);
        initialRoundState.setPlayerToAct(round.getStartingPlayer());
        initialRoundState.setPlayerStates(Lists.transform(game.getPlayers(), ((Player player) -> PlayerState.create(player))));

        round.setRoundStates(Lists.newArrayList(initialRoundState));
        game.setRounds(Lists.newArrayList(round));

        return this.gameRepository.save(game);
    }
}
