package com.mzom.aural.database;

import com.mzom.aural.models.Folder;
import com.mzom.aural.models.FolderWithPlayables;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

@Dao
public interface FolderDAO {

    @Insert
    long insert(Folder folder);

    @Update
    void update(Folder folder);

    @Delete
    void delete(Folder folder);

    @Query("DELETE FROM Folder")
    void deleteAll();

    @Transaction
    @Query("SELECT * FROM Folder")
    LiveData<List<FolderWithPlayables>> getFolders();

}
