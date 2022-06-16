package org.tensorflow.lite.examples.detection.logic.moves;

import org.tensorflow.lite.examples.detection.logic.BoardElements.Card;

public class TableToFoundationMove extends Move {

    private int foundationSuit;
    public TableToFoundationMove(Card fromCard, Card toCard, int foundationSuit) {
        this.fromCard = fromCard;
        this.toCard = toCard;
        this.foundationSuit = foundationSuit;
    }
}
