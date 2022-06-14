package org.tensorflow.lite.examples.detection;

import org.tensorflow.lite.examples.detection.logic.BoardElements.Card;

import java.util.LinkedList;

public class ChangeCard {

    public static void Change(LinkedList<Card> Columns, Card wrongCard, String correctCard){

        for (Card c:Columns
             ) {
            if(c.getTitle().equals(wrongCard.getTitle())){
                c.fixCard(correctCard);
            }
        }

    }

}
