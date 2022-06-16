package org.tensorflow.lite.examples.detection.logic;

import android.graphics.Color;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.tensorflow.lite.examples.detection.CameraActivity;
import org.tensorflow.lite.examples.detection.DetectorActivity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Stack;

public class Game {

    private Snackbar snackbar;

    private boolean continueGame = true;

    /*
    Rows
     */
    public static boolean TESTMODE = false;
    private static LinkedList[] cardColumns = null;
    public static LinkedList recognizedCards = new LinkedList<Card>();
    public static Stack deckCards = new Stack<Card>();
    public static ArrayList<String> cardMoves = new ArrayList<String>();

    private static int PHASE_CHANGE_COUNTER = 0;

    private static int cardColumnCounter = 0;
    public static SOLITARE_STATES gameState = SOLITARE_STATES.INITIAL;
    private static Card movingCard;
    private static int waitTimeCount = 0;
    private static int emptyColoumn = -1;


    /*
    Foundation piles
     */
    private static LinkedList<Card> spades = new LinkedList<>();
    private static LinkedList<Card> clubs = new LinkedList<>();
    private static LinkedList<Card> hearts = new LinkedList<>();
    private static LinkedList<Card> diamonds = new LinkedList<>();
    private static LinkedList<Card> finishedCard = new LinkedList<>();

    //Deck
    //den samlede mængde af kort i deck og talon skal være 3 eller over ellers kan kabalen ikke løses.
    private static int numberOfCardsDeck = 24;
    private static int numberOfCardsTalon = 0;

    public Game(){
        //initializing card columns
        initializeCardColumns();
    }

    public void initializeCardColumns() {
        if (cardColumns == null) {
            cardColumns = new LinkedList[7];
            for (int i = 0; i < 7; i++) {
                cardColumns[i] = new LinkedList<Card>();
            }
        }
    }



    private static String getCardMatch(int i, char c) {
        String returnText = "";
        if (i == 1) return "A" + c + "";
        if (i == 11) return "J" + c + "";
        else if (i == 12) return "Q" + c + "";
        else if (i == 13) return "K" + c + "";
        return (String) (returnText + i) + c + "";
    }

    public static boolean isCardCanBeUsed(Card card, Card resultCard) {
        int number, number1;
        char color;
        String cardMatch1;
        String cardMatch2;
        String temp = "";
        number = resultCard.getCardNumber();
        color = getCardColor(resultCard);
        if (isKingMovable(resultCard)){
            return false;
        }
        number1 = number + 1;
        if (color == 'h' || color == 'd') {
            cardMatch1 = getCardMatch(number1, 'c').trim();
            cardMatch2 = getCardMatch(number1, 's').trim();
        }  else {
            cardMatch1 = getCardMatch(number1, 'h').trim();
            cardMatch2 = getCardMatch(number1, 'd').trim();
        }
        if (card.getTitle().equalsIgnoreCase(cardMatch1) || card.getTitle().equalsIgnoreCase(cardMatch2))
            return true;
        return false;
    }

    public static Boolean isKingMovable(Card card){
        if (card.getTitle().equals("Kh") || card.getTitle().equals("Kd") || card.getTitle().equals("Kc") || card.getTitle().equals("Ks")) {
            for (int i = 0; i < 7; i++){
                //Så denne funktion bliver kaldt konstant hvilket betyder at den fylder alle de tomme rækker ud
                //med en konge så hvis række 1 og 2 er tomme bliver den fyldt med to gange kh.
                //hovedfunktionen tjekker alle rækker i gennem så den bliver basically kaldt 7 gange i træk.
                if (cardColumns[i].isEmpty()){
                    emptyColoumn = i;
                    return true;
                }
            }
        }
        return false;
    }

    public static char getCardColor(Card card) {

        if (card.getTitle().charAt(1) != '0') {
            return card.getTitle().charAt(1);
        }
        return card.getTitle().charAt(2);
    }

