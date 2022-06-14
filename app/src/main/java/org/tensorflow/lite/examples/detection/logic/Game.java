package org.tensorflow.lite.examples.detection.logic;

import org.tensorflow.lite.examples.detection.logic.BoardElements.Card;
import org.tensorflow.lite.examples.detection.logic.moves.Board;

import java.util.List;

public class Game {


    private Board board = new Board();
    public void init(List<Card> startingCards){

        board.startGameSetup()
    }

}
