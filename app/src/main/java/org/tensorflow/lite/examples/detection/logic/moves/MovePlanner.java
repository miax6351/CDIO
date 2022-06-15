package org.tensorflow.lite.examples.detection.logic.moves;

import org.tensorflow.lite.examples.detection.logic.BoardElements.Board;
import org.tensorflow.lite.examples.detection.logic.BoardElements.Card;

public class MovePlanner {
    private Board board;
    public MovePlanner(Board board) {
        this.board = board;
    }

    public void processCard(Card card){

    }
    public Move generateMoves(){
        return new Move();
    }
    public void executeMove(){

    }
}