    private SOLITARE_STATES handleCheckShownCards() {
     /*   int number, number1;
        char color;
        String cardMatch1 = "";
        String cardMatch2 = "";
        Card temp;
*/
        // first check if a card can be removed and put into finished card queue
        for (int i = 0; i < 7; i++) {
            if((!cardColumns[i].isEmpty()) && cardsToFoundationPile((Card) cardColumns[i].getLast())) {
                // this opened card should be moved out to finished card queue
                if(cardColumns[i].isEmpty()) {
                    // this is the last card in the list
                    return SOLITARE_STATES.DISPLAY_HIDDEN_CARD;
                }
                // card moved to finish queue, check if other card can be moved
                return SOLITARE_STATES.ANALYZE_CARD_MOVE;
            }

            //Card can be moved to another column
            else{
                for (int j = 0; j < 7; j++){
                    if (cardColumns[i].isEmpty()){
                        continue;
                    }
                    if (cardColumns[j].isEmpty() || (i == j))
                        continue;
                    if(isCardCanBeUsed((Card) cardColumns[j].getLast(),(Card) cardColumns[i].getFirst()) && i!=j){
                        movingCard = (Card) cardColumns[i].getFirst();
                        //for (int k = 0; k < 5; k++) {
                        //waitNSeconds(1);
                        //TODO:waitPlayerOption("Move " + movingCard.getTitle() + " to " + ((Card) cardColumns[j].getLast()).getTitle());
                        System.out.println("***************** CARD " + movingCard.getTitle() + " CAN BE MOVED TO " + ((Card) cardColumns[j].getLast()).getTitle() + " ************");
                        //MyResult myResult = new MyResult(movingCard, ((Card) cardColumns[j].getLast()));
                        cardMoves.add(movingCard.getTitle() + "-" + (j+1));
                        //}
                        cardColumns[j].addAll(cardColumns[i]);
                        cardColumns[i].clear();
                        return SOLITARE_STATES.DISPLAY_HIDDEN_CARD;
                    }
                }
            }
        }
        // then check if some card can be move to another list
            /*number = getCardNumber((Card) cardColumns[i].getFirst());
            color = getCardColor( ((Card) cardColumns[i].getFirst()));
            number1 = number + 1;*/

        // check if king is shown and can be moved to empty column
           /* if (cardColumns[i].isEmpty()){
                for (int j = 0; j < 7; j++) {
                    String kings = ((Card) cardColumns[j].getFirst()).getTitle();
                    if ((kings.equals("Kh") && kings.equals("Kd") && kings.equals("Kc") && kings.equals("Ks")) &&  !((Card) cardColumns[j].getFirst()).getLockedPosition()) {
                        System.out.println("------ move card " + cardColumns[j].getFirst() + "to card column " + i + "------");
                        cardColumns[j] = cardColumns[i];
                        ((Card) cardColumns[i].getFirst()).setLockedPosition(true);
                        cardColumns[i].clear();
                    }
                }
                continue;
            }*/

         /*   if (color == 'h' || color == 'd') {
                cardMatch1 = getCardMatch(number1, 'c').trim();
                cardMatch2 = getCardMatch(number1, 's').trim();
            } else {
                cardMatch1 = getCardMatch(number1, 'h').trim();
                cardMatch2 = getCardMatch(number1, 'd').trim();
            }*/
        //System.out.println("************ CARD MATCH 1 " + cardMatch1
        //  + "******** CARD MATCH 2: "+ cardMatch2 );
         /*   for (int j = 0; j < 7; j++) {

                temp = ((Card) cardColumns[j].getLast());
                if ((temp.getTitle().equals(cardMatch1.toLowerCase(Locale.ROOT))) || (temp.getTitle().equals(cardMatch2.toLowerCase()))) {

                }
            }*/
        // no card can be moved, then pickup new card
        return SOLITARE_STATES.PICKUP_DECK_CARD;
    }

