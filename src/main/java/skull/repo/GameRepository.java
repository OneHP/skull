package skull.repo;

import org.springframework.data.repository.CrudRepository;
import skull.domain.Game;

public interface GameRepository extends CrudRepository<Game,Long> {
}
