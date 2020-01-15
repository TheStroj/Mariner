package dev.simonmezgec.mariner.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import dev.simonmezgec.mariner.Constant;

/** Class for creating and accessing database instances. */
@Database(entities = {DataAsset.class, DataAssetFavorite.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class MarinerDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static MarinerDatabase sInstance;

    public static MarinerDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        MarinerDatabase.class, Constant.DATABASE_NAME).build();
            }
        }
        return sInstance;
    }

    public abstract DataAssetDao dataAssetDao();
}
