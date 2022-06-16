package org.tensorflow.lite.examples.detection.logic.BoardElements;

import java.util.LinkedList;
import java.util.List;

/**
 * Four foundation columns
 */
public class Foundation {
    private Stack hearts;
    private Stack diamonds;
    private Stack clovers;
    private Stack spades;

    public Foundation() {
        this.hearts = new Stack(11);
        this.diamonds = new Stack(12);
        this.clovers = new Stack(13);
        this.spades = new Stack(14);
    }

    public Stack getHearts() {
        return hearts;
    }

    public Stack getDiamonds() {
        return diamonds;
    }

    public Stack getClovers() {
        return clovers;
    }

    public Stack getSpades() {
        return spades;
    }

    public void addCardHearts(Card card){
        this.hearts.addCard(card);
    }

    public void addCardDiamonds(Card card) {
        this.diamonds.addCard(card);
    }

    public void addCardClovers(Card card) {
        this.clovers.addCard(card);
    }

    public void addCardSpades(Card card) {
        this.spades.addCard(card);
    }
}
