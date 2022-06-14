package org.tensorflow.lite.examples.detection.logic.moves;

import org.tensorflow.lite.examples.detection.logic.BoardElements.Deck;
import org.tensorflow.lite.examples.detection.logic.BoardElements.Foundation;
import org.tensorflow.lite.examples.detection.logic.BoardElements.Stack;
import org.tensorflow.lite.examples.detection.logic.BoardElements.Stock;
import org.tensorflow.lite.examples.detection.logic.BoardElements.Table;
import org.tensorflow.lite.examples.detection.logic.BoardElements.Talon;
import org.tensorflow.lite.examples.detection.logic.BoardElements.Card;

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


    public static Board startGameSetup(List<Card> firstCards){
        Deck deck = new Deck();
        List<Card> cards = deck.cardsKnownStartGame(firstCards);

        Board currentBoard = new Board();

        currentBoard.setTable(new Table());

        currentBoard.setFoundation(new Foundation());

        currentBoard.setTalon(new Talon());

        for (int i = 0; i < 7; i++){
            Stack stack = currentBoard.getTable().getRows()[i];
            for (int j = 0; j< i+ 1; j++){
                if (j ==i){
                    stack.addCard(cards.get(i));
                    stack.getCards().get(i).setFaceUp(true);
                }else{
                    stack.addCard(new Card(null));
                }
            }
        }
        currentBoard.knownStockTalon = new ArrayList<>();
        currentBoard.setStock(new Stock());
        for (int i = 0; i < 24; i++){
            currentBoard.getStock().addCard(new Card(null));
        }
        return  currentBoard;
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


}
