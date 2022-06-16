package org.tensorflow.lite.examples.detection.viewmodels;

import static org.tensorflow.lite.examples.detection.CameraActivity.cardSuit;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import org.tensorflow.lite.examples.detection.CameraActivity;
import org.tensorflow.lite.examples.detection.logic.Card;
import org.tensorflow.lite.examples.detection.logic.Game;

import java.util.LinkedList;

public class GameViewModel extends ViewModel {

    private LinkedList<Card> recognizedCards;
    public MutableLiveData<Boolean> isShowing;
    private MutableLiveData<String> content;



    public GameViewModel() {
        System.out.println("GameViewModel created");
        recognizedCards = new LinkedList<>();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        System.out.println("ViewModel destroyed");
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

    public void setShowBar(Boolean isShow, String content){
        this.isShowing.setValue(isShow);
        this.content.setValue(content);

    }
    public Boolean getShowBar(){
        return this.isShowing.getValue();
    }
    public String getSnackBarText(){
        return this.content.getValue();
    }

}
