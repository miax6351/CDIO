package org.tensorflow.lite.examples.detection.viewmodels;

import static org.tensorflow.lite.examples.detection.CameraActivity.cardSuit;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Camera;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import org.tensorflow.lite.examples.detection.CameraActivity;
import org.tensorflow.lite.examples.detection.DetectorActivity;
import org.tensorflow.lite.examples.detection.dao.GameStateDao;
import org.tensorflow.lite.examples.detection.database.GameStateDatabase;
import org.tensorflow.lite.examples.detection.logic.Card;
import org.tensorflow.lite.examples.detection.logic.Game;
import org.tensorflow.lite.examples.detection.logic.GameState;

import java.util.LinkedList;
import java.util.List;

public class GameViewModel extends ViewModel {

    private LinkedList<Card> recognizedCards;
    public MutableLiveData<Boolean> isShowing;
    private MutableLiveData<String> content;
    public static boolean FIRST_RUN = true;
    private GameState state;
    private GameStateDatabase stateDB;
    private GameStateDao stateDao;

    public GameViewModel() {
        System.out.println("GameViewModel created");
        recognizedCards = new LinkedList<>();
        isShowing = new MutableLiveData<>();
        content = new MutableLiveData<>();
        isShowing.setValue(true);
        stateDB = Room.databaseBuilder(CameraActivity.context, GameStateDatabase.class,"State-Database").allowMainThreadQueries().build();
        stateDao = stateDB.gameStateDao();
        state = new GameState();
        content.setValue("Film row 1");
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

    public void removeRecognizedCard(Card card) {
        recognizedCards.remove(card);
    }

    public LinkedList<Card> getRecognizedCards() {
        return recognizedCards;
    }

    public void loadRecognizedCards(){
       GameState temp =  stateDao.getRecognizedCards(0);
       List<String> tempList = List.of(temp.recognizedCards.split(","));
        for (String s:tempList
             ) {
            recognizedCards.add(new Card((s)));
        }
    }

    public void setShowBar(Boolean isShow, String content) {
        this.isShowing.postValue(isShow);
        this.content.postValue(content);

    }

    public Boolean getShowBar() {
        System.out.println(this.isShowing.getValue() + "***************FDSASTRHDRTHSRFwdeafrgsrtef");
        return this.isShowing.getValue();
    }

    public String getSnackBarText() {
        return this.content.getValue();
    }

}
