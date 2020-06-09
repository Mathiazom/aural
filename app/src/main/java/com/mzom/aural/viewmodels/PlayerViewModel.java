package com.mzom.aural.viewmodels;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.os.RemoteException;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import com.mzom.aural.models.Playable;
import com.mzom.aural.player.PlayablePlayerService;
import com.mzom.aural.player.PlayablePlayerState;
import com.mzom.aural.utils.TaskIterator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class PlayerViewModel extends AndroidViewModel {

    private static final String TAG = "AUR-PlayerViewModel";

    @NonNull
    private final PlayablePlayerState playablePlayerState = new PlayablePlayerState();

    private MediaBrowserCompat mediaBrowser;

    private PlaybackStateCompat playbackState;

    private MediaMetadataCompat mediaMetadata;


    private Collection<PlayerPositionListener> playerPositionListeners = new ArrayList<>();

    public interface PlayerPositionListener{

        void onPositionChanged(final long position, final Playable playable);

    }


    private final TaskIterator observePlayerProgressIterator = new TaskIterator(new TimerTask() {

        @Override
        public void run() {

            if(playbackState == null){
                throw new RuntimeException("Playback state was null but observing was started");
            }

            if(mediaMetadata == null){
                Log.e(TAG,"Media metadata was null");
                return;
            }

            final long position = playbackState.getPosition();

            final Playable currentPlayable = playablePlayerState.getPlayable().getValue();

            if(currentPlayable != null){
                currentPlayable.setProgress(position);
                for(PlayerPositionListener playerPositionListener : playerPositionListeners){
                    playerPositionListener.onPositionChanged(position, currentPlayable);
                }
            }

            playablePlayerState.setPosition(position);
            playablePlayerState.setDuration(mediaMetadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION));

        }
    },200);

    private final MediaControllerCompat.Callback mediaControllerCallback = new MediaControllerCompat.Callback() {

        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat state) {
            super.onPlaybackStateChanged(state);

            playbackState = state;

            final boolean isPlaying = state.getState() == PlaybackStateCompat.STATE_PLAYING;

            playablePlayerState.setPlaybackState(state.getState());

            if(isPlaying){
                resumeObservePlayerProgress();
            }else{
                pauseObservePlayerProgress();
            }

        }

        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            super.onMetadataChanged(metadata);

            mediaMetadata = metadata;

        }
    };


    public PlayerViewModel(final Application application){
        super(application);

    }


    public void addPlayerPositionListener(final PlayerPositionListener playerPositionListener){
        this.playerPositionListeners.add(playerPositionListener);
    }


    private void resumeObservePlayerProgress(){

        if(observePlayerProgressIterator.isStarted()){
            return;
        }

        observePlayerProgressIterator.start();

    }

    private void pauseObservePlayerProgress(){

        observePlayerProgressIterator.stop();

    }


    @NonNull
    public PlayablePlayerState getPlayablePlayerState(){

        return this.playablePlayerState;

    }

    public void requestMediaController(final Activity activity){

        mediaBrowser = createMediaBrowser(activity);
        mediaBrowser.connect();

    }

    private MediaBrowserCompat createMediaBrowser(@NonNull final Activity activity){

        return new MediaBrowserCompat(
                getApplication(),
                new ComponentName(getApplication(), PlayablePlayerService.class),
                new MediaBrowserCompat.ConnectionCallback(){

                    @Override
                    public void onConnected() {
                        super.onConnected();

                        final MediaControllerCompat controller = createMediaController(
                                activity, mediaBrowser.getSessionToken(), mediaControllerCallback
                        );

                        if(controller == null){
                            Log.e(TAG,"Connected controller was null");
                            return;
                        }

                        MediaControllerCompat.setMediaController(activity, controller);

                    }

                    @Override
                    public void onConnectionSuspended() {
                        super.onConnectionSuspended();

                        Log.e(TAG, "Error connecting media controller");

                    }

                    @Override
                    public void onConnectionFailed() {
                        super.onConnectionFailed();

                        Log.e(TAG, "Error connecting media controller");

                    }
                },
                null
        );

    }

    private MediaControllerCompat createMediaController(
            final Activity activity,
            @NonNull final MediaSessionCompat.Token sessionToken,
            final MediaControllerCompat.Callback callback
    ) {

        try {

            final MediaControllerCompat mediaController = new MediaControllerCompat(activity, sessionToken);

            mediaController.registerCallback(callback);

            return mediaController;


        } catch (RemoteException e) {

            Log.e(TAG, "Error creating media controller");

            e.printStackTrace();

        }

        return null;

    }


}
