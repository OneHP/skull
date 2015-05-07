package skull.domain;

import javax.persistence.Entity;

@Entity
public class Player extends PersistableDomainObject{

    private String name;

    private int points;

    private int skulls;
    private int roses;

    public Player(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getSkulls() {
        return skulls;
    }

    public void setSkulls(int skulls) {
        this.skulls = skulls;
    }

    public int getRoses() {
        return roses;
    }

    public void setRoses(int roses) {
        this.roses = roses;
    }

    public static Player create(String name){
        Player player = new Player();
        player.setPoints(0);
        player.setSkulls(1);
        player.setRoses(3);
        player.setName(name);
        return player;
    }
}
