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
    public static boolean FIRST_RUN = true;

    public GameViewModel() {
        System.out.println("GameViewModel created");
        recognizedCards = new LinkedList<>();
        isShowing = new MutableLiveData<>();
        content = new MutableLiveData<>();
        isShowing.setValue(true);
        content.setValue("Film row 1");
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
        this.isShowing.postValue(isShow);
        this.content.postValue(content);

    }
    public Boolean getShowBar(){
        System.out.println(this.isShowing.getValue() + "***************FDSASTRHDRTHSRFwdeafrgsrtef");
        return this.isShowing.getValue();
    }
    public String getSnackBarText(){
        return this.content.getValue();
    }

}
