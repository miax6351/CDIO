package org.tensorflow.lite.examples.detection;

import static org.junit.Assert.*;

import org.tensorflow.lite.examples.detection.logic.Card;
import org.testng.annotations.Test;



public class DetectorActivityTest {
    @org.junit.Test


    @Test
    public void recognizedCardsContains() {
        Card card = new Card("h10");
        DetectorActivity.recognizedCards.add(card);
        DetectorActivity.recognizedCards.add(new Card("h11"));
        DetectorActivity.recognizedCards.add(new Card("s9"));
        assertEquals(true, DetectorActivity.recognizedCardsContains(card));
    }

    @Test
    public void print(){
        assertEquals(true, true);
    }
}