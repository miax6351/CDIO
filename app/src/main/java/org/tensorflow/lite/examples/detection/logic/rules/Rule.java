package org.tensorflow.lite.examples.detection.logic.rules;

import org.tensorflow.lite.examples.detection.logic.BoardElements.Board;

public class Rule {
    protected Board board;

    public Rule(Board board) {
        this.board = board;
    }
}
