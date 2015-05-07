package skull.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(BlockJUnit4ClassRunner.class)
public class RandomKeyUtilTest {

    @Test
    public void canGenerateRandomKey(){
        assertThat(RandomKeyUtil.generateKey().length(),is(9));
    }
}
