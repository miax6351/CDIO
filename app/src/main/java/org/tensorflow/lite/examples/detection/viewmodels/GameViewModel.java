package org.tensorflow.lite.examples.detection.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.tensorflow.lite.examples.detection.CameraActivity;
import org.tensorflow.lite.examples.detection.logic.Card;

import java.util.LinkedList;

public class GameViewModel extends ViewModel {

    private LinkedList<Card> recognizedCards;
    public MutableLiveData<Boolean> isShowing;
    public MutableLiveData<Boolean> isShowingEdit;
    public MutableLiveData<String> content;
    private String editInputContent;

    public GameViewModel() {
        System.out.println("GameViewModel created");
        recognizedCards = new LinkedList<>();
        isShowing = new MutableLiveData<>();
        editInputContent = "";
        isShowingEdit = new MutableLiveData<>();
        isShowingEdit.setValue(false);
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
        if (this.content.equals(content)){
            return;
        }
        CameraActivity.waitNSeconds(3);
        this.isShowing.postValue(isShow);
        this.content.postValue(content);

    }



    public String getSnackBarText(){
        return this.content.getValue();
    }

    public Boolean getIsShowingEdit(){
        return this.isShowingEdit.getValue();
    }

    public String getEditContent(){
        return this.editInputContent;
    }

}
