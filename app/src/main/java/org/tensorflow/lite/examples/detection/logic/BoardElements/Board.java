package org.tensorflow.lite.examples.detection.logic.BoardElements;

import java.util.ArrayList;
import java.util.List;

/**
 * This class keeps track of all board elements within a game of solitaire.
 */

public class Board {

    private Table table;
    private Stock stock;
    private Foundation foundation;
    private Talon talon;
    private List<Card> knownStockTalon = new ArrayList<>();
    private List<Card> recognizedCards = new ArrayList<>();


    public Board startGameSetup(List<Card> firstCards){
        Deck deck = new Deck();
        List<Card> cards = deck.cardsKnownStartGame(firstCards);

        this.setTable(new Table());

        this.setFoundation(new Foundation());

        this.setTalon(new Talon());

        for (int i = 0; i < 7; i++){
            Stack stack = this.getTable().getColumns()[i];
            for (int j = 0; j< i+ 1; j++){
                if (j ==i){
                    stack.addCard(cards.get(i));
                    stack.getCards().get(i).setFaceUp(true);
                }else{
                    stack.addCard(new Card(null));
                }
            }
        }
        this.knownStockTalon = new ArrayList<>();
        this.setStock(new Stock());
        for (int i = 0; i < 24; i++){
            this.getStock().addCard(new Card(null));
        }
        return  this;
    }

    public void setTable(Table table){
        this.table = table;
    }
    public void setStock(Stock stock){
        this.stock = stock;
    }
    public void setFoundation(Foundation foundation){
        this.foundation = foundation;
    }
    public void setTalon(Talon talon){
        this.talon = talon;
    }
    public void setKnownStockTalon(List<Card> stockTalon){
        this.knownStockTalon = stockTalon;
    }
    public Table getTable(){
        return table;
    }
    public Stack getStock(){
        return stock;
    }
    public Foundation getFoundation(){
        return foundation;
    }
    public Talon getTalon(){
        return talon;
    }
    public List<Card> getKnownStockTalon(){
        return knownStockTalon;
    }
    public List<Card> addRecognizedCards(List<Card> cards){
        for (int i = 0; i < cards.size()-1; i++) {
            recognizedCards.add(cards.get(i)) ;
        }


    }


}
