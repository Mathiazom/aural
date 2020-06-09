package com.mzom.aural.database;

import android.content.Context;

import com.mzom.aural.models.Folder;
import com.mzom.aural.models.Playable;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(
        entities = {
                Folder.class,
                Playable.class
        },
        version = 9,
        exportSchema = false
)
public abstract class PlayableDatabase extends RoomDatabase {

    private static PlayableDatabase instance;

    public abstract FolderDAO folderDAO();

    public abstract PlayableDAO playableDAO();


    public static synchronized PlayableDatabase getInstance(final Context context){

        if(instance == null){

            instance = Room.databaseBuilder(context.getApplicationContext(),
                    PlayableDatabase.class,"playable_database")
                    .fallbackToDestructiveMigration()
                    .build();

        }

        return instance;

    }

}
