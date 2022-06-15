package org.tensorflow.lite.examples.detection.viewmodels;

import static org.tensorflow.lite.examples.detection.CameraActivity.cardSuit;

import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import org.tensorflow.lite.examples.detection.CameraActivity;
import org.tensorflow.lite.examples.detection.logic.Card;

import java.util.LinkedList;

public class GameViewModel extends ViewModel {

    private LinkedList<Card> recognizedCards;
    private int score;

    public GameViewModel() {
        System.out.println("GameViewModel created");
        recognizedCards = new LinkedList<>();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        System.out.println("ViewModel destroyed");
    }

    public void increment(){
        score++;
    }

    public void decrement(){
        score--;
    }

    public void addRecognizedCard(Card card){
        recognizedCards.add(card);
    }

    public void removeRecognizedCard(Card card){
        recognizedCards.remove(card);
    }

    public LinkedList<Card> getRecognizedCards(){
        return recognizedCards;
    }
}
