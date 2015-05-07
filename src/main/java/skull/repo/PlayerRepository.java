package skull.repo;

import org.springframework.data.repository.CrudRepository;
import skull.domain.Player;

public interface PlayerRepository extends CrudRepository<Player,Long>{
}
