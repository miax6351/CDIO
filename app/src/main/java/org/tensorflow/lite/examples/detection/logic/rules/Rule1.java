package org.tensorflow.lite.examples.detection.logic.rules;

import org.tensorflow.lite.examples.detection.logic.BoardElements.Board;
import org.tensorflow.lite.examples.detection.logic.moves.Move;

import java.util.List;

/**
 * RULE ONE: Always play an Ace or Deuce wherever you can immediately.
 */
public class Rule1 extends Rule {

    public Rule1(Board board) {
        super(board);
    }

    public Move rule1(){
        List<Integer> coloumnIndex = board.getTable().getColumnsWithAceOrDeuce();
        if (!coloumnIndex.isEmpty()){
            for (Integer i : coloumnIndex){

            }
        }

    }
}
