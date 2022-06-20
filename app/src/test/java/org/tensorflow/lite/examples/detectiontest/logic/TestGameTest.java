package org.tensorflow.lite.examples.detectiontest.logic;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import org.tensorflow.lite.examples.detection.logic.Card;
import org.tensorflow.lite.examples.detection.logic.SOLITARE_STATES;
import org.tensorflow.lite.examples.detectiontest.logic.TestGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Board{
    public List<List<Card>> coloumnCards = new LinkedList<>();
    public List<Card> deck = new LinkedList<>();
    public List<List<Card>> finishDeck = new LinkedList<>();
    private Integer nextDeckInt = 2;
    public int forceMove = 0;


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
        //hvis der er mere end 3 kort i decket:
        if (!(deck.size() < 2)){
            nextDeckInt += 3;
            if (nextDeckInt >= deck.size()){
                nextDeckInt = nextDeckInt - deck.size();
            }
            return deck.get(nextDeckInt);
            //Hvis der er mindre end 3 kort i decket skal den ikke kunne draw et nyt ved mindre den ligger et kort
        }else{
            nextDeckInt = deck.size()-1;
            return deck.get(nextDeckInt);
        }

    }

    public Card getCurrentDeckCard(){
        if (!deck.isEmpty()){
            return deck.get(nextDeckInt);
        }
        Card card = new Card("WA");
        return card;
    }

    public void setCurrentCardDeckAferRemove(){
        //Hvis decket er tomt
        if (deck.isEmpty()){
            TestGame.pickupDeckCardTest = false;
            return;
        }

        //hvis det øverste kort fjernes skal man bare trække en ny fra bunken
        if (nextDeckInt == 0){
            nextDeckCard();
        }else{
            nextDeckInt--;
        }

    }

    public void PrintBoard(Board board){
        System.out.println("Round: " + TestGame.roundCounterTest);
        for (int i = 0; i < 4; i++){
            if (!board.finishDeck.get(i).isEmpty()){
                System.out.print("f" + i + ": " + board.finishDeck.get(i).get(board.finishDeck.get(i).size()-1).getTitle() + "   ");
            }else{
                System.out.print("f" + i + ": ");
            }
        }
        System.out.println();
        if (board.deck.isEmpty()){
            System.out.print("Row1     row2     row3     row4     row5     row6     row7     " + "Deck: " + "empty");
        }else{
            System.out.print("Row1     row2     row3     row4     row5     row6     row7     " + "Deck: " + board.deck.get(nextDeckInt).getTitle());
        }
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
class MultipleSets{
    public List<Card> allCards = new LinkedList<>();

    public MultipleSets(){
        allCards t = new allCards();
        this.allCards = Stream.of(t.ca, t.c2, t.c3, t.c4, t.c5, t.c6, t.c7, t.c8, t.c9, t.c10, t.cj, t.cq, t.ck,
                t.da, t.d2, t.d3, t.d4, t.d5, t.d6, t.d7, t.d8, t.d9, t.d10, t.dj, t.dq, t.dk,
                t.ha, t.h2, t.h3, t.h4, t.h5, t.h6, t.h7, t.h8, t.h9, t.h10, t.hj, t.hq, t.hk,
                t.sa, t.s2, t.s3, t.s4, t.s5, t.s6, t.s7, t.s8, t.s9, t.s10, t.sj, t.sq, t.sk).collect(Collectors.toList());

    }
    public Card chooseCardAndRemove(){
        Random ran = new Random();
        int random = ran.nextInt(allCards.size());
        Card cardToReturn = allCards.get(random);
        allCards.remove(random);
        return cardToReturn;
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


public class TestGameTest {

    @Test
    public void playGame() {
        allCards t = new allCards();
        Card revealedCard = new Card("");
        /*List<Card> row1 = Stream.of(t.d4).collect(Collectors.toList());
        List<Card> row2 = Stream.of(t.d7, t.c9).collect(Collectors.toList());
        List<Card> row3 = Stream.of(t.d8, t.s7, t.c10).collect(Collectors.toList());
        List<Card> row4 = Stream.of(t.h2, t.d3, t.s2, t.h7).collect(Collectors.toList());
        List<Card> row5 = Stream.of(t.s9, t.da, t.s6, t.ca, t.h3).collect(Collectors.toList());
        List<Card> row6 = Stream.of(t.sq, t.h6, t.s10, t.hq, t.d6, t.s3).collect(Collectors.toList());
        List<Card> row7 = Stream.of(t.d5, t.cq, t.c4, t.c8, t.ha, t.c2, t.c7).collect(Collectors.toList());
        List<Card> deck = Stream.of(t.cj, t.hk, t.h5, t.d2, t.c6, t.s4, t.hj, t.s8, t.d10, t.s5, t.h8, t.h10, t.dk, t.h4, t.h9, t.d9, t.dq, t.sk, t.c3, t.c5, t.dj, t.sj, t.ck, t.sa).collect(Collectors.toList());
        */
        List<Card> row1 = Stream.of(t.cq).collect(Collectors.toList());
        List<Card> row2 = Stream.of(t.s4, t.ca).collect(Collectors.toList());
        List<Card> row3 = Stream.of(t.s10, t.ha, t.sa).collect(Collectors.toList());
        List<Card> row4 = Stream.of(t.c9, t.h7, t.d9, t.c6).collect(Collectors.toList());
        List<Card> row5 = Stream.of(t.s3, t.d10, t.h10, t.c7, t.h5).collect(Collectors.toList());
        List<Card> row6 = Stream.of(t.h8, t.dj, t.c10, t.h3, t.c8, t.s9).collect(Collectors.toList());
        List<Card> row7 = Stream.of(t.dq, t.cj, t.d4, t.d3, t.d2, t.s2, t.c2).collect(Collectors.toList());
        List<Card> deck = Stream.of(t.h4, t.c3, t.h2, t.dk, t.sj, t.sq, t.hq, t.c4, t.d6, t.c5, t.s8, t.sk, t.hk, t.d5, t.h9, t.s5, t.d7, t.hj, t.s7, t.d8, t.da, t.h6, t.s6, t.ck).collect(Collectors.toList());
        List<Card> finishh = new LinkedList<Card>();
        List<Card> finishd = new LinkedList<Card>();
        List<Card> finishs = new LinkedList<Card>();
        List<Card> finishc = new LinkedList<Card>();

        Board board = new Board(row1, row2, row3, row4, row5, row6, row7, deck, finishh, finishd, finishs, finishc);
        board.PrintBoard(board);
        TestGame activity = new TestGame();
        activity.TESTMODE = true;
        activity.initializeCardColumns();

        for (int i = 0; i < 7; i++){
            Card frontOfRowCard = board.getFrontOfRow(i+1);
            if (!frontOfRowCard.getTitle().equals("NA")){
                activity.playGame(frontOfRowCard);
            }

        }
        //activity.playGame(board.getCurrentDeckCard());
        int dowhile = 1;
        do {

            if (TestGame.drawTest == true){
                board.nextDeckCard();
            }
            if (TestGame.moveToFoundationTest == true){
                if (TestGame.getCardColor(((LinkedList<Card>) TestGame.fromTest).getLast()) == 'h'){
                    board.finishDeck.get(0).add(((LinkedList<Card>) TestGame.fromTest).getLast());
                }else if (TestGame.getCardColor(((LinkedList<Card>) TestGame.fromTest).getLast()) == 'c'){
                    board.finishDeck.get(1).add(((LinkedList<Card>) TestGame.fromTest).getLast());
                }else if (TestGame.getCardColor(((LinkedList<Card>) TestGame.fromTest).getLast()) == 'd'){
                    board.finishDeck.get(2).add(((LinkedList<Card>) TestGame.fromTest).getLast());
                }else if (TestGame.getCardColor(((LinkedList<Card>) TestGame.fromTest).getLast()) == 's'){
                    board.finishDeck.get(3).add(((LinkedList<Card>) TestGame.fromTest).getLast());
                }
                for (int i = 0; i < board.coloumnCards.size(); i++){
                    if (board.getFrontOfRow(i+1).equals(((LinkedList<Card>) TestGame.fromTest).getLast())){
                        board.getRow(i+1).remove(board.coloumnCards.get(i).size()-1);
                        revealedCard = board.getFrontOfRow(i+1);
                    }
                }
                if (board.getCurrentDeckCard().equals(((LinkedList<Card>) TestGame.fromTest).getLast())){
                    board.deck.remove(board.getCurrentDeckCard());
                    //if (!board.deck.isEmpty()){
                    board.setCurrentCardDeckAferRemove();
                    revealedCard = board.getCurrentDeckCard();
                    //}
                }
                TestGame.moveToFoundationTest = false;
            }
            if (TestGame.moveCardColoumnTest == true){
                for (int i = 0; i < board.coloumnCards.size(); i++) {
                    if (board.getFrontOfRow(i + 1) == ((LinkedList<Card>) TestGame.fromTest).getLast()) {
                        for (int j = 0; j <= TestGame.fromTest.size()-1; j++){
                            board.getRow(i + 1).remove(board.coloumnCards.get(i).size() - 1);
                        }
                        revealedCard = board.getFrontOfRow(i + 1);
                    }
                    if (board.getFrontOfRow(i + 1) == TestGame.toTest) {
                        for (int j = 0; j <= TestGame.fromTest.size()-1; j++){
                            //board.getRow(i + 1).add(((LinkedList<Card>) DetectorActivity.from).getLast());
                            board.getRow(i + 1).add(TestGame.fromTest.get(j));
                        }
                    }
                }
                if (board.getCurrentDeckCard().equals(((LinkedList<Card>) TestGame.fromTest).getLast())){
                    board.deck.remove(board.getCurrentDeckCard());
                    //if (!board.deck.isEmpty()){
                    board.setCurrentCardDeckAferRemove();
                    revealedCard = board.getCurrentDeckCard();
                    //}
                }
            }
            board.PrintBoard(board);
            if (TestGame.pickupDeckCardTest == true){
                if (deck.isEmpty()){
                    TestGame.pickupDeckCardTest = false;
                    TestGame.gameState = SOLITARE_STATES.ANALYZE_CARD_MOVE;
                }else{
                    activity.playGame(board.getCurrentDeckCard());
                }


            }else{
                if (!revealedCard.getTitle().equals("NA")){
                    activity.playGame(revealedCard);
                }else{
                    //Dette bliver brugt når ingen ting ændrer sig i et stykke tid derfor kaldes playGame med et tilfældigt kort så
                    //main logikken selv skubber spillet videre ved at trække et nyt deck kort.
                    activity.playGame(((Card) TestGame.recognizedCards.getLast()));
                }
            }
            if(TestGame.moveCardTest == true){
                if(board.getCurrentDeckCard() == TestGame.fromDeckTest){
                    for(int i = 0; i < board.coloumnCards.size(); i++){

                        if ((!board.getRow(i+1).isEmpty()) &&(board.getRow(i+1).get(board.getRow(i+1).size()-1) == TestGame.toTest)){
                            board.getRow(i+1).add(TestGame.fromDeckTest);
                            board.deck.remove(board.getCurrentDeckCard());
                            board.setCurrentCardDeckAferRemove();
                        }
                        else if(TestGame.toEmptyTest != -1){
                            if (board.getRow(i+1).isEmpty()){
                                board.getRow(i+1).add(TestGame.fromDeckTest);
                                board.deck.remove(board.getCurrentDeckCard());
                                board.setCurrentCardDeckAferRemove();
                                TestGame.toEmptyTest = -1;
                            }
                        }
                    }
                }
            }

            dowhile++;
        }while(dowhile <= 500);
        //test
        board.PrintBoard(board);
    }

    @Test
    public void inserDeck(){
        allCards t = new allCards();
        Card revealedCard = new Card("");

        List<Card> insertList = new ArrayList<>();
        insertList = Stream.of(t.hk, t.s6, t.h6, t.s7, t.d8, t.da, t.hj, t.d7, t.s5, t.h9, t.d5, t.ck, t.sk, t.s8, t.c5, t.d6, t.c4, t.hq, t.sq, t.sj, t.dk, t.h2, t.c3, t.h4, t.c2, t.s2, t.s9, t.d2, t.c8, t.h5, t.d3, t.h3, t.c7, t.c6, t.d4, t.c10,t.h10,t.d9,t.sa,t.cj,t.dj,t.d10,t.h7,t.ha,t.ca,t.dq,t.h8,t.s3,t.c9,t.s10,t.s4,t.cq).collect(Collectors.toList());
        Collections.reverse(insertList);
       /* for (Card card : insertList){
            System.out.print(card.getTitle() + " ");
        }*/

        List<Card> row1 = Stream.of(insertList.get(0)).collect(Collectors.toList());
        List<Card> row2 = Stream.of(insertList.get(1), insertList.get(7)).collect(Collectors.toList());
        List<Card> row3 = Stream.of(insertList.get(2), insertList.get(8), insertList.get(13)).collect(Collectors.toList());
        List<Card> row4 = Stream.of(insertList.get(3),insertList.get(9),insertList.get(14),insertList.get(18)).collect(Collectors.toList());
        List<Card> row5 = Stream.of(insertList.get(4),insertList.get(10),insertList.get(15),insertList.get(19),insertList.get(22)).collect(Collectors.toList());
        List<Card> row6 = Stream.of(insertList.get(5),insertList.get(11),insertList.get(16),insertList.get(20),insertList.get(23),insertList.get(25)).collect(Collectors.toList());
        List<Card> row7 = Stream.of(insertList.get(6),insertList.get(12),insertList.get(17),insertList.get(21),insertList.get(24),insertList.get(26),insertList.get(27)).collect(Collectors.toList());
        List<Card> deck = Stream.of(insertList.get(28),insertList.get(29),insertList.get(30),insertList.get(31),insertList.get(32),insertList.get(33),insertList.get(34),insertList.get(35),insertList.get(36),insertList.get(37),insertList.get(38),insertList.get(39),insertList.get(40),insertList.get(41),insertList.get(42),insertList.get(43),insertList.get(44),insertList.get(45),insertList.get(46),insertList.get(47),insertList.get(48),insertList.get(49),insertList.get(50),insertList.get(51)).collect(Collectors.toList());
        List<Card> finishh = new LinkedList<Card>();
        List<Card> finishd = new LinkedList<Card>();
        List<Card> finishs = new LinkedList<Card>();
        List<Card> finishc = new LinkedList<Card>();

        Board board = new Board(row1, row2, row3, row4, row5, row6, row7, deck, finishh, finishd, finishs, finishc);
        board.PrintBoard(board);
        System.out.print("Deck: ");
        for (Card card : deck){
            System.out.print(card.getTitle() + " ");
        }
        TestGame activity = new TestGame();
        activity.TESTMODE = true;
        activity.initializeCardColumns();

        for (int i = 0; i < 7; i++){
            Card frontOfRowCard = board.getFrontOfRow(i+1);
            if (!frontOfRowCard.getTitle().equals("NA")){
                activity.playGame(frontOfRowCard);
            }

        }
        //activity.playGame(board.getCurrentDeckCard());
        int dowhile = 1;
        do {

            if (TestGame.drawTest == true){
                board.nextDeckCard();
            }
            if (TestGame.moveToFoundationTest == true){
                if (TestGame.getCardColor(((LinkedList<Card>) TestGame.fromTest).getLast()) == 'h'){
                    board.finishDeck.get(0).add(((LinkedList<Card>) TestGame.fromTest).getLast());
                }else if (TestGame.getCardColor(((LinkedList<Card>) TestGame.fromTest).getLast()) == 'c'){
                    board.finishDeck.get(1).add(((LinkedList<Card>) TestGame.fromTest).getLast());
                }else if (TestGame.getCardColor(((LinkedList<Card>) TestGame.fromTest).getLast()) == 'd'){
                    board.finishDeck.get(2).add(((LinkedList<Card>) TestGame.fromTest).getLast());
                }else if (TestGame.getCardColor(((LinkedList<Card>) TestGame.fromTest).getLast()) == 's'){
                    board.finishDeck.get(3).add(((LinkedList<Card>) TestGame.fromTest).getLast());
                }
                for (int i = 0; i < board.coloumnCards.size(); i++){
                    if (board.getFrontOfRow(i+1).equals(((LinkedList<Card>) TestGame.fromTest).getLast())){
                        board.getRow(i+1).remove(board.coloumnCards.get(i).size()-1);
                        revealedCard = board.getFrontOfRow(i+1);
                    }
                }
                if (board.getCurrentDeckCard().equals(((LinkedList<Card>) TestGame.fromTest).getLast())){
                    board.deck.remove(board.getCurrentDeckCard());
                    //if (!board.deck.isEmpty()){
                    board.setCurrentCardDeckAferRemove();
                    revealedCard = board.getCurrentDeckCard();
                    //}
                }
                TestGame.moveToFoundationTest = false;
            }
            if (TestGame.moveCardColoumnTest == true){
                for (int i = 0; i < board.coloumnCards.size(); i++) {
                    if (board.getFrontOfRow(i + 1) == ((LinkedList<Card>) TestGame.fromTest).getLast()) {
                        for (int j = 0; j <= TestGame.fromTest.size()-1; j++){
                            board.getRow(i + 1).remove(board.coloumnCards.get(i).size() - 1);
                        }
                        revealedCard = board.getFrontOfRow(i + 1);
                    }
                    if (board.getFrontOfRow(i + 1) == TestGame.toTest) {
                        for (int j = 0; j <= TestGame.fromTest.size()-1; j++){
                            //board.getRow(i + 1).add(((LinkedList<Card>) DetectorActivity.from).getLast());
                            board.getRow(i + 1).add(TestGame.fromTest.get(j));
                        }
                    }
                }
                if (board.getCurrentDeckCard().equals(((LinkedList<Card>) TestGame.fromTest).getLast())){
                    board.deck.remove(board.getCurrentDeckCard());
                    //if (!board.deck.isEmpty()){
                    board.setCurrentCardDeckAferRemove();
                    revealedCard = board.getCurrentDeckCard();
                    //}
                }
            }
            board.PrintBoard(board);
            if (TestGame.pickupDeckCardTest == true){
                if (deck.isEmpty()){
                    TestGame.pickupDeckCardTest = false;
                    TestGame.gameState = SOLITARE_STATES.ANALYZE_CARD_MOVE;
                }else{
                    activity.playGame(board.getCurrentDeckCard());
                }


            }else{
                if (!revealedCard.getTitle().equals("NA")){
                    activity.playGame(revealedCard);
                }else{
                    //Dette bliver brugt når ingen ting ændrer sig i et stykke tid derfor kaldes playGame med et tilfældigt kort så
                    //main logikken selv skubber spillet videre ved at trække et nyt deck kort.
                    activity.playGame(((Card) TestGame.recognizedCards.getLast()));
                }
            }
            if(TestGame.moveCardTest == true){
                if(board.getCurrentDeckCard() == TestGame.fromDeckTest){
                    for(int i = 0; i < board.coloumnCards.size(); i++){

                        if ((!board.getRow(i+1).isEmpty()) &&(board.getRow(i+1).get(board.getRow(i+1).size()-1) == TestGame.toTest)){
                            board.getRow(i+1).add(TestGame.fromDeckTest);
                            board.deck.remove(board.getCurrentDeckCard());
                            board.setCurrentCardDeckAferRemove();
                        }
                        else if(TestGame.toEmptyTest != -1){
                            if (board.getRow(i+1).isEmpty()){
                                board.getRow(i+1).add(TestGame.fromDeckTest);
                                board.deck.remove(board.getCurrentDeckCard());
                                board.setCurrentCardDeckAferRemove();
                                TestGame.toEmptyTest = -1;
                            }
                        }
                    }
                }
            }

            dowhile++;
        }while(dowhile <= 500);
        //test
        board.PrintBoard(board);
    }

    public void solveIt(){

    }
}