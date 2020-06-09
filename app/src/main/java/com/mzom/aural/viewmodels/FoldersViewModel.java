package com.mzom.aural.viewmodels;

import android.app.Application;

import com.mzom.aural.models.FolderWithPlayables;
import com.mzom.aural.models.Playable;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class FoldersViewModel extends AndroidViewModel {

    private static final String TAG = "AUR-FoldersViewModel";

    private final FoldersRepository filesRepository;


    public FoldersViewModel(final Application application){
        super(application);

        this.filesRepository = new FoldersRepository(application.getApplicationContext());

    }


    public void addFolder(final FolderWithPlayables folder){

        filesRepository.addFolder(folder);

    }

    public void updatePlayable(final Playable playable){
        filesRepository.updatePlayable(playable);
    }

    public void addPlayable(final Playable playable){

        filesRepository.addPlayable(playable);

    }

    public LiveData<List<Playable>> getPlayablesFromFolder(long folderId){

        return filesRepository.getPlayablesFromFolder(folderId);

    }

    public LiveData<List<FolderWithPlayables>> getFolders(){

        return filesRepository.getFolders();

    }


}
