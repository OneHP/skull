package skull.controller.action;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import skull.controller.PlayerView;
import skull.domain.action.Bid;

@JsonAutoDetect
public class BidView extends ActionView{

    private int bid;

    public BidView() {
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public static BidView fromBid(Bid bid){
        BidView view = new BidView();
        view.setPlayer(PlayerView.fromPlayer(bid.getActingPlayer()));
        view.setAction("bid");
        view.bid = bid.getValue();
        return view;
    }
}
