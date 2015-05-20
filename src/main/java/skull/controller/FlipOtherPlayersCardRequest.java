package skull.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class FlipOtherPlayersCardRequest {

    private Long targetPlayerId;
    private int index;

    public FlipOtherPlayersCardRequest() {
    }

    public Long getTargetPlayerId() {
        return targetPlayerId;
    }

    public void setTargetPlayerId(Long targetPlayerId) {
        this.targetPlayerId = targetPlayerId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
