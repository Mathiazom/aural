package com.mzom.aural.utils;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;

public class FileMetaDataRetriever {

    private static final String TAG = "AUR-FileMetaDataRetriever";


    private final Context context;

    private final MediaMetadataRetriever mediaMetadataRetriever;

    public FileMetaDataRetriever(final Context context){

        this.context = context;

        this.mediaMetadataRetriever = new MediaMetadataRetriever();

    }

    // TODO: Search for interesting metadata types

    public String getFileName(final Uri uri){

        mediaMetadataRetriever.setDataSource(context, uri);

        return mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);

        /*final Cursor cursor = context.getContentResolver().query(uri, null, null, null,null);

        if(cursor == null){
            Log.e(TAG,"File cursor was null");
            return "";
        }

        final int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);

        cursor.moveToFirst();

        final String fileName = cursor.getString(nameIndex);

        cursor.close();

        return fileName;*/

    }

    public String getFileAuthor(final Uri uri){

        mediaMetadataRetriever.setDataSource(context, uri);

        return mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR);

    }

    public String getFileAlbum(final Uri uri){

        mediaMetadataRetriever.setDataSource(context, uri);

        return mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);

    }

    public String getFileArtist(final Uri uri){

        mediaMetadataRetriever.setDataSource(context, uri);

        return mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);

    }

    public String getFileWriter(final Uri uri){

        mediaMetadataRetriever.setDataSource(context, uri);

        return mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_WRITER);

    }

    public long getFileDuration(final Uri uri){

        mediaMetadataRetriever.setDataSource(context, uri);

        return Long.valueOf(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));

    }


}
