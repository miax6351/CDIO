package org.tensorflow.lite.examples.detection.logic.moves;

import org.tensorflow.lite.examples.detection.logic.BoardElements.Card;

public class Move {

    protected Card fromCard;
    protected Card toCard;

    public Card getFromCard() {
        return fromCard;
    }

    public Card getToCard() {
        return toCard;
    }
}
