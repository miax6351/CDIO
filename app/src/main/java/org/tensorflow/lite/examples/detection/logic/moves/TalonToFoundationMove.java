package org.tensorflow.lite.examples.detection.logic.moves;

import org.tensorflow.lite.examples.detection.logic.BoardElements.Card;

public class TalonToFoundationMove extends Move{
    private int foundationSuit;
    public TalonToFoundationMove(Card fromCard, Card toCard, int foundationSuit) {
        this.fromCard = fromCard;
        this.toCard = toCard;
        this.foundationSuit = foundationSuit;
    }
}
