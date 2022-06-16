package org.tensorflow.lite.examples.detection.logic.rules;

import org.tensorflow.lite.examples.detection.logic.BoardElements.Board;
import org.tensorflow.lite.examples.detection.logic.moves.Move;

public class Rule {
    protected Board board;

    public Rule(Board board) {
        this.board = board;
    }

    protected void evaluateRule(Move move){

    }
}
