package org.tensorflow.lite.examples.detection;

import static org.junit.Assert.*;

import org.junit.Test;

import org.tensorflow.lite.examples.detection.logic.Card;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Board{
    public List<List<Card>> coloumnCards = new LinkedList<>();
    public List<Card> deck = new LinkedList<>();
    public List<List<Card>> finishDeck = new LinkedList<>();
    private Integer nextDeckInt = 2;

    public Board(List<Card> row1, List<Card> row2, List<Card> row3, List<Card> row4, List<Card> row5, List<Card> row6, List<Card> row7,
                 List<Card> deck,
                 List<Card> finish1, List<Card> finish2, List<Card> finish3, List<Card> finish4) {
        this.coloumnCards.add(row1);
        this.coloumnCards.add(row2);
        this.coloumnCards.add(row3);
        this.coloumnCards.add(row4);
        this.coloumnCards.add(row5);
        this.coloumnCards.add(row6);
        this.coloumnCards.add(row7);
        this.deck = deck;
        this.finishDeck.add(finish1);
        this.finishDeck.add(finish2);
        this.finishDeck.add(finish3);
        this.finishDeck.add(finish4);
    }
    public List<Card> getRow(int x){
        return coloumnCards.get(x-1);
    }
    public Card getFrontOfRow( int x){
        if(!coloumnCards.get(x-1).isEmpty()){
            return coloumnCards.get(x-1).get(coloumnCards.get(x-1).size()-1);
        }
        return new Card("NA");
    }
    public Card nextDeckCard(){
        nextDeckInt += 3;
        if (nextDeckInt >= deck.size()){
            nextDeckInt = nextDeckInt - deck.size();
        }
        return deck.get(nextDeckInt);
    }

    public Card getCurrentDeckCard(){
        return deck.get(nextDeckInt);
    }

    public  void moveToFoundation(Card card){

    }

    public void PrintBoard(Board board){
        System.out.println("Round: " + DetectorActivity.roundCounterTest);
        for (int i = 0; i < 4; i++){
            if (!board.finishDeck.get(i).isEmpty()){
                System.out.print("f" + i + ": " + board.finishDeck.get(i).get(board.finishDeck.get(i).size()-1).getTitle() + "   ");
            }else{
                System.out.print("f" + i + ": ");
            }
        }
        System.out.println();
        System.out.print("Row1     row2     row3     row4     row5     row6     row7     " + "Deck: " + board.deck.get(nextDeckInt).getTitle());
        System.out.println();
        int biggestRow = 0;

        for (int k = 6; k >= 0; k--){
            if(biggestRow < board.coloumnCards.get(k).size()){
                biggestRow = board.coloumnCards.get(k).size();
            }
        }
        for(int j = 0; j < biggestRow; j++){
            for (int i = 0; i < 7; i++){
                try {
                    System.out.print(board.coloumnCards.get(i).get(j).getTitle() + "       ");
                } catch (Exception e){
                    System.out.print("         ");
                }
            }
            System.out.println();
        }

    }
}

class allCards{
    public Card sa = new Card("As");
    public Card s2 = new Card("2s");
    public Card s3 = new Card("3s");
    public Card s4 = new Card("4s");
    public Card s5 = new Card("5s");
    public Card s6 = new Card("6s");
    public Card s7 = new Card("7s");
    public Card s8 = new Card("8s");
    public Card s9 = new Card("9s");
    public Card s10 = new Card("10s");
    public Card sj = new Card("Js");
    public Card sq = new Card("Qs");
    public Card sk = new Card("Ks");

    public Card ha = new Card("Ah");
    public Card h2 = new Card("2h");
    public Card h3 = new Card("3h");
    public Card h4 = new Card("4h");
    public Card h5 = new Card("5h");
    public Card h6 = new Card("6h");
    public Card h7 = new Card("7h");
    public Card h8 = new Card("8h");
    public Card h9 = new Card("9h");
    public Card h10 = new Card("10h");
    public Card hj = new Card("Jh");
    public Card hq = new Card("Qh");
    public Card hk = new Card("Kh");

    public Card da = new Card("Ad");
    public Card d2 = new Card("2d");
    public Card d3 = new Card("3d");
    public Card d4 = new Card("4d");
    public Card d5 = new Card("5d");
    public Card d6 = new Card("6d");
    public Card d7 = new Card("7d");
    public Card d8 = new Card("8d");
    public Card d9 = new Card("9d");
    public Card d10 = new Card("10d");
    public Card dj = new Card("Jd");
    public Card dq = new Card("Qd");
    public Card dk = new Card("Kd");

