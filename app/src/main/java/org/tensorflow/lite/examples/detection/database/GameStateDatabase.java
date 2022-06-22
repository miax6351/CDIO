package org.tensorflow.lite.examples.detection.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import org.tensorflow.lite.examples.detection.dao.GameStateDao;
import org.tensorflow.lite.examples.detection.logic.GameState;

@Database(entities = {GameState.class}, version = 1)
public abstract class GameStateDatabase extends RoomDatabase {
    public abstract GameStateDao gameStateDao();
}
