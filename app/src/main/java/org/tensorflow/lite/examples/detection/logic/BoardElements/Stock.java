package org.tensorflow.lite.examples.detection.logic.BoardElements;

import org.tensorflow.lite.examples.detection.logic.Card;

import java.util.ArrayList;
import java.util.List;

/**
 * entire pack that is not laid out in a tableau at the beginning of a game
 */
public class Stock extends Stack{

    List<Card> stock;

    public Stock(){
        this.stock = new ArrayList<>();
    }



    //for testing:
    public void setStock(List<Card> cardList){
        for (Card card : cardList){
            this.stock.add(card);
        }
    }
}
