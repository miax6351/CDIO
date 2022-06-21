package org.tensorflow.lite.examples.detectiontest.logic;

import org.tensorflow.lite.examples.detection.CameraActivity;
import org.tensorflow.lite.examples.detection.logic.Card;
import org.tensorflow.lite.examples.detection.logic.SOLITARE_STATES;
import org.tensorflow.lite.examples.detection.viewmodels.GameViewModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Stack;

public class TestGame {

    private GameViewModel gameViewModel;

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

    //Test//
    public static List<Card> fromTest = new LinkedList<Card>();
    public static int toEmptyTest = -1;
    public static Card toTest;
    public static boolean kingToEmpty = false;
    public static Boolean pickupDeckCardTest = false;
    public static Boolean moveCardTest = false;
    public static Card fromDeckTest;
    public static Boolean drawTest = false;
    public static Boolean moveToFoundationTest = false;
    public static Boolean moveCardColoumnTest = true;
    public static int roundCounterTest = 0;
    //Test//


    //Deck
    //den samlede mængde af kort i deck og talon skal være 3 eller over ellers kan kabalen ikke løses.
    private static int numberOfCardsDeck = 24;
    private static int numberOfCardsTalon = 0;

    public static int NEWEST_EMPTY_COLUMN;
    private int[] hiddenCardsInColumns = {0, 1, 2, 3, 4, 5, 6};

    public TestGame() {
        NEWEST_EMPTY_COLUMN = -1;
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
        if (isKingMovable(resultCard)) {
            return false;
        }
        number1 = number + 1;
        if (color == 'h' || color == 'd') {
            cardMatch1 = getCardMatch(number1, 'c').trim();
            cardMatch2 = getCardMatch(number1, 's').trim();
        } else {
            cardMatch1 = getCardMatch(number1, 'h').trim();
            cardMatch2 = getCardMatch(number1, 'd').trim();
        }
        if (card.getTitle().equalsIgnoreCase(cardMatch1) || card.getTitle().equalsIgnoreCase(cardMatch2))
            return true;
        return false;
    }

