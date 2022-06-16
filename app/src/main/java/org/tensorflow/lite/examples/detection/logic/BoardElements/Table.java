package org.tensorflow.lite.examples.detection.logic.BoardElements;

import java.util.ArrayList;
import java.util.List;

/**
 * Seven piles that make up the main table
 * Stack position range from 0-6, Talon position is 10, Stock position is 15.
 */

public class Table {
    private Stack[] columns = new Stack[7];
    private ArrayList<Integer> emptyColumns = new ArrayList<>();

    public Table(){
        for (int i = 0; i <7; i++){
            columns[i] = new Stack(i);
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
            if (columns[i].containsKing()){
                return i;
            }
        }
        return -1;
    }
    public int getBiggestPileOfDownCards(){
        int highestNumberOfHiddenCards = 0;
        int index = 0;
        for (int i = 0; i < columns.length-1; i++) {
            int numberOfHiddenCards = columns[i].getHiddenCards();
            if (numberOfHiddenCards > highestNumberOfHiddenCards){
                highestNumberOfHiddenCards = numberOfHiddenCards;
                index = i;
            }
        }
        return index;
    }

    //returns a list of integers of all the coloumns which contains ace or two's.
    public List<Integer> getColumnsWithAceOrDeuce(){
        List<Integer> indexes = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            if (columns[i].containsAceOrDeuce()){
                indexes.add(i);
            }
        }
            return indexes;
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
