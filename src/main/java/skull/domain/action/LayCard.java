package skull.domain.action;

import skull.domain.Card;
import skull.domain.Player;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
public class LayCard extends Action{

    @Enumerated(EnumType.STRING)
    private Card card;

    public LayCard() {

    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public static LayCard create(Player player, Card card){
        LayCard layCard = new LayCard();
        layCard.setActingPlayer(player);
        layCard.card = card;
        return layCard;
    }
}
