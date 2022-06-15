package org.tensorflow.lite.examples.detection.logic.moves;

import org.tensorflow.lite.examples.detection.logic.BoardElements.Card;

public class TalonToTableMove extends Move{
    private int destinationColumnIndex;
    public TalonToTableMove(Card fromCard, Card toCard, int destinationColumnnIndex) {
        this.fromCard = fromCard;
        this.toCard = toCard;
        this.destinationColumnIndex = destinationColumnnIndex;
    }
}
