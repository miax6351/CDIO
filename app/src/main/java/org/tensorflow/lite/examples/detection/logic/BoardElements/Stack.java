package org.tensorflow.lite.examples.detection.logic.BoardElements;

import org.tensorflow.lite.examples.detection.logic.Card;

import java.util.ArrayList;
import java.util.List;

public class Stack {

    private List<Card> cards;

    public Stack(){
        this.cards = new ArrayList<>();
    }

    public void addCards(List<Card> addCards){
        for (Card card : addCards){
            cards.add(card);
        }
    }

    public void removeCards(List<Card> removeCards){
        for (Card card : removeCards){
            cards.remove(card);
        }
    }
    public Card getNorthMostCard(){
        if (cards.isEmpty()){
            return null;
        }
        return cards.get(0);
    }
    public Card getSouthMostCard(){
        if (cards.isEmpty()){
            return null;
        }
        return cards.get(cards.size()-1);
    }
    public void removeSouthCard(){
        Card card = getNorthMostCard();
        if (card == null){
            System.out.println("Pile is probably empty, can't remove card");
        }
        cards.remove(card);
    }
    public void addCard(Card card){
        cards.add(card);
    }
    public Card getCard(int i){
        return cards.get(i);
    }
    public List<Card> getCards(){
        return cards;
    }

    public void setCards(List<Card> cards){
        this.cards = cards;
    }

    public Boolean isEmpty(){
        return cards.isEmpty();
    }

}
