package com.mzom.aural.player;

import com.mzom.aural.models.Playable;

interface PlayablePlayerControls {

    void prepare(final Playable playable);

    void play(final Playable playable);

    void pause();

    void resume();

    void seekTo(long seekTo);

}
