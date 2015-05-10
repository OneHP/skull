package skull.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class PlayerRequest {

    private String name;

    public PlayerRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
