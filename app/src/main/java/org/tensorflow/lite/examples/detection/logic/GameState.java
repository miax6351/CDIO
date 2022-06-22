package org.tensorflow.lite.examples.detection.logic;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.sql.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
public class GameState {

    @PrimaryKey
    public int id;

    @ColumnInfo(name = "recognized_Cards")
    public String recognizedCards;

    @Ignore
    public void setRecognizedCards(LinkedList<Card> recognizedCards) {
        id = 0;
        List<String> values = new ArrayList<>();

        for (Card c : recognizedCards
        ) {
            values.add(c.getTitle());
        }
        this.recognizedCards = values.toString()
                .replace("[","")
                .replace("]","");
    }

}