    private void waitNSeconds(int i) {
        try {
            System.out.println("******* WAIT " + i + " SEC **********");
            Thread.sleep(i * 1000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            ex.printStackTrace();
        }
    }

    public static boolean recognizedCardsContains(Card card) {
        ListIterator listIterator = recognizedCards.listIterator();
        while (listIterator.hasNext()) {
            Card columnCard =  (Card)listIterator.next();
            if (columnCard.getTitle().equals(card.getTitle())) {
                return true;
            }
        }
        return false;
    }

    private boolean canMoveCardFromDeck(){
        if(numberOfCardsDeck + numberOfCardsTalon == 3 && numberOfCardsTalon != 0){
            return false;
        }
        else{
            return true;
        }
    }
    private void moveCardFromDeck(){
        if(numberOfCardsTalon == 0){
            numberOfCardsDeck -= 3;
            numberOfCardsTalon = 3;
        }
        numberOfCardsTalon --;
    }



    private boolean cardsToFoundationPile(Card card) {
        boolean removeCard = false;
        char lastColor = getCardColor(card);
        switch (lastColor) {
            case 's':
                if (spades.isEmpty() && card.getTitle().toLowerCase(Locale.ROOT).charAt(0) == 'a') {
                    spades.add(new Card("As"));
                    removeCard = true;
                } else if (!spades.isEmpty() && (card.getCardNumber() == spades.getLast().getCardNumber() + 1)) {
                    spades.add(new Card(card.getTitle()));
                    removeCard = true;
                }
                break;
            case 'c':
                if (clubs.isEmpty() && card.getTitle().toLowerCase(Locale.ROOT).charAt(0) == 'a') {
                    clubs.add(new Card("Ac"));
                    removeCard = true;

                } else if (!clubs.isEmpty() && (card.getCardNumber() == clubs.getLast().getCardNumber() + 1)) {
                    clubs.add(new Card(card.getTitle()));
                    removeCard = true;
                }
                break;
            case 'h':
                if (hearts.isEmpty() && card.getTitle().toLowerCase(Locale.ROOT).charAt(0) == 'a') {
                    hearts.add(new Card("Ah"));
                    removeCard = true;
                } else if (!hearts.isEmpty() && (card.getCardNumber() == hearts.getLast().getCardNumber() + 1)) {
                    hearts.add(new Card(card.getTitle()));
                    removeCard = true;
                }
                break;
            case 'd':
                if (diamonds.isEmpty() && card.getTitle().toLowerCase(Locale.ROOT).charAt(0) == 'a') {
                    diamonds.add(new Card("Ad"));
                    removeCard = true;
                } else if (!diamonds.isEmpty() && card.getCardNumber() == diamonds.getLast().getCardNumber() + 1) {
                    diamonds.add(new Card(card.getTitle()));
                    removeCard = true;
                }
                break;
        }
        if(removeCard) {
            for (int i = 0; i < 7; i++) {
                if(!cardColumns[i].isEmpty() && ((Card)cardColumns[i].getLast()).getTitle().equalsIgnoreCase(card.getTitle())) {
                    //TEST//
                    //TODO:DetectorActivity.fromTest.clear();
                    //TEST//
                    cardColumns[i].remove(card);
                    break;
                }
            }
            //for(int i = 0; i < 5; i++) {
            //TODO:waitPlayerOption("------ move card " + card.getTitle() + " to foundation pile ------");
            //waitNSeconds(1);
            // }
            cardMoves.add(card.getTitle() + "-F");
        }
        return removeCard;
    }
    public void playGame(Card resultCard) {

        emptyColoumn = -1;

        switch (gameState) {

            case INITIAL:
                if (!recognizedCardsContains(resultCard)){
                    System.out.println("RECOGNIZED SPECIFIC CARD:" + resultCard.getTitle());
                    recognizedCards.add(resultCard);
                    CameraActivity.gameViewModel.addRecognizedCard(resultCard);
                    if (cardColumnCounter == 6) {
                        for (int i = 0; i < 7; i++) {
                            cardColumns[i].add(recognizedCards.get(i));
                            Card card = (Card) cardColumns[i].getFirst();
                            System.out.println("************ KNOWN CARDS IN COLUMN: " + i + "   "
                                    + card.getTitle().trim() + "**********");
                        }
                        //waitNSeconds(5);
                        gameState = SOLITARE_STATES.ANALYZE_CARD_MOVE;
                        playGame((Card) (cardColumns[6].getFirst()));
                        return;
                    } else {
                        cardColumnCounter++;
                        //TODO:waitPlayerOption("Film card on row " + (cardColumnCounter + 1));
                    }
                }

                break;

            case ANALYZE_CARD_MOVE:
                System.out.println("************* ENTER ANALYZE_CARD_MOVE_PHASE");
                PHASE_CHANGE_COUNTER = 0;
                gameState = handleCheckShownCards();
                break;

            case DISPLAY_HIDDEN_CARD:
                System.out.println("************* ENTER DISPLAY_HIDDEN_CARD ********");
                //Counter to change phase
                PHASE_CHANGE_COUNTER++;
                if (PHASE_CHANGE_COUNTER >= 10){
                    gameState = SOLITARE_STATES.PICKUP_DECK_CARD;
                    //TODO:waitPlayerOption("Pick up new card from deck!");
                    break;
                }

                if (!recognizedCardsContains(resultCard)) {
                    recognizedCards.add(resultCard);
                    CameraActivity.gameViewModel.addRecognizedCard(resultCard);
                    System.out.println("------- Find lately opened card " + resultCard.getTitle() + "-------");
                    cardMoves.add("T");
                    for (int i = 0; i < 7; i++) {
                        if (cardColumns[i].isEmpty()){
                            cardColumns[i].add(resultCard);
                            gameState = SOLITARE_STATES.ANALYZE_CARD_MOVE;
                            return;

                        }
                        if (i == 6){
                            System.out.println("----------------- No new card opened and no column to move, pickup new card from deck +++++++++++++++++++++++++++++++++++");
                            cardMoves.add("T");
                            gameState = SOLITARE_STATES.PICKUP_DECK_CARD;
                            break;
                            // pickupDeckCard = true;

                        }
                    }
                }


                break;
            case PICKUP_DECK_CARD:
                System.out.println("*************  ENTER PICKUP_DECK_CARD *****");
                boolean cardCanBeUsed = false;
                if (!recognizedCardsContains(resultCard)){
                    System.out.println("-------- found a new card " + resultCard.getTitle() + "-------");
                    cardMoves.add("T");
                    if (!cardsToFoundationPile(resultCard)){
                        for (int i = 0; i < 7; i++) {
                            if ((!cardColumns[i].isEmpty()) && isCardCanBeUsed(((Card) cardColumns[i].getLast()), resultCard) && !finishedCard.contains(resultCard)) {
                                // add the new card to the list
                                Card oldListLastCard = ((Card) cardColumns[i].getLast());
                                cardColumns[i].addLast(resultCard);
                                recognizedCards.add(resultCard);
                                CameraActivity.gameViewModel.addRecognizedCard(resultCard);
                                //TODO:waitPlayerOption("Move new card: " + resultCard.getTitle() +" to " + oldListLastCard.getTitle() );
                                System.out.println("Move new card " + resultCard.getTitle() + "to" + oldListLastCard.getTitle());
                                cardMoves.add(resultCard.getTitle() + "-" + ( i + 1));
                                gameState = SOLITARE_STATES.ANALYZE_CARD_MOVE;
                                cardCanBeUsed = true;
                                break;
                            }
                            else if(emptyColoumn != -1){
                                // add the new card to the list
                                cardColumns[emptyColoumn].addLast(resultCard);
                                recognizedCards.add(resultCard);
                                CameraActivity.gameViewModel.addRecognizedCard(resultCard);
                                //for (int k = 0; k < 10; k++) {
                                //TODO:waitPlayerOption("Move new card: " + resultCard.getTitle() +" to " + "empty columnn" );
                                System.out.println("------ new card " + resultCard.getTitle() + " can be moved to " + "empty columnn" + "----------------------");
                                emptyColoumn = -1;
                                cardCanBeUsed = true;
                                gameState = SOLITARE_STATES.ANALYZE_CARD_MOVE;
                                break;
                            }
                        }
                    }else{
                        cardCanBeUsed = true;
                        recognizedCards.add(resultCard);
                    }
                    if (!cardCanBeUsed) {
                        gameState = SOLITARE_STATES.PICKUP_DECK_CARD;
                        //TODO:waitPlayerOption(resultCard.getTitle() + " cannot be used anywhere, pick a new card.");
                    }else{
                        gameState = SOLITARE_STATES.ANALYZE_CARD_MOVE;
                    }
                }

                break;
            default:
                break;
        }
    }

    /*public void waitPlayerOption (String snackbarText) {
        if (TESTMODE == true){
            System.out.println(snackbarText);
            return;
        }

        continueGame = false;
        TODO:snackbar = Snackbar
                .make(findViewById(android.R.id.content).getRootView(), snackbarText + "\n\n", Snackbar.LENGTH_INDEFINITE)
                .setAction("Complete move" + "\n", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        continueGame = true;

                        Toast.makeText(getApplicationContext(),"Move completed",Toast.LENGTH_LONG).show();

                        return;
                    }
                });

        // snackbar UI
        snackbar.setActionTextColor(Color.GRAY);
        snackbar.setTextColor(Color.BLACK);
        snackbar.setBackgroundTint(Color.WHITE);

        snackbar.show();
        int inactiveCount = 0;
        while (!continueGame){
            inactiveCount++;
            // loop until player presses done
            if (inactiveCount >= 1000){
                continueGame = true;
                break;
            }

        }
    }*/
    final class MyResult{
        private final Card from;
        private final Card to;

        MyResult(Card from, Card to) {
            this.from = from;
            this.to = to;
        }
    }

    public void printBoard(){
        int biggestRow = 0;

        for (int k = 6; k >= 0; k--){
            if(biggestRow < cardColumns[k].size()){
                biggestRow = cardColumns[k].size();
            }
        }
        for(int j = 0; j < biggestRow; j++){
            for (int i = 0; i < 7; i++){
                try {
                    System.out.print(((Card) (cardColumns[i].get(j))).getTitle()+ "       ");
                } catch (Exception e){
                    System.out.print("         ");
                }
            }
            System.out.println();
        }
    }

    public void printMoves(){
        int last = 0;
        System.out.println("");
        for (String move : cardMoves){
            if (last == cardMoves.size() - 1){
                System.out.print(move);
            } else {
                System.out.print(" "+ move + " ,");
                last++;
            }
        }
        System.out.println("");
    }

    public SOLITARE_STATES getGameState(){
        return gameState;
    }
}
