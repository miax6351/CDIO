package org.tensorflow.lite.examples.detection.logic;

import java.util.LinkedList;
import java.util.ListIterator;

public class Tableau {

    private static Tableau instance;

    private LinkedList[] cardColumns = null;
    private LinkedList recognizedCards = new LinkedList<Card>();

    private Tableau() {
    }

    public static Tableau getInstance(){
        if (instance == null)
            instance = new Tableau();

        return instance;
    }

    public boolean contains(Card card) {
        ListIterator listIterator = recognizedCards.listIterator();
        while (listIterator.hasNext()) {
            Card columnCard =  (Card)listIterator.next();
            if (columnCard.getTitle().equals(card.getTitle())) {
                return true;
            }
        }
        return false;
    }

    public LinkedList[] getAllCardColumns(){
        if (cardColumns == null){
            cardColumns = new LinkedList[7];
            for (int i = 0; i < 7; i++) {
                cardColumns[i] = new LinkedList<Card>();
            }
        }
        return cardColumns;
    }

    public void addKnownCard(Card card){
        recognizedCards.add(card);
    }

    public LinkedList getCards(){
        return recognizedCards;
    }

    public void removeCard(Card card){
        boolean breakLoop = false;
        recognizedCards.remove(card);
        for (int i = 0; i < cardColumns.length; i++){
            for (int j = 0; j < cardColumns[i].size(); j++){
                if (card.getTitle().equals(cardColumns[i].get(j))){
                    cardColumns[i].remove(cardColumns[i].get(j));
                    breakLoop = true;
                    break;
                }

            }
            if (breakLoop)
                break;
        }
    }
}
