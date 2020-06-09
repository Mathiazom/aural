package com.mzom.aural.database;

import com.mzom.aural.models.Playable;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface PlayableDAO {

    @Insert
    void insert(Playable playable);

    @Update
    void update(Playable playable);

    @Delete
    void delete(Playable playable);

    @Query("SELECT * FROM Playable WHERE folder_id = :folderId")
    LiveData<List<Playable>> getPlayablesFromFolder(long folderId);


}
