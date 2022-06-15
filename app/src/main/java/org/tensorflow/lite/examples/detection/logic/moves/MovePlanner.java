package org.tensorflow.lite.examples.detection.logic.moves;

import org.tensorflow.lite.examples.detection.logic.BoardElements.Board;
import org.tensorflow.lite.examples.detection.logic.BoardElements.Card;
import org.tensorflow.lite.examples.detection.logic.BoardElements.Foundation;
import org.tensorflow.lite.examples.detection.logic.BoardElements.Stack;
import org.tensorflow.lite.examples.detection.logic.BoardElements.Table;
import org.tensorflow.lite.examples.detection.logic.BoardElements.Talon;

import java.util.ArrayList;

public class MovePlanner {
    private Board board;
    public MovePlanner(Board board) {
        this.board = board;
    }

    public void processCard(Card card){

    }
    public ArrayList<Move> generateMoves(){

        //Initialize all elements used
        Table table = board.getTable();
        Foundation foundation = board.getFoundation();
        Talon talon = board.getTalon();
        Stack[] tableColumns = table.getColumns();
        ArrayList<Move> allPosibleMoves= new ArrayList<Move>();

        //Moves from table
        for (int i = 0; i < tableColumns.length; i++) {
            Card northMostCardFrom = tableColumns[i].getNorthMostCard();
            Card southMostCardFrom = tableColumns[i].getSouthMostCard();

            //Check for table to foundation moves
            if(southMostCardFrom.isCardAbleToGoOnCard(foundation.getClovers().getSouthMostCard())){
                allPosibleMoves.add(new TableToFoundationMove(southMostCardFrom,foundation.getClovers().getSouthMostCard(), 13));
            }
            if(southMostCardFrom.isCardAbleToGoOnCard(foundation.getSpades().getSouthMostCard())){
                allPosibleMoves.add(new TableToFoundationMove(southMostCardFrom,foundation.getSpades().getSouthMostCard(), 14));
            }
            if(southMostCardFrom.isCardAbleToGoOnCard(foundation.getHearts().getSouthMostCard())){
                allPosibleMoves.add(new TableToFoundationMove(southMostCardFrom,foundation.getHearts().getSouthMostCard(), 11));
            }
            if(southMostCardFrom.isCardAbleToGoOnCard(foundation.getDiamonds().getSouthMostCard())){
                allPosibleMoves.add(new TableToFoundationMove(southMostCardFrom,foundation.getDiamonds().getSouthMostCard(), 12));
            }

            //Check for Table to Table moves
            for (int j = 0; j < tableColumns.length; j++) {
                if(i!=j){
                    Card southMostCardTo = tableColumns[j].getSouthMostCard();
                    if(northMostCardFrom.isCardAbleToGoOnCard(southMostCardTo)){
                        allPosibleMoves.add(new TableToTableMove(northMostCardFrom, southMostCardTo, j));
                    }
                }
            }
        }

        //Moves From Talon
        Card talonTopCard = talon.getTopCard();
        //Talon to Table Moves
        for (int j = 0; j < tableColumns.length; j++) {
            Card southMostCardToTalon = tableColumns[j].getSouthMostCard();
            if(talonTopCard.isCardAbleToGoOnCard(southMostCardToTalon)){
                allPosibleMoves.add(new TalonToTableMove(talonTopCard, southMostCardToTalon, j));
            }
        }
        
        //Check for talon to foundation moves
        if(talonTopCard.isCardAbleToGoOnCard(foundation.getClovers().getSouthMostCard())){
            allPosibleMoves.add(new TalonToFoundationMove(talonTopCard,foundation.getClovers().getSouthMostCard(), 13));
        }
        if(talonTopCard.isCardAbleToGoOnCard(foundation.getSpades().getSouthMostCard())){
            allPosibleMoves.add(new TalonToFoundationMove(talonTopCard,foundation.getSpades().getSouthMostCard(), 14));
        }
        if(talonTopCard.isCardAbleToGoOnCard(foundation.getHearts().getSouthMostCard())){
            allPosibleMoves.add(new TalonToFoundationMove(talonTopCard,foundation.getHearts().getSouthMostCard(), 11));
        }
        if(talonTopCard.isCardAbleToGoOnCard(foundation.getDiamonds().getSouthMostCard())){
            allPosibleMoves.add(new TalonToFoundationMove(talonTopCard,foundation.getDiamonds().getSouthMostCard(), 12));
        }

        //If no moves are available, draw card from talon.
        if(allPosibleMoves.isEmpty()){
            allPosibleMoves.add(new DrawCardFromTalon());
        }
        return allPosibleMoves;
    }

    public void evaluateMoves(){

    }
    public void executeMove(){

    }
}
