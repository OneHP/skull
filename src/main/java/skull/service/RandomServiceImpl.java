package skull.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * A random service that simply delegates on to java.util.Random
 */
@Profile("!testing")
@Service
public class RandomServiceImpl implements RandomService {

    @Override
    public int randomInt(int exclusiveMaximum) {
        return new Random().nextInt(exclusiveMaximum);
    }
}
