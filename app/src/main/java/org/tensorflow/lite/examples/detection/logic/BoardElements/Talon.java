package org.tensorflow.lite.examples.detection.logic.BoardElements;

import org.tensorflow.lite.examples.detection.logic.Card;

/**
 * Cards from the stock pile that have no place in the tableau or on foundations are laid face up in the waste pile
 */
public class Talon extends Stack {

    //gets the top card of the talon and removes it much like a draw.
    public Card getTopCard(){
        return cards.remove(cards.size()-1);
    }

}