    public Card ca = new Card("Ac");
    public Card c2 = new Card("2c");
    public Card c3 = new Card("3c");
    public Card c4 = new Card("4c");
    public Card c5 = new Card("5c");
    public Card c6 = new Card("6c");
    public Card c7 = new Card("7c");
    public Card c8 = new Card("8c");
    public Card c9 = new Card("9c");
    public Card c10 = new Card("10c");
    public Card cj = new Card("Jc");
    public Card cq = new Card("Qc");
    public Card ck = new Card("Kc");


}



public class DetectorActivityTest {

    @Test
    public void testGetCardNumber() {
        allCards t = new allCards();
        Card revealedCard = new Card("");
        List<Card> row1 = Stream.of(t.d4).collect(Collectors.toList());
        List<Card> row2 = Stream.of(t.d7, t.c9).collect(Collectors.toList());
        List<Card> row3 = Stream.of(t.d8, t.s7, t.c10).collect(Collectors.toList());
        List<Card> row4 = Stream.of(t.h2, t.d3, t.s2, t.h7).collect(Collectors.toList());
        List<Card> row5 = Stream.of(t.s9, t.da, t.s6, t.ca, t.h3).collect(Collectors.toList());
        List<Card> row6 = Stream.of(t.sq, t.h6, t.s10, t.hq, t.d6, t.s3).collect(Collectors.toList());
        List<Card> row7 = Stream.of(t.d5, t.dq, t.c4, t.c8, t.ha, t.c2, t.c7).collect(Collectors.toList());
        List<Card> deck = Stream.of(t.cj, t.hk, t.h5, t.d2, t.c6, t.s4, t.hj, t.s8, t.d10, t.s5, t.h8, t.h10, t.dk, t.h4, t.h9, t.d9, t.dq, t.sk, t.c3, t.c5, t.dj, t.sj, t.ck, t.sa).collect(Collectors.toList());
        List<Card> finishh = new LinkedList<Card>();
        List<Card> finishd = new LinkedList<Card>();
        List<Card> finishs = new LinkedList<Card>();
        List<Card> finishc = new LinkedList<Card>();

        Board board = new Board(row1, row2, row3, row4, row5, row6, row7, deck, finishh, finishd, finishs, finishc);
        board.PrintBoard(board);
        DetectorActivity activity = new DetectorActivity();
        activity.TESTMODE = true;
        activity.initializeCardColumns();
        for (int i = 0; i < 7; i++){
            Card frontOfRowCard = board.getFrontOfRow(i+1);
            if (!frontOfRowCard.getTitle().equals("NA")){
                activity.playGame(frontOfRowCard);
            }

        }
        activity.playGame(board.getCurrentDeckCard());
        int dowhile = 1;
        do {

            if (DetectorActivity.drawTest == true){
                board.nextDeckCard();
            }
            if (DetectorActivity.moveToFoundationTest == true){
                if (DetectorActivity.getCardColor(((LinkedList<Card>) DetectorActivity.fromTest).getLast()) == 'h'){
                    board.finishDeck.get(0).add(((LinkedList<Card>) DetectorActivity.fromTest).getLast());
                }else if (DetectorActivity.getCardColor(((LinkedList<Card>) DetectorActivity.fromTest).getLast()) == 'c'){
                    board.finishDeck.get(1).add(((LinkedList<Card>) DetectorActivity.fromTest).getLast());
                }else if (DetectorActivity.getCardColor(((LinkedList<Card>) DetectorActivity.fromTest).getLast()) == 'd'){
                    board.finishDeck.get(2).add(((LinkedList<Card>) DetectorActivity.fromTest).getLast());
                }else if (DetectorActivity.getCardColor(((LinkedList<Card>) DetectorActivity.fromTest).getLast()) == 's'){
                    board.finishDeck.get(3).add(((LinkedList<Card>) DetectorActivity.fromTest).getLast());
                }
                for (int i = 0; i < board.coloumnCards.size(); i++){
                    if (board.getFrontOfRow(i+1) == DetectorActivity.fromTest){
                        board.getRow(i+1).remove(board.coloumnCards.get(i).size()-1);
                        revealedCard = board.getFrontOfRow(i+1);
                    }
                }
            }
            if (DetectorActivity.moveCardColoumnTest == true){
                for (int i = 0; i < board.coloumnCards.size(); i++) {
                    if (board.getFrontOfRow(i + 1) == ((LinkedList<Card>) DetectorActivity.fromTest).getLast()) {
                        for (int j = 0; j <= DetectorActivity.fromTest.size()-1; j++){
                            board.getRow(i + 1).remove(board.coloumnCards.get(i).size() - 1);
                        }
                        revealedCard = board.getFrontOfRow(i + 1);
                    }
                    if (board.getFrontOfRow(i + 1) == DetectorActivity.toTest) {
                        for (int j = 0; j <= DetectorActivity.fromTest.size()-1; j++){
                            //board.getRow(i + 1).add(((LinkedList<Card>) DetectorActivity.from).getLast());
                            board.getRow(i + 1).add(DetectorActivity.fromTest.get(j));
                        }

                    }
                }
            }
            board.PrintBoard(board);
            if (DetectorActivity.pickupDeckCardTest == true){
                activity.playGame(board.getCurrentDeckCard());

            }else{
                if (!revealedCard.getTitle().equals("NA")){
                activity.playGame(revealedCard);
                }
            }
            if(DetectorActivity.moveCardTest == true){
                if(board.getCurrentDeckCard() == DetectorActivity.fromDeckTest){
                    for(int i = 0; i < board.coloumnCards.size(); i++){

                        if ((!board.getRow(i+1).isEmpty()) &&(board.getRow(i+1).get(board.getRow(i+1).size()-1) == DetectorActivity.toTest)){
                            board.getRow(i+1).add(DetectorActivity.fromDeckTest);
                            board.deck.remove(board.getCurrentDeckCard());
                            board.nextDeckCard();
                        }

                    }
                }
            }

            dowhile++;
        }while(dowhile <= 200);
        //test
        board.PrintBoard(board);


    }




