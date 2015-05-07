package skull.service;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import skull.domain.Game;
import skull.domain.Player;
import skull.repo.GameRepository;
import skull.repo.PlayerRepository;
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
}
