package org.tensorflow.lite.examples.detection.logic.BoardElements;

import org.tensorflow.lite.examples.detection.logic.Card;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private Stack[] rows = new Stack[7];

    public Table(){
        for (int i = 0; i <7; i++){
            rows[i] = new Stack();
        }
    }
    public void setStacks(Stack[] stacks){
        this.rows = stacks;
    }
    public Stack[] getRows(){
        return rows;
    }
    public Stack getRow(int i){
        return rows[i];
    }

    public Stack getOneEmptyStack(){
        for (int i = 0; i < 7; i++){
            if (rows[i].isEmpty()){
                return rows[i];
            }
        }
        return null;
    }
    public Boolean isRowEmpty(Stack row){
        return row.isEmpty();
    }
    //get all move options for a specific card from row to row.
    public List<Stack> getAllMoveOptions(Card cardFrom, Stack stackFrom){
        List<Stack> allRowsOptions = new ArrayList<>();


        for (Stack stack : rows){
            //don't want to check if the card can be moved on it's own stack
            if (stack.equals(stackFrom)){
                Card topCard = stack.getSouthMostCard();
                boolean canBeMovedTo = cardFrom.isCardAbleToGoOnCard(topCard);

                //Only for the kings.
                if (stack.isEmpty()){
                    if (cardFrom.getCardNumber() == 'K'){
                        allRowsOptions.add(stack);
                    }
                }
                else if (canBeMovedTo){
                    allRowsOptions.add(stack);
                }
            }

        }
        return allRowsOptions;
    }

}
