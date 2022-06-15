package org.tensorflow.lite.examples.detection.logic.moves;

import org.tensorflow.lite.examples.detection.logic.BoardElements.Card;

public class TableToTable extends Move{
    public TableToTable(Card fromCard, Card toCard) {
        this.fromCard = fromCard;
        this.toCard = toCard;
    }


}
