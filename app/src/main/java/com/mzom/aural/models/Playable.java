package com.mzom.aural.models;

import android.content.Context;
import android.net.Uri;

import com.mzom.aural.utils.FileMetaDataRetriever;

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Playable implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "folder_id")
    private long folderId;

    private static final long NO_PROGRESS = -1;

    private final String title;

    private final String description;

    private final String filePath;

    private long progress = NO_PROGRESS;

    private long duration;

    public static final DiffUtil.ItemCallback<Playable> DIFF_CALLBACK = new DiffUtil.ItemCallback<Playable>() {
        @Override
        public boolean areItemsTheSame(@NonNull Playable oldItem, @NonNull Playable newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Playable oldItem, @NonNull Playable newItem) {
            return
                    oldItem.title.equals(newItem.title) &&
                            oldItem.description.equals(newItem.description) &&
                            oldItem.filePath.equals(newItem.filePath) &&
                            oldItem.progress == newItem.progress;
        }
    };


    public static class Factory{

        final FileMetaDataRetriever fileMetaDataRetriever;

        public Factory(final Context context){
            this.fileMetaDataRetriever = new FileMetaDataRetriever(context);
        }

        public Playable buildFromUri(final Uri uri){

            return new Playable(
                    fileMetaDataRetriever.getFileName(uri),
                    fileMetaDataRetriever.getFileAlbum(uri),
                    fileMetaDataRetriever.getFileDuration(uri),
                    uri
            );

        }

    }



    public Playable(final String title, String description, final long duration, final String filePath) {
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.filePath = filePath;
    }

    public Playable(final String title, String description, final long duration, final Uri uri) {
        this(title, description, duration, uri.toString());
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public Uri getFileUri() {
        return Uri.parse(filePath);
    }

    public void setProgress(final long progress) {
        this.progress = progress;
    }

    public long getProgress() {
        return progress;
    }

    public long getDuration() {
        return duration;
    }

    @NonNull
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "@" + hashCode() + ": " + this.title;
    }

    public long getId() {
        return id;
    }

    public long getFolderId() {
        return folderId;
    }

    public void setFolderId(long folderId) {
        this.folderId = folderId;
    }

    public void setId(long id) {
        this.id = id;
    }
}
