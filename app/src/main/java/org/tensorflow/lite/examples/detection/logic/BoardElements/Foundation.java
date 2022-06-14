package org.tensorflow.lite.examples.detection.logic.BoardElements;

import java.util.LinkedList;
import java.util.List;

/**
 * Four foundation columns
 */
public class Foundation {
    private List<Card> hearts;
    private List<Card> diamonds;
    private List<Card> clovers;
    private List<Card> spades;

    public Foundation() {
        this.hearts = new LinkedList<>();
        this.diamonds = new LinkedList<>();
        this.clovers = new LinkedList<>();
        this.spades = new LinkedList<>();
    }


}
