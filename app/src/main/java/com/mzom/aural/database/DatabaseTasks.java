package com.mzom.aural.database;

import android.os.AsyncTask;
import android.util.Log;

import com.mzom.aural.models.Folder;
import com.mzom.aural.models.FolderWithPlayables;
import com.mzom.aural.models.Playable;

import java.util.List;

/**
 *
 * Collection of tasks related to alterations of the database
 *
 */
public class DatabaseTasks {

    private static final String TAG = "AUR-DatabaseTasks";

    /**
     *
     * Updates the given {@link Playable}s in the database.
     *
     */
    public static class UpdatePlayableAsyncTask extends AsyncTask<Playable, Void, Void> {

        private final PlayableDAO playableDAO;

        public UpdatePlayableAsyncTask(PlayableDAO playableDAO) {
            this.playableDAO = playableDAO;
        }

        @Override
        protected Void doInBackground(Playable... playables) {

            for(Playable playable : playables){
                playableDAO.update(playable);
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Log.i(TAG,"Add playable completed");
        }
    }

    /**
     *
     * Inserts the given {@link Playable}s into the database.
     *
     * Remember to provide the folder id of each playable with {@link Playable#setFolderId(long)},
     * @see AddFolderWithPlayablesAsyncTask
     *
     */
    public static class AddPlayableAsyncTask extends AsyncTask<Playable, Void, Void> {

        private final PlayableDAO playableDAO;

        public AddPlayableAsyncTask(PlayableDAO playableDAO) {
            this.playableDAO = playableDAO;
        }

        @Override
        protected Void doInBackground(Playable... playables) {

            for(Playable playable : playables){
                playableDAO.insert(playable);
                Log.i(TAG,"Inserted playable with folder id: " + playable.getFolderId());
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Log.i(TAG,"Add playable completed");
        }
    }

    /**
     *
     * Inserts the {@link Folder} and {@link Playable}s of the given {@link FolderWithPlayables}.
     *
     * The Folder from {@link FolderWithPlayables#getFolder()} is first inserted into the Folder table.
     * When this completes, the Playables will be associated (database-wise) with the inserted
     * Folder by way of its now established primary key. The playables are then inserted into
     * the Playable table.
     *
     * Future retrievals through {@link FolderDAO#getFolders()} will reflect this
     * Folder/Playable relation as a FolderWithPlayables.
     *
     */
    public static class AddFolderWithPlayablesAsyncTask extends AsyncTask<FolderWithPlayables, Void, Void> {

        private final FolderDAO folderDAO;
        private final PlayableDAO playableDAO;

        private FolderWithPlayables[] folders;

        public AddFolderWithPlayablesAsyncTask(FolderDAO folderDAO, PlayableDAO playableDAO) {
            this.folderDAO = folderDAO;
            this.playableDAO = playableDAO;
        }

        @Override
        protected Void doInBackground(FolderWithPlayables... folders) {

            this.folders = folders;

            for(FolderWithPlayables folder : folders){
                final long folderId = folderDAO.insert(folder.getFolder());
                folder.getFolder().setId(folderId);
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Log.i(TAG,"Add folder completed");

            for(FolderWithPlayables folder : folders){

                Log.i(TAG,"Added folder with id: " + folder.getFolder().getId());

                // Associate (database-wise) each playable with the recently inserted folder
                for(Playable playable : folder.getPlayables()){
                    playable.setFolderId(folder.getFolder().getId());
                }

                // Insert folder's playables into database
                new AddPlayableAsyncTask(playableDAO).execute(folder.getPlayables().toArray(new Playable[]{}));
            }

        }
    }


    // TODO: ONLY REALLY NEEDED FOR DEBUGGING

    public static class DeleteAllFoldersAsyncTask extends AsyncTask<Folder, Void, Void> {

        private final FolderDAO folderDAO;
        private final PlayableDAO playableDAO;

        public DeleteAllFoldersAsyncTask(FolderDAO folderDAO, PlayableDAO playableDAO) {
            this.folderDAO = folderDAO;
            this.playableDAO = playableDAO;
        }

        @Override
        protected Void doInBackground(Folder... folders) {

            final List<FolderWithPlayables> folderWithPlayablesList = folderDAO.getFolders().getValue();

            if (folderWithPlayablesList != null) {
                for (FolderWithPlayables folder : folderWithPlayablesList) {
                    for (Playable playable : folder.getPlayables()) {
                        playableDAO.delete(playable);
                    }
                }
            }

            folderDAO.deleteAll();

            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Log.i(TAG,"Delete all folders completed");

        }
    }

}
