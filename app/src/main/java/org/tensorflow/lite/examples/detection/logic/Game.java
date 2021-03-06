package org.tensorflow.lite.examples.detection.logic;

import android.graphics.Camera;

import org.tensorflow.lite.examples.detection.CameraActivity;
import org.tensorflow.lite.examples.detection.DetectorActivity;
import org.tensorflow.lite.examples.detection.viewmodels.GameViewModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Locale;

public class Game {

    private GameViewModel gameViewModel;

    public static LinkedList[] cardColumns;
    public static LinkedList recognizedCards;
    public static ArrayList<String> cardMoves;
    private static int cardColumnCounter;
    public static SOLITARE_STATES gameState;
    private static Card movingCard;
    private static int emptyColoumn;


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

    public static int NEWEST_EMPTY_COLUMN;
    public static int[] hiddenCardsInColumns = {0,1,2,3,4,5,6};

    public Game(){
        gameViewModel = DetectorActivity.gameViewModel;
        cardColumns = null;
        emptyColoumn = -1;
        gameState = SOLITARE_STATES.INITIAL;
        cardColumnCounter = 0;
        NEWEST_EMPTY_COLUMN = -1;
        //initializing card columns
        initializeCardColumns();
        cardMoves = new ArrayList<String>();
        recognizedCards = new LinkedList<Card>();

    }

    public void loadGame() {
        CameraActivity.gameViewModel.loadRecognizedCards();
        LinkedList<Card> temp = (LinkedList<Card>) gameViewModel.getLoadedCards();
        for (Card c : temp
        ) {
            playGame(c);
        }
        gameState = SOLITARE_STATES.ANALYZE_CARD_MOVE;
    }


