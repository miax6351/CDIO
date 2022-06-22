package org.tensorflow.lite.examples.detection.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.Room;

import org.tensorflow.lite.examples.detection.CameraActivity;
import org.tensorflow.lite.examples.detection.dao.GameStateDao;
import org.tensorflow.lite.examples.detection.database.GameStateDatabase;
import org.tensorflow.lite.examples.detection.logic.Card;
import org.tensorflow.lite.examples.detection.logic.GameState;

import java.util.LinkedList;
import java.util.List;

public class GameViewModel extends ViewModel {

    private LinkedList<Card> recognizedCards;
    private LinkedList<Card> loadedCards;
    public MutableLiveData<Boolean> isShowing;
    public MutableLiveData<Boolean> isShowingEdit;
    public MutableLiveData<String> content;
    private String editInputContent;

    private GameState state;
    private GameStateDatabase stateDB;
    private GameStateDao stateDao;

    public GameViewModel() {
        System.out.println("GameViewModel created");
        recognizedCards = new LinkedList<>();
        loadedCards = new LinkedList<>();
        stateDB = Room.databaseBuilder(CameraActivity.context, GameStateDatabase.class,"State-Database").allowMainThreadQueries().build();
        stateDao = stateDB.gameStateDao();
        state = new GameState();
        isShowing = new MutableLiveData<>();
        editInputContent = "";
        isShowingEdit = new MutableLiveData<>();
        isShowingEdit.setValue(false);
        content = new MutableLiveData<>();
        isShowing.setValue(true);
        content.setValue("Film row 1");
    }



    public LinkedList<Card> getLoadedCards(){return loadedCards;}

    public void loadRecognizedCards(){
        GameState temp =  stateDao.getRecognizedCards(0);
        List<String> tempList = List.of(temp.recognizedCards.split(","));
        for (String s:tempList
        ) {
            loadedCards.add(new Card((s.replace(" ",""))));
        }
        System.out.println(tempList);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        System.out.println("ViewModel destroyed");
    }

    public void addRecognizedCard(Card card) {
        recognizedCards.add(card);
        state.setRecognizedCards(recognizedCards);
        stateDao.updateState(state);
    }

    public void removeRecognizedCard(Card card){
        recognizedCards.remove(card);
    }

    public LinkedList<Card> getRecognizedCards(){
        return recognizedCards;
    }

    public void setShowBar(Boolean isShow, String content){
        CameraActivity.waitNSeconds(3);
        this.isShowing.postValue(isShow);
        this.content.postValue(content);

    }

    public void setIsShowingEdit(Boolean isShowingEdit){
        this.isShowingEdit.postValue(isShowingEdit);
    }

    public void setEditContent(String editInputContent){
        this.editInputContent = editInputContent;
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
