package skull.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * A testing version of the random service that always returns the same number.
 */
@Service
@Profile("testing")
public class RandomServiceTestingImpl implements RandomService{

    @Override
    public int randomInt(int exclusiveMaximum) {
        return 0;
    }
}
