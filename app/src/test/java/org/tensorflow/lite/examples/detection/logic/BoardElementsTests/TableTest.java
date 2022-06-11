package org.tensorflow.lite.examples.detection.logic.BoardElementsTests;

import static org.junit.Assert.*;

import com.google.android.material.tabs.TabLayout;

import org.junit.Test;
import org.tensorflow.lite.examples.detection.logic.BoardElements.Deck;
import org.tensorflow.lite.examples.detection.logic.BoardElements.Stack;
import org.tensorflow.lite.examples.detection.logic.BoardElements.Table;
import org.tensorflow.lite.examples.detection.logic.Card;

import java.util.ArrayList;
import java.util.List;

public class TableTest {
    Table table = new Table();
    Deck deck = new Deck();
    public TableTest(){
        deck.buildDeck();

        for (int i = 0; i < 7; i++){
            Stack stack = table.getRows()[i];
            for (int j = 0; j < i + 1; j++){
                if (i == j){

                    stack.addCard(deck.getCard(i));
                    stack.getCards().get(i).setFaceUp(true);
                }else{
                    stack.addCard(new Card(null));
                }
            }
        }

    }

    @Test
    public void getRows() {
        table.getRows();


    }

    @Test
    public void getRow() {
    }

    @Test
    public void getOneEmptyStack() {
    }

    @Test
    public void isRowEmpty() {
    }

    @Test
    public void getAllMoveOptions() {
    }
}