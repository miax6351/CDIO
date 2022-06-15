package org.tensorflow.lite.examples.detection.logic.moves;

import org.tensorflow.lite.examples.detection.logic.BoardElements.Board;
import org.tensorflow.lite.examples.detection.logic.BoardElements.Card;

public class TableToTable extends Move{
    public TableToTable(Card fromCard, Card toCard, Board board) {
        this.fromCard = fromCard;
        this.toCard = toCard;
        this.board = board;
    }

    @Override
    protected void processMove() {

    }
}