    @Test
    public void recognizedCardsContains() {
        Card card = new Card("10h");
        DetectorActivity.recognizedCards.add(card);
        DetectorActivity.recognizedCards.add(new Card("11h"));
        DetectorActivity.recognizedCards.add(new Card("9s"));
        assertEquals(true, DetectorActivity.recognizedCardsContains(card));
        assertEquals(10, DetectorActivity.getCardNumber(card));
    }

    @Test
    public void getCardNumber() {
        Card card = new Card("10h");
        assertEquals(10, DetectorActivity.getCardNumber(card));
    }

    @Test
    public void isCardCanBeUsed() {
        Card card = new Card("10h");
        Card resultCard = new Card("9s");
        Card card2 = new Card("5s");
        assertEquals(true, DetectorActivity.isCardCanBeUsed(card, resultCard));
        assertEquals(false, DetectorActivity.isCardCanBeUsed(card2, resultCard));
    }


    @Test
    public void getCardColor() {
        Card card = new Card("10h");
        assertEquals('h', DetectorActivity.getCardColor(card));
    }


    @Test
    public void isDuplicateCard() {
        Card card = new Card("10h");
        Card card1 = new Card("5h");
        DetectorActivity.recognizedCards.add(card);
        DetectorActivity.recognizedCards.add(new Card("11h"));
        DetectorActivity.recognizedCards.add(new Card("9s"));
        assertEquals(card, DetectorActivity.isCardDuplicate(card));
        assertEquals(null, DetectorActivity.isCardDuplicate(card1));
    }

    @Test
    public void playGame() {
        Card card1 = new Card("4d");
        Card card2 = new Card("9c");
        Card card3 = new Card("10d");
        Card card4 = new Card("7h");
        Card card5 = new Card("3h");
        Card card6 = new Card("3s");
        Card card7 = new Card("7c");
        //fra stock
        Card card8 = new Card("Jc");
        DetectorActivity activity = new DetectorActivity();
        activity.TESTMODE = true;
        activity.initializeCardColumns();
        activity.playGame(card1);

       activity.playGame(card2);
        activity.playGame(card3);
        activity.playGame(card4);
        activity.playGame(card5);
        activity.playGame(card6);
        activity.playGame(card7);
        activity.playGame(card1);
        activity.playGame(card8);
    }
}