package skull.service.exception;

import skull.domain.Card;

public class CardNotInHandException extends Exception{

    private Card card;

    public CardNotInHandException(Card card) {
        super(String.format("Card %s is not in hand.",card));
        this.card = card;
    }

    public Card getCard() {
        return card;
    }
}
