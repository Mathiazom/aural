package com.mzom.aural.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.exoplayer2.ui.DefaultTimeBar;
import com.mzom.aural.R;
import com.mzom.aural.player.PlayablePlayerControlsHandle;
import com.mzom.aural.player.PlayablePlayerState;
import com.mzom.aural.viewmodels.PlayerViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class BottomPlayerFragment extends Fragment {


    private static final String TAG = "AUR-BottomPlayerFragment";


    public BottomPlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_bottom_player, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        final DefaultTimeBar progressBar = view.findViewById(R.id.exo_progress);
        final TextView playerTitle = view.findViewById(R.id.exo_playable_title);

        final ImageButton playButton = view.findViewById(R.id.exo_play);
        playButton.setOnClickListener(view1 -> new PlayablePlayerControlsHandle(requireActivity()).resume());

        final ImageButton pauseButton = view.findViewById(R.id.exo_pause);
        pauseButton.setOnClickListener(view1 -> new PlayablePlayerControlsHandle(requireActivity()).pause());

        view.setOnClickListener(Navigation.createNavigateOnClickListener(PlayerFragmentDirections.actionGlobalPlayerFragment()));

        final PlayerViewModel playerViewModel = new ViewModelProvider(requireActivity()).get(PlayerViewModel.class);

        final PlayablePlayerState playablePlayerState = playerViewModel.getPlayablePlayerState();

        // Hide player when no playable is loaded
        view.setVisibility(View.GONE);

        playablePlayerState.getPlaybackState().observe(getViewLifecycleOwner(), state -> {

            view.setVisibility(View.VISIBLE);

            switch (state){

                case PlaybackStateCompat.STATE_PLAYING:
                case PlaybackStateCompat.STATE_BUFFERING:

                    playButton.setVisibility(View.GONE);
                    pauseButton.setVisibility(View.VISIBLE);

                    break;

                default:

                    playButton.setVisibility(View.VISIBLE);
                    pauseButton.setVisibility(View.GONE);

                    break;


            }

        });

        playablePlayerState.getPlayable().observe(getViewLifecycleOwner(), playable -> playerTitle.setText(playable.getTitle()));

        playablePlayerState.getDuration().observe(getViewLifecycleOwner(), progressBar::setDuration);

        playablePlayerState.getPosition().observe(getViewLifecycleOwner(), progressBar::setPosition);

    }
}
