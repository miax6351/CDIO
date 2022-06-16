package org.tensorflow.lite.examples.detection.logic.moves;

import org.tensorflow.lite.examples.detection.logic.BoardElements.Board;
import org.tensorflow.lite.examples.detection.logic.BoardElements.Card;

public class Move {

    protected Card fromCard;
    protected Card toCard;


    protected Card getFromCard() {
        return fromCard;
    }

    protected Card getToCard() {
        return toCard;
    }


}
