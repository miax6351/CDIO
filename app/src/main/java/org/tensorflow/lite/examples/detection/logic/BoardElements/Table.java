package org.tensorflow.lite.examples.detection.logic.BoardElements;

import java.util.ArrayList;
import java.util.List;

/**
 * Seven piles that make up the main table
 */

public class Table {
    private Stack[] columns = new Stack[7];
    private ArrayList<Integer> emptyColumns = new ArrayList<>();

    public Table(){
        for (int i = 0; i <7; i++){
            columns[i] = new Stack();
        }
    }
    public void setStacks(Stack[] stacks){
        this.columns = stacks;
    }
    public Stack[] getColumns(){
        return columns;
    }
    public Stack getColumn(int i){
        return columns[i];
    }

    public Stack getOneEmptyStack(){
        for (int i = 0; i < 7; i++){
            if (columns[i].isEmpty()){
                return columns[i];
            }
        }
        return null;
    }
    public Boolean isRowEmpty(Stack row){
        return row.isEmpty();
    }

    public Boolean containsEmptyColumn(){
        for (int i = 0; i < columns.length-1; i++) {
            if (columns[i].isEmpty()){
                emptyColumns.add(i);
                return true;
            }
        }
        return false;
    }

    public int containsKing(){
        for (int i = 0; i < columns.length-1; i++) {
            if (columns[i].isEmpty()){
                emptyColumns.add(i);
                return i;
            }
        }
        return -1;
    }

    //get all move options for a specific card from row to row.
    public List<Stack> getAllMoveOptions(Card cardFrom, Stack stackFrom){
        List<Stack> allRowsOptions = new ArrayList<>();


        for (Stack stack : columns){
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
