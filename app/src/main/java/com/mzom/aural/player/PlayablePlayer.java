package com.mzom.aural.player;

import android.content.Context;
import android.util.Log;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.mzom.aural.models.Playable;

class PlayablePlayer {


    private static final String TAG = "AUR-PlayablePlayer";

    private Playable playable;

    public ExoPlayer getPlayer() {
        return player;
    }

    public Playable getPlayable() {
        return playable;
    }

    private final ExoPlayer player;

    private final ProgressiveMediaSource.Factory mediaSourceFactory;

    PlayablePlayer(final Context context) {

        this.player = new SimpleExoPlayer.Builder(context).build();

        this.mediaSourceFactory = new ProgressiveMediaSource.Factory(

                new DefaultDataSourceFactory(
                        context,
                        Util.getUserAgent(
                                context,
                                (context).getApplicationInfo().name
                        )
                )

        );

    }


    void addEventListener(final Player.EventListener eventListener){

        this.player.addListener(eventListener);

    }

    void prepare(final Playable playable){

        Log.i(TAG,"Preparing " + playable);

        this.playable = playable;

        final MediaSource playableSource = mediaSourceFactory
                .createMediaSource(playable.getFileUri());

        Log.i(TAG,"Playable uri: " + playable.getFileUri());
        Log.i(TAG,"Playable source: " + playableSource);

        player.prepare(playableSource);

    }

    void play(final Playable playable){

        Log.i(TAG,"Playing " + playable);

        if(this.playable != playable){
            prepare(playable);
        }

        player.setPlayWhenReady(true);

    }

    void pause(){

        player.setPlayWhenReady(false);

    }

    void resume(){

        player.setPlayWhenReady(true);

    }

}
