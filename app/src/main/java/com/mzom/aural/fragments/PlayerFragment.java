package com.mzom.aural.fragments;

import android.os.Bundle;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.exoplayer2.ui.DefaultTimeBar;
import com.google.android.exoplayer2.ui.TimeBar;
import com.mzom.aural.R;
import com.mzom.aural.player.PlayablePlayerControlsHandle;
import com.mzom.aural.player.PlayablePlayerState;
import com.mzom.aural.utils.MillisFormatter;
import com.mzom.aural.viewmodels.PlayerViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class PlayerFragment extends Fragment {


    private static final String TAG = "AUR-PlayerFragment";


    private PlayerViewModel playerViewModel;


    public PlayerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_player, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.playerViewModel = new ViewModelProvider(requireActivity()).get(PlayerViewModel.class);

        final DefaultTimeBar progressBar = view.findViewById(R.id.large_player_progress);

        progressBar.addListener(new TimeBar.OnScrubListener() {
            @Override
            public void onScrubStart(@NonNull TimeBar timeBar, long position) {

            }

            @Override
            public void onScrubMove(@NonNull TimeBar timeBar, long position) {

            }

            @Override
            public void onScrubStop(@NonNull TimeBar timeBar, long position, boolean canceled) {

                Log.i(TAG,"Scrub stop: " + position + ", " + canceled);

                if(!canceled){
                    new PlayablePlayerControlsHandle(requireActivity()).seekTo(position);
                    playerViewModel.getPlayablePlayerState().setPosition(position);
                }

            }
        });

        final TextView progressText = view.findViewById(R.id.large_player_progress_text);
        final TextView durationText = view.findViewById(R.id.large_player_duration_text);

        final TextView playerTitle = view.findViewById(R.id.player_title);
        final TextView playerDescription = view.findViewById(R.id.player_description);

        final ImageButton playButton = view.findViewById(R.id.play_large);
        playButton.setOnClickListener(view1 -> new PlayablePlayerControlsHandle(requireActivity()).resume());

        final ImageButton pauseButton = view.findViewById(R.id.pause_large);
        pauseButton.setOnClickListener(view1 -> new PlayablePlayerControlsHandle(requireActivity()).pause());

        final ImageButton bufferButton = view.findViewById(R.id.buffer_large);


        final PlayerViewModel playerViewModel = new ViewModelProvider(requireActivity()).get(PlayerViewModel.class);

        final PlayablePlayerState playablePlayerState = playerViewModel.getPlayablePlayerState();

        playablePlayerState.getPlaybackState().observe(getViewLifecycleOwner(), state -> {

            switch (state){

                case PlaybackStateCompat.STATE_PLAYING:

                    playButton.setVisibility(View.GONE);
                    bufferButton.setVisibility(View.GONE);
                    pauseButton.setVisibility(View.VISIBLE);

                    if(getContext() != null){
                        progressBar.setPlayedColor(getResources().getColor(R.color.colorAccent));
                    }

                    break;

                case PlaybackStateCompat.STATE_BUFFERING:

                    playButton.setVisibility(View.GONE);
                    bufferButton.setVisibility(View.VISIBLE);
                    pauseButton.setVisibility(View.GONE);

                    if(getContext() != null){
                        progressBar.setPlayedColor(getResources().getColor(R.color.colorAccentDark));
                    }

                    break;

                default:

                    playButton.setVisibility(View.VISIBLE);
                    bufferButton.setVisibility(View.GONE);
                    pauseButton.setVisibility(View.GONE);

                    if(getContext() != null){
                        progressBar.setPlayedColor(getResources().getColor(R.color.colorAccent));
                    }

                    break;


            }

        });

        playablePlayerState.getPlayable().observe(getViewLifecycleOwner(), playable -> {
            playerTitle.setText(playable.getTitle());
            playerDescription.setText(playable.getDescription());
        });

        playablePlayerState.getDuration().observe(getViewLifecycleOwner(), duration -> {
            progressBar.setDuration(duration);

            durationText.setText(MillisFormatter.toFormat(duration, MillisFormatter.MillisFormat.HH_MM_SS));

        });

        playablePlayerState.getPosition().observe(getViewLifecycleOwner(), position -> {
            progressBar.setPosition(position);

            progressText.setText(MillisFormatter.toFormat(position, MillisFormatter.MillisFormat.HH_MM_SS));

        });

    }
}
