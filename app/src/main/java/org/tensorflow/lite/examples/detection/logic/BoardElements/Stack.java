package org.tensorflow.lite.examples.detection.logic.BoardElements;

import java.util.ArrayList;
import java.util.List;

/**
 * Stack is a pile of cards so as long as it's more than one card it is a stack.
 */
public class Stack {
    protected int position;
    protected List<Card> cards;

    public Stack(int position){
        this.position = position;
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
    public Boolean containsKing(){
        for (int i = 0; i <cards.size(); i++) {
            if(cards.get(i).getCardNumber() == 'K'){
                return true;
            }
        }
        return false;
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

    public int getPosition() {
        return position;
    }
}
