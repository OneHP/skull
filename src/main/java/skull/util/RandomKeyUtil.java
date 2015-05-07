package skull.util;

import java.util.Random;

public class RandomKeyUtil {

    private static final Character[] CHARACTERS = new Character[]{'A','S','D','F','G','H','J','K','L'};

    /**
     * Generates a random key that is made of 8 characters from the home row and a single number between 1 and 9
     *
     * @return A random key
     */
    public static String generateKey(){

        Random random = new Random();

        StringBuilder builder = new StringBuilder();

        for(int i = 0; i < 9; i++){
            builder.append(CHARACTERS[random.nextInt(9)]);
        }

        builder.append(random.nextInt(8)+1);

        return builder.toString();
    }
}
