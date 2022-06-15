package org.tensorflow.lite.examples.detection.logic.moves;

import org.tensorflow.lite.examples.detection.logic.BoardElements.Board;
import org.tensorflow.lite.examples.detection.logic.BoardElements.Card;
import org.tensorflow.lite.examples.detection.logic.BoardElements.Foundation;
import org.tensorflow.lite.examples.detection.logic.BoardElements.Stack;
import org.tensorflow.lite.examples.detection.logic.BoardElements.Table;

import java.util.ArrayList;

public class MovePlanner {
    private Board board;
    public MovePlanner(Board board) {
        this.board = board;
    }

    public void processCard(Card card){

    }
    public ArrayList<Move> generateMoves(){
        Table table = board.getTable();
        Foundation foundation = board.getFoundation();
        Stack[] tableColumns = table.getColumns();
        ArrayList<Move> allPosibleMoves= new ArrayList<Move>();

        //Moves from table
        for (int i = 0; i < tableColumns.length; i++) {
            Card northMostCardFrom = tableColumns[i].getNorthMostCard();
            Card southMostCardFrom = tableColumns[i].getSouthMostCard();

            //Check for Table to Table moves
            for (int j = 0; j < tableColumns.length; j++) {
                if(i!=j){
                    Card southMostCardTo = tableColumns[j].getSouthMostCard();
                    if(northMostCardFrom.isCardAbleToGoOnCard(southMostCardTo)){
                        allPosibleMoves.add(new TableToTableMove(northMostCardFrom, southMostCardTo, j));
                    }
                }
            }
            //Check for table to foundation moves
            if(southMostCardFrom.isCardAbleToGoOnCard(foundation.getClovers().getSouthMostCard())){

            }


        }

        return allPosibleMoves;
    }
    public void executeMove(){

    }
}
