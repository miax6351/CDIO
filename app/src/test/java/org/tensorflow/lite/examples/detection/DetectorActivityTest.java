package org.tensorflow.lite.examples.detection;

import static org.junit.Assert.*;

import org.junit.Test;

import org.tensorflow.lite.examples.detection.logic.Card;

import java.util.LinkedList;


public class DetectorActivityTest {

    @Test
    public void testGetCardNumber() {
        Card card = new Card("10h");
        assertEquals(10, DetectorActivity.getCardNumber(card));
    }




    @Test
    public void recognizedCardsContains() {
        Card card = new Card("10h");
        DetectorActivity.recognizedCards.add(card);
        DetectorActivity.recognizedCards.add(new Card("11h"));
        DetectorActivity.recognizedCards.add(new Card("9s"));
        assertEquals(true, DetectorActivity.recognizedCardsContains(card));
        assertEquals(10, DetectorActivity.getCardNumber(card));
    }

    @Test
    public void getCardNumber() {
        Card card = new Card("10h");
        assertEquals(10, DetectorActivity.getCardNumber(card));
    }

    @Test
    public void isCardCanBeUsed() {
        Card card = new Card("10h");
        Card resultCard = new Card("9s");
        Card card2 = new Card("5s");
        assertEquals(true, DetectorActivity.isCardCanBeUsed(card, resultCard));
        assertEquals(false, DetectorActivity.isCardCanBeUsed(card2, resultCard));
    }


    @Test
    public void getCardColor() {
        Card card = new Card("10h");
        assertEquals('h', DetectorActivity.getCardColor(card));
    }


    @Test
    public void getCard() {
        Card card = new Card("10h");
        Card card1 = new Card("5h");
        DetectorActivity.recognizedCards.add(card);
        DetectorActivity.recognizedCards.add(new Card("11h"));
        DetectorActivity.recognizedCards.add(new Card("9s"));
        assertEquals(card, DetectorActivity.isCardDuplicate(card));
        assertEquals(null, DetectorActivity.isCardDuplicate(card1));
    }

    @Test
    public void playGame() {
        Card card1 = new Card("4d");
        Card card2 = new Card("9c");
        Card card3 = new Card("10c");
        Card card4 = new Card("7h");
        Card card5 = new Card("3h");
        Card card6 = new Card("3s");
        Card card7 = new Card("7c");
        //fra stock
        Card card8 = new Card("Jc");
        DetectorActivity activity = new DetectorActivity();
        activity.TESTMODE = true;
        activity.initializeCardColumns();
        activity.playGame(card1);

       activity.playGame(card2);
        activity.playGame(card3);
        activity.playGame(card4);
        activity.playGame(card5);
        activity.playGame(card6);
        activity.playGame(card7);
        activity.playGame(card1);
        activity.playGame(card8);
    }
}