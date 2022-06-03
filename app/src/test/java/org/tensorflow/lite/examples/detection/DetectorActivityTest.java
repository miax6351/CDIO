package org.tensorflow.lite.examples.detection;

import static org.junit.Assert.*;

import org.junit.Test;

import static org.junit.Assert.*;

import org.tensorflow.lite.examples.detection.DetectorActivity;
import org.tensorflow.lite.examples.detection.logic.Card;




public class DetectorActivityTest {

    @org.testng.annotations.Test
    public void testGetCardNumber() {
        Card card = new Card("10h");
        assertEquals(10, DetectorActivity.getCardNumber(card));
    }

    @org.junit.Test


    @org.testng.annotations.Test
    public void recognizedCardsContains() {
        Card card = new Card("10h");
        DetectorActivity.recognizedCards.add(card);
        DetectorActivity.recognizedCards.add(new Card("11h"));
        DetectorActivity.recognizedCards.add(new Card("9s"));
        assertEquals(true, DetectorActivity.recognizedCardsContains(card));
        assertEquals(10, DetectorActivity.getCardNumber(card));
    }

    @org.junit.Test
    @org.testng.annotations.Test
    public void getCardNumber() {
        Card card = new Card("10h");
        assertEquals(10, DetectorActivity.getCardNumber(card));
    }

    @org.junit.Test
    @org.testng.annotations.Test
    public void isCardCanBeUsed() {
        Card card = new Card("10h");
        Card resultCard = new Card("9s");
        Card card2 = new Card("5s");
        assertEquals(true, DetectorActivity.isCardCanBeUsed(card, resultCard));
        assertEquals(false, DetectorActivity.isCardCanBeUsed(card2, resultCard));
    }


    @org.junit.Test
    @org.testng.annotations.Test
    public void getCardColor() {
        Card card = new Card("10h");
        assertEquals('h', DetectorActivity.getCardColor(card));
    }


    @org.junit.Test
    @org.testng.annotations.Test
    public void getCard() {
        Card card = new Card("10h");
        Card card1 = new Card("5h");
        DetectorActivity.recognizedCards.add(card);
        DetectorActivity.recognizedCards.add(new Card("11h"));
        DetectorActivity.recognizedCards.add(new Card("9s"));
        assertEquals(card, DetectorActivity.getCard(card));
        assertEquals(null, DetectorActivity.getCard(card1));
    }
}