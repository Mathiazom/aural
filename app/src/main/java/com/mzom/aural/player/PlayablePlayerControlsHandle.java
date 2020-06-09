package com.mzom.aural.player;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.media.session.MediaControllerCompat;

import com.mzom.aural.models.Playable;

public class PlayablePlayerControlsHandle implements PlayablePlayerControls {

    private static final String TAG = "AUR-PlayablePlayerControlsHandle";


    private final MediaControllerCompat mediaController;

    public PlayablePlayerControlsHandle(Activity activity) {
        this.mediaController = MediaControllerCompat.getMediaController(activity);
    }

    PlayablePlayerControlsHandle(MediaControllerCompat mediaController) {
        this.mediaController = mediaController;
    }


    public void play(final Playable playable){

        mediaController.sendCommand(PlayablePlayerCommands.PLAYER_COMMAND_PLAY, createPlayableBundle(playable), null);

    }

    public void prepare(final Playable playable){

        mediaController.sendCommand(PlayablePlayerCommands.PLAYER_COMMAND_PREPARE, createPlayableBundle(playable), null);

    }

    public void pause(){

        mediaController.sendCommand(PlayablePlayerCommands.PLAYER_COMMAND_PAUSE, null, null);

    }

    public void resume(){

        mediaController.sendCommand(PlayablePlayerCommands.PLAYER_COMMAND_RESUME, null, null);

    }

    public void seekTo(long seekTo){

        mediaController.sendCommand(PlayablePlayerCommands.PLAYER_COMMAND_SEEK_TO, createSeekToBundle(seekTo), null);

    }


    void handleCommand(final String command, final Bundle extras, final PlayablePlayerControls controls){

        switch (command){

            case PlayablePlayerCommands.PLAYER_COMMAND_PREPARE:

                controls.prepare(getPlayableFromCommandBundle(extras));

                break;

            case PlayablePlayerCommands.PLAYER_COMMAND_PLAY:

                controls.play(getPlayableFromCommandBundle(extras));

                break;

            case PlayablePlayerCommands.PLAYER_COMMAND_PAUSE:

                controls.pause();

                break;

            case PlayablePlayerCommands.PLAYER_COMMAND_SEEK_TO:

                controls.seekTo(getSeekToFromCommandBundle(extras));

                break;

            case PlayablePlayerCommands.PLAYER_COMMAND_RESUME:

                controls.resume();

                break;

        }

    }


    private static Bundle createPlayableBundle(final Playable playable){

        final Bundle bundle = new Bundle();

        bundle.putSerializable(PlayablePlayerCommands.PLAYABLE_EXTRA_KEY, playable);

        return bundle;

    }

    private static Bundle createSeekToBundle(final long seekTo){

        final Bundle bundle = new Bundle();

        bundle.putLong(PlayablePlayerCommands.SEEK_TO_EXTRA_KEY, seekTo);

        return bundle;

    }

    private Playable getPlayableFromCommandBundle(final Bundle bundle){

        return (Playable) bundle.getSerializable(PlayablePlayerCommands.PLAYABLE_EXTRA_KEY);

    }

    private long getSeekToFromCommandBundle(final Bundle bundle){

        return bundle.getLong(PlayablePlayerCommands.SEEK_TO_EXTRA_KEY);

    }

}