    public static Boolean isKingMovable(Card card) {
        if (card.getTitle().equals("Kh") || card.getTitle().equals("Kd") || card.getTitle().equals("Kc") || card.getTitle().equals("Ks")) {
            for (int i = 0; i < 7; i++) {
                //Så denne funktion bliver kaldt konstant hvilket betyder at den fylder alle de tomme rækker ud
                //med en konge så hvis række 1 og 2 er tomme bliver den fyldt med to gange kh.
                //hovedfunktionen tjekker alle rækker i gennem så den bliver basically kaldt 7 gange i træk.
                if (cardColumns[i].isEmpty()) {
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
            if ((!cardColumns[i].isEmpty()) && cardsToFoundationPile((Card) cardColumns[i].getLast())) {
                // this opened card should be moved out to finished card queue
                if (cardColumns[i].isEmpty()) {
                    // this is the last card in the list
                    NEWEST_EMPTY_COLUMN = i;
                    return SOLITARE_STATES.DISPLAY_HIDDEN_CARD;
                }
                // card moved to finish queue, check if other card can be moved
                return SOLITARE_STATES.ANALYZE_CARD_MOVE;
            }

            //Card can be moved to another column
            else {
                for (int j = 0; j < 7; j++) {
                    if (cardColumns[i].isEmpty()) {
                        continue;
                    }
                    if (cardColumns[j].isEmpty() || (i == j))
                        continue;
                    if ((isCardCanBeUsed((Card) cardColumns[j].getLast(), (Card) cardColumns[i].getFirst()) && i != j) || isKingMovable((Card) cardColumns[i].getFirst())) {
                        if (emptyColoumn != -1){
                            cardColumns[emptyColoumn].addAll(cardColumns[i]);
                            moveCardColoumnTest = true;
                            kingToEmpty = true;
                            toEmptyTest = emptyColoumn;
                            NEWEST_EMPTY_COLUMN = i;

                            fromTest.clear();
                            fromTest.addAll(cardColumns[i]);
                            cardColumns[i].clear();
                            emptyColoumn = -1;
                        }else{
                            movingCard = (Card) cardColumns[i].getFirst();
                            //for (int k = 0; k < 5; k++) {
                            //waitNSeconds(1);

                            System.out.println("***************** CARD " + movingCard.getTitle() + " CAN BE MOVED TO " + ((Card) cardColumns[j].getLast()).getTitle() + " ************");
                            //MyResult myResult = new MyResult(movingCard, ((Card) cardColumns[j].getLast()));
                            cardMoves.add(movingCard.getTitle() + "-" + (j + 1));
                            moveCardColoumnTest = true;
                            fromTest.clear();
                            fromTest.addAll(cardColumns[i]);
                            toTest = ((Card) cardColumns[j].getLast());

                            //}
                            cardColumns[j].addAll(cardColumns[i]);
                            cardColumns[i].clear();
                            NEWEST_EMPTY_COLUMN = i;
                        }
                        return SOLITARE_STATES.DISPLAY_HIDDEN_CARD;
                    }
                }
            }
        }
        // no card can be moved, then pickup new card
        return SOLITARE_STATES.PICKUP_DECK_CARD;
    }


    public static boolean recognizedCardsContains(Card card) {
        ListIterator listIterator = recognizedCards.listIterator();
        while (listIterator.hasNext()) {
            Card columnCard = (Card) listIterator.next();
            if (columnCard.getTitle().equals(card.getTitle())) {
                return true;
            }
        }
        return false;
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
        if (removeCard) {
            for (int i = 0; i < 7; i++) {
                if (!cardColumns[i].isEmpty() && ((Card) cardColumns[i].getLast()).getTitle().equalsIgnoreCase(card.getTitle())) {
                    cardColumns[i].remove(card);
                    break;
                }
            }

            fromTest.clear();
            fromTest.add(card);
            System.out.println("Move to foundation: " + card.getTitle());
            moveToFoundationTest = true;
            cardMoves.add(card.getTitle() + "-F");

        }
        return removeCard;
    }

    //returns two cards for tests, should return void when no testing.
    public void playGame(Card resultCard) {
        //TEST//

        pickupDeckCardTest = false;
        moveCardTest = false;
        drawTest = false;
        moveToFoundationTest = false;
        moveCardColoumnTest = false;
        emptyColoumn = -1;
        //TEST//
        roundCounterTest++;


        switch (gameState) {

            case INITIAL:
                if (!recognizedCardsContains(resultCard)) {
                    System.out.println("RECOGNIZED SPECIFIC CARD:" + resultCard.getTitle());
                    recognizedCards.add(resultCard);

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
                boolean hiddenCardCanBeDisplayed = false;
                for (int i = 0; i < 7; i++) {
                    if (cardColumns[i].isEmpty() && hiddenCardsInColumns[i] != 0) {
                        hiddenCardCanBeDisplayed = true;
                    }
                }
                if (!hiddenCardCanBeDisplayed) {

                    gameState = SOLITARE_STATES.ANALYZE_CARD_MOVE;
                    break;
                }
                //Counter to change phase
                /*PHASE_CHANGE_COUNTER++;
                if (PHASE_CHANGE_COUNTER >= 10){
                    gameState = SOLITARE_STATES.PICKUP_DECK_CARD;
                    waitPlayerOption("Pick up new card from deck!");
                    break;
                }*/

                if (!recognizedCardsContains(resultCard)) {
                    recognizedCards.add(resultCard);


                    System.out.println("------- Find lately opened card " + resultCard.getTitle() + "-------");
                    cardMoves.add("T");
                    //for (int i = 0; i < 7; i++) {
                    if (NEWEST_EMPTY_COLUMN != -1) {
                        if (cardColumns[NEWEST_EMPTY_COLUMN].isEmpty() && hiddenCardsInColumns[NEWEST_EMPTY_COLUMN] != 0) {
                            updateHiddenCardsInColumns(NEWEST_EMPTY_COLUMN);
                            cardColumns[NEWEST_EMPTY_COLUMN].add(resultCard);
                            gameState = SOLITARE_STATES.ANALYZE_CARD_MOVE;
                            NEWEST_EMPTY_COLUMN = -1;
                            return;

                        }
                    }
                        /*if (i == 6){
                            System.out.println("----------------- No new card opened and no column to move, pickup new card from deck +++++++++++++++++++++++++++++++++++");
                            cardMoves.add("T");
                            gameState = SOLITARE_STATES.PICKUP_DECK_CARD;
                            break;
                            // pickupDeckCard = true;
                        }*/
                    //}
                }


                break;
            case PICKUP_DECK_CARD:
                System.out.println("*************  ENTER PICKUP_DECK_CARD *****");
                //TEST//
                pickupDeckCardTest = true;
                moveCardTest = false;
                //TEST
                PHASE_CHANGE_COUNTER = 0;


                boolean cardCanBeUsed = false;
                if (!recognizedCardsContains(resultCard)) {
                    recognizedCards.add(resultCard);

                    System.out.println("-------- found a new card " + resultCard.getTitle() + "-------");
                    cardMoves.add("T");
                    if (!cardsToFoundationPile(resultCard)) {
                        for (int i = 0; i < 7; i++) {
                            if ((!cardColumns[i].isEmpty()) && isCardCanBeUsed(((Card) cardColumns[i].getLast()), resultCard) && !finishedCard.contains(resultCard)) {
                                // add the new card to the list
                                Card oldListLastCard = ((Card) cardColumns[i].getLast());
                                cardColumns[i].addLast(resultCard);
                                recognizedCards.add(resultCard);


                                System.out.println("Move new card " + resultCard.getTitle() + "to" + oldListLastCard.getTitle());
                                fromDeckTest = resultCard;
                                toTest = ((Card) cardColumns[i].get(cardColumns[i].size() - 2));
                                moveCardTest = true;
                                cardMoves.add(resultCard.getTitle() + "-" + (i + 1));
                                gameState = SOLITARE_STATES.ANALYZE_CARD_MOVE;
                                cardCanBeUsed = true;
                                break;
                            } else if (emptyColoumn != -1) {
                                // add the new card to the list
                                cardColumns[emptyColoumn].addLast(resultCard);
                                recognizedCards.add(resultCard);

                                //for (int k = 0; k < 10; k++) {

                                System.out.println("------ new card " + resultCard.getTitle() + " can be moved to " + "empty columnn" + "----------------------");

                                fromDeckTest = resultCard;
                                toEmptyTest = emptyColoumn;
                                moveCardTest = true;

                                emptyColoumn = -1;
                                cardCanBeUsed = true;
                                gameState = SOLITARE_STATES.ANALYZE_CARD_MOVE;
                                break;
                            }
                        }
                    } else {
                        cardCanBeUsed = true;
                    }
                    if (!cardCanBeUsed) {
                        gameState = SOLITARE_STATES.PICKUP_DECK_CARD;

                        recognizedCards.remove(resultCard);
                        drawTest = true;
                    } else {
                        TestGame.pickupDeckCardTest = false;
                        gameState = SOLITARE_STATES.ANALYZE_CARD_MOVE;
                    }
                }
                break;
            default:
                break;
        }
    }

    public void updateHiddenCardsInColumns(int column) {
        hiddenCardsInColumns[column]--;
    }

}
