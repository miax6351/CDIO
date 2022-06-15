package org.tensorflow.lite.examples.detection.logic;

import org.tensorflow.lite.examples.detection.logic.BoardElements.Card;
import org.tensorflow.lite.examples.detection.logic.BoardElements.Board;
import org.tensorflow.lite.examples.detection.logic.moves.MovePlanner;

import java.util.List;

public class Game {


    private Board board = new Board();
    private MovePlanner movePlanner = new MovePlanner(board);
    public void init(List<Card> startingCards){

        board.startGameSetup(startingCards);
        movePlanner.generateMoves();
    }

    public void playGame(Card card){

    }

}
