package com.mzom.aural.models;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.documentfile.provider.DocumentFile;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.Embedded;
import androidx.room.Relation;

public class FolderWithPlayables implements Serializable {

    private static final String TAG = "AUR-FolderWithPlayables";

    @Embedded
    private final Folder folder;

    @Relation(
            parentColumn = "id",
            entityColumn = "folder_id",
            entity = Playable.class
    )
    private final List<Playable> playables;

    public static class Factory{

        private final Context context;

        private final Playable.Factory playableFactory;

        public Factory(final Context context){

            this.context = context;

            this.playableFactory = new Playable.Factory(context);

        }

        public FolderWithPlayables buildFromUri(final Uri uri){

            final DocumentFile treeFile = DocumentFile.fromTreeUri(context, uri);

            if(treeFile == null){
                // TODO: Handle support for sdk < 21
                throw new IllegalArgumentException("No document tree found for given uri");
            }

            final Folder treeFolder = new Folder(treeFile.getName());

            final List<Playable> playables = new ArrayList<>();

            for (DocumentFile playableFile : treeFile.listFiles()) {

                Log.i(TAG,"Tree list file: " + playableFile);

                final Playable playable = playableFactory.buildFromUri(playableFile.getUri());

                playables.add(playable);

            }

            return new FolderWithPlayables(treeFolder, playables);

        }

    }

    public FolderWithPlayables(final Folder folder, final List<Playable> playables){
        this.folder = folder;
        this.playables = playables;
    }

    public String getTitle(){
        return folder.getTitle();
    }

    public Folder getFolder(){
        return this.folder;
    }

    public List<Playable> getPlayables() {
        return playables;
    }


    public static final DiffUtil.ItemCallback<FolderWithPlayables> DIFF_CALLBACK = new DiffUtil.ItemCallback<FolderWithPlayables>() {
        @Override
        public boolean areItemsTheSame(@NonNull FolderWithPlayables oldItem, @NonNull FolderWithPlayables newItem) {
            //Log.i(TAG,"Are " + oldItem.getTitle() + " and " + newItem.getTitle() + " the same ?= " + (oldItem.folder.getId() == newItem.folder.getId()));
            return oldItem.folder.getId() == newItem.folder.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull FolderWithPlayables oldItem, @NonNull FolderWithPlayables newItem) {
            return
                    oldItem.folder.getTitle().equals(newItem.folder.getTitle()) &&
                    oldItem.playables.containsAll(newItem.playables) &&
                    newItem.playables.containsAll(oldItem.playables);
        }
    };

}
