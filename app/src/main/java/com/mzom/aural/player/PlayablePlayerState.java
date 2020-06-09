package com.mzom.aural.player;

import com.mzom.aural.models.Playable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class PlayablePlayerState {


    private final MutableLiveData<Playable> playable = new MutableLiveData<>();

    private final MutableLiveData<Long> position = new MutableLiveData<>();

    private final MutableLiveData<Long> duration = new MutableLiveData<>();

    private final MutableLiveData<Integer> playbackState = new MutableLiveData<>();



    public void setPlayable(final Playable playable){

        this.playable.setValue(playable);

    }

    public LiveData<Playable> getPlayable(){

        return this.playable;

    }

    public void updatePlayablePosition(final long position){

        final Playable currentPlayable = playable.getValue();

        if(currentPlayable == null){
            return;
        }

        currentPlayable.setProgress(position);

    }

    public void setPosition(final Long position){

        this.position.postValue(position);

    }

    public LiveData<Long> getPosition(){

        return this.position;

    }

    public void setDuration(final Long duration) {
        this.duration.postValue(duration);
    }

    public LiveData<Long> getDuration() {
        return duration;
    }

    public LiveData<Integer> getPlaybackState() {
        return playbackState;
    }

    public void setPlaybackState(final int playbackState) {
        this.playbackState.postValue(playbackState);
    }




}