    public void initializeCardColumns() {
        if (cardColumns == null) {
            cardColumns = new LinkedList[7];
            for (int i = 0; i < 7; i++) {
                cardColumns[i] = new LinkedList<Card>();
            }
        }
    }
    public static int getCardNumber(Card card) {

        char[] toArray = card.getTitle().toCharArray();
        if ((char) (toArray[0]) == 'A')
            return 1;
        if ((char) (toArray[0]) == 'J')
            return 11;
        else if ((char) (toArray[0]) == 'Q')
            return 12;
        else if ((char) (toArray[0]) == 'K')
            return 13;
        else if (((char) (toArray[0]) == '1') && ((char) (toArray[1]) == '0'))
            return 10;
        return Integer.parseInt(toArray[0] + "");
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
        number = getCardNumber(resultCard);
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
        // first check if a card can be removed and put into finished card queue
        for (int i = 0; i < 7; i++) {
            if((!cardColumns[i].isEmpty()) && cardsToFoundationPile((Card) cardColumns[i].getLast())) {
                // this opened card should be moved out to finished card queue
                if(cardColumns[i].isEmpty()) {
                    // this is the last card in the list
                    NEWEST_EMPTY_COLUMN = i;
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
                        gameViewModel.setShowBar(true, "Move " + movingCard.getTitle() + " to " + ((Card) cardColumns[j].getLast()).getTitle());
                        CameraActivity.waitPlayerOptionLoop();
                        System.out.println("***************** CARD " + movingCard.getTitle() + " CAN BE MOVED TO " + ((Card) cardColumns[j].getLast()).getTitle() + " ************");
                        cardMoves.add(movingCard.getTitle() + "-" + (j+1));
                        //}
                        cardColumns[j].addAll(cardColumns[i]);
                        cardColumns[i].clear();
                        NEWEST_EMPTY_COLUMN = i;
                        return SOLITARE_STATES.DISPLAY_HIDDEN_CARD;
                    }
                }
            }
        }
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
                } else if (!spades.isEmpty() && (getCardNumber(card) == getCardNumber(spades.getLast()) + 1)) {
                    spades.add(new Card(card.getTitle()));
                    removeCard = true;
                }
                break;
            case 'c':
                if (clubs.isEmpty() && card.getTitle().toLowerCase(Locale.ROOT).charAt(0) == 'a') {
                    clubs.add(new Card("Ac"));
                    removeCard = true;

                } else if (!clubs.isEmpty() && (getCardNumber(card) == getCardNumber(clubs.getLast()) + 1)) {
                    clubs.add(new Card(card.getTitle()));
                    removeCard = true;
                }
                break;
            case 'h':
                if (hearts.isEmpty() && card.getTitle().toLowerCase(Locale.ROOT).charAt(0) == 'a') {
                    hearts.add(new Card("Ah"));
                    removeCard = true;
                } else if (!hearts.isEmpty() && (getCardNumber(card) == getCardNumber(hearts.getLast()) + 1)) {
                    hearts.add(new Card(card.getTitle()));
                    removeCard = true;
                }
                break;
            case 'd':
                if (diamonds.isEmpty() && card.getTitle().toLowerCase(Locale.ROOT).charAt(0) == 'a') {
                    diamonds.add(new Card("Ad"));
                    removeCard = true;
                } else if (!diamonds.isEmpty() && (getCardNumber(card) == getCardNumber(diamonds.getLast()) + 1)) {
                    diamonds.add(new Card(card.getTitle()));
                    removeCard = true;
                }
                break;
        }
        if(removeCard) {
            for (int i = 0; i < 7; i++) {
                if(!cardColumns[i].isEmpty() && ((Card)cardColumns[i].getLast()).getTitle().equalsIgnoreCase(card.getTitle())) {
                    cardColumns[i].remove(card);
                    break;
                }
            }
            gameViewModel.setShowBar(true, "Move card " + card.getTitle() + " to foundation pile");
            CameraActivity.waitPlayerOptionLoop();
            cardMoves.add(card.getTitle() + "-F");
        }
        return removeCard;
    }
    //returns two cards for tests, should return void when no testing.
    public void playGame(Card resultCard) {

        emptyColoumn = -1;

        switch (gameState) {

            case INITIAL:
                if (!recognizedCardsContains(resultCard)){
                    System.out.println("RECOGNIZED SPECIFIC CARD:" + resultCard.getTitle());
                    recognizedCards.add(resultCard);
                    gameViewModel.addRecognizedCard(resultCard);
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
                        gameViewModel.setShowBar(true, "Film card on row " + (cardColumnCounter + 1));
                        CameraActivity.waitPlayerOptionLoop();
                    }
                }

                break;

            case ANALYZE_CARD_MOVE:
                System.out.println("************* ENTER ANALYZE_CARD_MOVE_PHASE");
                gameState = handleCheckShownCards();
                break;

            case DISPLAY_HIDDEN_CARD:
                gameViewModel.setShowBar(true, "Display hidden card");
                CameraActivity.waitPlayerOptionLoop();
                System.out.println("************* ENTER DISPLAY_HIDDEN_CARD ********");
                boolean hiddenCardCanBeDisplayed = false;
                for (int i = 0; i < 7; i++) {
                    if(cardColumns[i].isEmpty() && hiddenCardsInColumns[i] != 0){
                        hiddenCardCanBeDisplayed = true;
                    }
                }
                if(!hiddenCardCanBeDisplayed){
                    gameViewModel.setShowBar(true, "Pick up new card from deck!");
                    CameraActivity.waitPlayerOptionLoop();
                    gameState = SOLITARE_STATES.ANALYZE_CARD_MOVE;
                    break;
                }

                if (!recognizedCardsContains(resultCard)) {
                    recognizedCards.add(resultCard);
                    gameViewModel.addRecognizedCard(resultCard);
                    gameViewModel.setShowBar(true, resultCard.getTitle());
                    CameraActivity.waitPlayerOptionLoop();
                    System.out.println("------- Find lately opened card " + resultCard.getTitle() + "-------");
                    cardMoves.add("T");
                    //for (int i = 0; i < 7; i++) {
                    if (NEWEST_EMPTY_COLUMN != -1){
                        if (cardColumns[NEWEST_EMPTY_COLUMN].isEmpty() && hiddenCardsInColumns[NEWEST_EMPTY_COLUMN] != 0){
                            updateHiddenCardsInColumns(NEWEST_EMPTY_COLUMN);
                            cardColumns[NEWEST_EMPTY_COLUMN].add(resultCard);
                            gameState = SOLITARE_STATES.ANALYZE_CARD_MOVE;
                            NEWEST_EMPTY_COLUMN = -1;
                            return;

                        }
                    }

                }


                break;
            case PICKUP_DECK_CARD:
                System.out.println("*************  ENTER PICKUP_DECK_CARD *****");
                gameViewModel.setShowBar(true, "Pick up card from deck");
                CameraActivity.waitPlayerOptionLoop();
                boolean cardCanBeUsed = false;
                if (!recognizedCardsContains(resultCard)){
                    recognizedCards.add(resultCard);
                    gameViewModel.setShowBar(true, resultCard.getTitle());
                    CameraActivity.waitPlayerOptionLoop();
                    System.out.println("-------- found a new card " + resultCard.getTitle() + "-------");
                    cardMoves.add("T");
                    if (!cardsToFoundationPile(resultCard)){
                        for (int i = 0; i < 7; i++) {
                            if ((!cardColumns[i].isEmpty()) && isCardCanBeUsed(((Card) cardColumns[i].getLast()), resultCard) && !finishedCard.contains(resultCard)) {
                                // add the new card to the list
                                Card oldListLastCard = ((Card) cardColumns[i].getLast());
                                cardColumns[i].addLast(resultCard);
                                recognizedCards.add(resultCard);
                                gameViewModel.addRecognizedCard(resultCard);
                                gameViewModel.setShowBar(true,"Move new card: " + resultCard.getTitle() +" to " + oldListLastCard.getTitle());
                                CameraActivity.waitPlayerOptionLoop();
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
                                gameViewModel.addRecognizedCard(resultCard);
                                //for (int k = 0; k < 10; k++) {
                                gameViewModel.setShowBar(true,"Move new card: " + resultCard.getTitle() +" to " + "empty columnn");
                                CameraActivity.waitPlayerOptionLoop();
                                System.out.println("------ new card " + resultCard.getTitle() + " can be moved to " + "empty columnn" + "----------------------");

                                emptyColoumn = -1;
                                cardCanBeUsed = true;
                                gameState = SOLITARE_STATES.ANALYZE_CARD_MOVE;
                                break;
                            }
                        }
                    }else{
                        cardCanBeUsed = true;
                    }
                    if (!cardCanBeUsed) {
                        gameState = SOLITARE_STATES.PICKUP_DECK_CARD;
                        gameViewModel.setShowBar(true,resultCard.getTitle() + " cannot be used anywhere, pick a new card.");
                        recognizedCards.remove(resultCard);
                        CameraActivity.waitPlayerOptionLoop();
                    }else{
                        gameState = SOLITARE_STATES.ANALYZE_CARD_MOVE;
                    }
                }
                break;
            default:
                break;
        }
    }

    public void updateHiddenCardsInColumns(int column){
        hiddenCardsInColumns[column]--;
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

    public static void fixCardsOnEditedCard(String editedCardTitle) {
        String lastEditedCardTitle = editedCardTitle;
        LinkedList<Card> cardsOnEditedCard = new LinkedList<Card>();
        for (int j = 0; j < 7; j++) {
            for (int k = 0; k < Game.cardColumns[j].size(); k++) {

                if (((Card) Game.cardColumns[j].get(k)).getTitle().equals(lastEditedCardTitle)) {
                    for (int h = k + 1; h < Game.cardColumns[j].size(); h++) {
                        cardsOnEditedCard.add((Card) Game.cardColumns[j].get(h));
                    }
                    if (!Game.cardColumns[j].isEmpty())
                        Game.cardColumns[j].removeAll(cardsOnEditedCard);
                }

            }
        }
        boolean hasAdded = false;
        int emptyIndex = 0;
        for (int j = 0; j < 7; j++) {
            if (Game.cardColumns[j].isEmpty() && Game.hiddenCardsInColumns[j] > 0) {
                hasAdded = true;
                Game.cardColumns[j] = cardsOnEditedCard;
                break;
            } else if (Game.cardColumns[j].isEmpty()) {
                emptyIndex = j;
            }
        }
        if (!hasAdded)
            Game.cardColumns[emptyIndex] = cardsOnEditedCard;
    }




}