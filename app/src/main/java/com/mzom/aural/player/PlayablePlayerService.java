package com.mzom.aural.player;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.mzom.aural.MainActivity;
import com.mzom.aural.utils.MillisFormatter;
import com.mzom.aural.R;
import com.mzom.aural.models.Playable;
import com.mzom.aural.utils.TaskIterator;

import java.util.List;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.media.MediaBrowserServiceCompat;

public class PlayablePlayerService extends MediaBrowserServiceCompat {


    private static final String TAG = "AUR-PlayerService";


    private static final String EMPTY_MEDIA_ROOT_ID = "EMPTY_MEDIA_ROOT_ID";


    private MediaSessionCompat mediaSession = null;

    private PlayablePlayer playablePlayer = null;


    private PlaybackStateCompat.Builder stateBuilder;

    private final MediaMetadataCompat.Builder mediaMetadataBuilder = new MediaMetadataCompat.Builder();


    private final MediaSessionCompat.Callback mediaSessionCallback = new MediaSessionCompat.Callback() {
        @Override
        public void onCommand(String command, Bundle extras, ResultReceiver cb) {
            super.onCommand(command, extras, cb);

            new PlayablePlayerControlsHandle(mediaSession.getController())
                    .handleCommand(command, extras, new PlayablePlayerControls() {

                        @Override
                        public void prepare(Playable playable) {
                            playablePlayer.prepare(playable);
                        }

                        @Override
                        public void play(final Playable playable) {

                            Log.i(TAG, "Received play command: " + playable);
                            playablePlayer.play(playable);

                        }

                        @Override
                        public void pause() {
                            playablePlayer.pause();
                        }

                        @Override
                        public void resume() {
                            playablePlayer.resume();
                        }

                        @Override
                        public void seekTo(long seekTo) {
                            playablePlayer.getPlayer().seekTo(seekTo);
                        }

                    });

        }
    };

    private final TimerTask updatePlaybackStateTask = new TimerTask() {
        @Override
        public void run() {

            if (mediaSession == null || mediaSession.getController() == null) {
                Log.e(TAG, "Media session or controller was null");
                return;
            }

            updatePlaybackState();

            updateMetadata();

        }
    };

    private final TaskIterator updatePlaybackStateIterator = new TaskIterator(updatePlaybackStateTask, 200);

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            // Not supported with this SDK
            return;
        }

        final NotificationManager notificationManager = getSystemService(NotificationManager.class);

        if (notificationManager == null) {
            Log.e(TAG, "Notification manager was null");
            return;
        }

        notificationManager.createNotificationChannel(NotificationConstants.NOTIFICATION_CHANNEL());

    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(TAG, "Creating player service");

        initializePlayer();

        mediaSession = new MediaSessionCompat(getBaseContext(), TAG);
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PLAY_PAUSE);
        mediaSession.setPlaybackState(stateBuilder.build());

        mediaSession.setCallback(mediaSessionCallback);

        mediaSession.setActive(true);


        setSessionToken(mediaSession.getSessionToken());

        createNotificationChannel();

        final PlayerNotificationManager playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(
                getApplicationContext(), NotificationConstants.PLAYABLE_PLAYER_NOTIFICATION_CHANNEL_ID, R.string.app_name, R.string.app_name,
                NotificationConstants.PLAYABLE_PLAYER_NOTIFICATION_ID,
                new PlayerNotificationManager.MediaDescriptionAdapter() {
                    @NonNull
                    @Override
                    public CharSequence getCurrentContentTitle(@NonNull Player player) {
                        return playablePlayer.getPlayable().getTitle();
                    }

                    @Nullable
                    @Override
                    public PendingIntent createCurrentContentIntent(@NonNull Player player) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        return PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    }

                    @NonNull
                    @Override
                    public CharSequence getCurrentContentText(@NonNull Player player) {
                        return playablePlayer.getPlayable().getFileUri().toString();
                    }

                    @Nullable
                    @Override
                    public Bitmap getCurrentLargeIcon(@NonNull Player player, @NonNull PlayerNotificationManager.BitmapCallback callback) {
                        return null;
                    }
                }
                , new PlayerNotificationManager.NotificationListener() {
                    @Override
                    public void onNotificationPosted(int notificationId, Notification notification, boolean ongoing) {
                        startForeground(notificationId, notification);
                    }
                }
        );

        playerNotificationManager.setMediaSessionToken(getSessionToken());

        playerNotificationManager.setPlayer(playablePlayer.getPlayer());


    }

    private void initializePlayer() {

        playablePlayer = new PlayablePlayer(getApplicationContext());

        playablePlayer.addEventListener(new Player.EventListener() {

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                updatePlaybackState();
            }

            @Override
            public void onSeekProcessed() {
                updatePlaybackState();
            }

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {

                if (isPlaying) {
                    resumeUpdatePlaybackState();
                } else {
                    pauseUpdatePlaybackState();
                }

            }
        });

    }

    private void resumeUpdatePlaybackState() {

        updatePlaybackStateIterator.start();

    }

    private void pauseUpdatePlaybackState() {

        updatePlaybackStateIterator.stop();

    }

    private void updatePlaybackState() {

        mediaSession.setPlaybackState(stateBuilder
                .setState(
                        getPlaybackStateFromPlayer(playablePlayer.getPlayer()),
                        playablePlayer.getPlayer().getCurrentPosition(),
                        playablePlayer.getPlayer().getPlaybackParameters().speed
                )
                .build());

    }

    private static int getPlaybackStateFromPlayer(final ExoPlayer exoPlayer) {

        if (exoPlayer.isPlaying()) {

            return PlaybackStateCompat.STATE_PLAYING;

        }

        switch (exoPlayer.getPlaybackState()) {

            case Player.STATE_BUFFERING:

                return PlaybackStateCompat.STATE_BUFFERING;

            case Player.STATE_READY:

                return PlaybackStateCompat.STATE_PAUSED;

            case Player.STATE_ENDED:

                return PlaybackStateCompat.STATE_STOPPED;

            default:
                return PlaybackStateCompat.STATE_NONE;

        }

    }

    private void updateMetadata() {

        mediaSession.setMetadata(
                mediaMetadataBuilder
                        .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, playablePlayer.getPlayable() != null ? playablePlayer.getPlayable().getTitle() : "")
                        .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE,
                                MillisFormatter.toFormat(playablePlayer.getPlayer().getDuration() - playablePlayer.getPlayer().getCurrentPosition(), MillisFormatter.MillisFormat.MIN_TEXT) +
                                        " left"
                        )
                        .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION, "Playing")
                        .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, playablePlayer.getPlayer().getDuration())
                        .build()
        );

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        playablePlayer.pause();

    }

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {

        // Does not allow browsing
        return new BrowserRoot(EMPTY_MEDIA_ROOT_ID, null);

    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {

        // Does not allow browsing
        result.sendResult(null);

    }

}
