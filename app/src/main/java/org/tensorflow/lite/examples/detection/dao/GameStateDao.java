package org.tensorflow.lite.examples.detection.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import org.tensorflow.lite.examples.detection.logic.Game;
import org.tensorflow.lite.examples.detection.logic.GameState;

@Dao
public interface GameStateDao {

    @Query("SELECT * FROM GameState WHERE id == :gameId")
    GameState getRecognizedCards(int gameId);

    @Update
    void updateState(GameState... states);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(GameState... states);

}
