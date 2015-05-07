package skull.service;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import skull.domain.Game;
import skull.domain.Player;
import skull.repo.GameRepository;
import skull.repo.PlayerRepository;
import skull.service.exception.InsufficientPlayersException;
import skull.util.RandomKeyUtil;

import javax.transaction.Transactional;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    @Transactional
    public Game createGame(String playerName) {

        Player host = new Player();
        host.setName(playerName);
        this.playerRepository.save(host);

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
        this.playerRepository.save(player);

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
        return this.gameRepository.save(game);
    }
}
