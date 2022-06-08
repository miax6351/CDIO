package org.tensorflow.lite.examples.detection.logic;

public enum SOLITARE_STATES {
    INITIAL, ANALYZE_CARD_MOVE, DISPLAY_HIDDEN_CARD, PICKUP_DECK_CARD
    /*
    INITIAL PHASE: First 7 cards are recognized and inserted into LinkedList "columns"
    ANALYZE_CARD_MOVE: Analyze which cards can be moved to where, and tells player to move
    DISPLAY_HIDDEN_CARD: Tells the player to display a hidden card
    or move another column to an empty column
    PICK_DECK_CARD: With nothing to move, tells the player to pick up a card from the deck
     */

}


