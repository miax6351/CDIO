package org.tensorflow.lite.examples.detection.logic;

import java.util.Iterator;
import java.util.LinkedList;

public class CardEditor {

    public static void EditCard(LinkedList[] Editcolumns, Card Editcard, String Newcard, boolean isSuit) {

        for (LinkedList<Card> e : Editcolumns) {

            for (Card card : e) {
                if (Editcard.getTitle().equals(card.getTitle())) {
                    if (isSuit){
                        card.fixSuit(Newcard);
                    }
                    else{
                        card.fixRank(Newcard);
                    }
                }
            }
        }
    }

    public static void RemoveCard(LinkedList[] Removecolumns, Card Removecard) {

        for (LinkedList e : Removecolumns) {

            for (Iterator<Card> iterator = e.iterator(); iterator.hasNext();){
                String title = iterator.next().getTitle();
                if (Removecard.getTitle().equals(title)){
                    iterator.remove();
                }
            }
        }
    }
}