package org.tensorflow.lite.examples.detection.logic;

import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;

public class CardTest {
    private Card card;

    private LinkedList<Card> recognizedCards;

    @Before
    public void before(){
        card = new Card("5D");
        recognizedCards = new LinkedList<>();
        recognizedCards.add(card);
        recognizedCards.add(new Card("10S"));
    }

    @Test
    public void fixCard() {
        assertEquals("5D",card.getTitle());
        assertEquals("5D", recognizedCards.get(0).getTitle());
        card.fixCard("3S");
        assertEquals("3S",card.getTitle());
        assertEquals("3S",recognizedCards.get(0).getTitle());
    }

    @Test
    public void getTitle(){
        assertEquals("5D",card.getTitle());
    }

    @Test
    public void getRank(){
        Card card1 = new Card("10C");

        assertEquals("5",card.getRank());
        assertEquals("10",card1.getRank());
    }
    @Test
    public void getSuit(){
        Card card1 = new Card("10C");
        assertEquals("D",card.getSuit());
        assertEquals("C",card1.getSuit());
    }
}