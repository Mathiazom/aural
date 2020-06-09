package com.mzom.aural.viewmodels;

import android.content.Context;

import com.mzom.aural.database.DatabaseTasks;
import com.mzom.aural.database.FolderDAO;
import com.mzom.aural.database.PlayableDAO;
import com.mzom.aural.database.PlayableDatabase;
import com.mzom.aural.models.FolderWithPlayables;
import com.mzom.aural.models.Playable;

import java.util.List;

import androidx.lifecycle.LiveData;

class FoldersRepository {


    private static final String TAG = "AUR-FoldersRepository";

    private final FolderDAO folderDAO;
    private final PlayableDAO playableDAO;


    FoldersRepository(final Context context){

        final PlayableDatabase playableDatabase = PlayableDatabase.getInstance(context);

        this.folderDAO = playableDatabase.folderDAO();
        this.playableDAO = playableDatabase.playableDAO();

        // TODO: ONLY FOR DEBUGGING
        deleteAllFolders();

    }


    LiveData<List<FolderWithPlayables>> getFolders(){

        return folderDAO.getFolders();

    }

    LiveData<List<Playable>> getPlayablesFromFolder(long folderId){

        return playableDAO.getPlayablesFromFolder(folderId);

    }

    private void deleteAllFolders(){

        new DatabaseTasks.DeleteAllFoldersAsyncTask(folderDAO, playableDAO).execute();

    }

    void addFolder(final FolderWithPlayables folder){

        new DatabaseTasks.AddFolderWithPlayablesAsyncTask(folderDAO, playableDAO).execute(folder);

    }

    void updatePlayable(final Playable playable){

        new DatabaseTasks.UpdatePlayableAsyncTask(playableDAO).execute(playable);

    }

    void addPlayable(final Playable playable){

        new DatabaseTasks.AddPlayableAsyncTask(playableDAO).execute(playable);

    }

}
