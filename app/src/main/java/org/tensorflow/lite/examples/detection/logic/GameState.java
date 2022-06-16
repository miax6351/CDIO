package org.tensorflow.lite.examples.detection.logic;

import java.sql.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GameState {

    private String moves;
    private LinkedList recognizedCards;
    private SOLITARE_STATES state;
    private LinkedList cardColumns;
    private static GameState instance;


    private GameState(){
    }

    public static GameState CreateState(){
        if (instance == null) {
            instance = new GameState();
        }
        return instance;
    }

    public static void setNewState(GameState newState){
        instance = newState;
    }

    public void setCardColumns(LinkedList cardColumns) {
        this.cardColumns = cardColumns;
    }

    public void setState(SOLITARE_STATES state){
        this.state = state;
    }

    public void setRecognizedCards(LinkedList recognizedCards) {
        this.recognizedCards = recognizedCards;
    }

    public void setMoves(String moves) {
        this.moves = moves;
    }

}
