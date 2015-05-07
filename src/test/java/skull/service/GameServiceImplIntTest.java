package skull.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import skull.SkullApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkullApplication.class)
@WebAppConfiguration
public class GameServiceImplIntTest {

    private static String HOST_PLAYER_NAME = "Thomas";

    @Autowired
    private GameService serviceUnderTest;

    @Test
    public void canCreateGame(){
        this.serviceUnderTest.createGame(HOST_PLAYER_NAME);
    }
}
