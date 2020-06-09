package com.mzom.aural.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mzom.aural.R;
import com.mzom.aural.adapters.PlayablesRecyclerAdapter;
import com.mzom.aural.models.FolderWithPlayables;
import com.mzom.aural.models.Playable;
import com.mzom.aural.player.PlayablePlayerControlsHandle;
import com.mzom.aural.viewmodels.FoldersViewModel;
import com.mzom.aural.viewmodels.PlayerViewModel;

import java.util.Random;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class FolderFragment extends Fragment {


    private static final String TAG = "AUR-VaultFragment";


    private View view;

    private PlayablesRecyclerAdapter playablesRecyclerAdapter;

    private FolderWithPlayables folder;

    private FoldersViewModel foldersViewModel;

    private PlayerViewModel playerViewModel;


    private final ActivityResultLauncher<String[]> multipleContentsResultLauncher = registerForActivityResult(new ActivityResultContracts.OpenMultipleDocuments(),
            uriList -> {

                final Playable.Factory playableFactory = new Playable.Factory(requireContext());

                for(final Uri playableUri : uriList){

                    final Playable playable = playableFactory.buildFromUri(playableUri);

                    playable.setFolderId(folder.getFolder().getId());
                    foldersViewModel.addPlayable(playable);

                }

            });


    public FolderFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.foldersViewModel = new ViewModelProvider(requireActivity()).get(FoldersViewModel.class);

        this.folder = FolderFragmentArgs.fromBundle(getArguments()).getFolder();

        this.playerViewModel = new ViewModelProvider(requireActivity()).get(PlayerViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        this.view = inflater.inflate(R.layout.fragment_folder, container, false);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initPlayablesRecyclerView();

        foldersViewModel.getPlayablesFromFolder(folder.getFolder().getId()).observe(getViewLifecycleOwner(), playables -> {

            if (playablesRecyclerAdapter != null) {
                playablesRecyclerAdapter.submitList(playables);
            }

            final TextView folderTagline = this.view.findViewById(R.id.folder_size);
            folderTagline.setText(getResources().getString(R.string.folder_size_tagline,
                    playables == null ? 0 : playables.size()));

        });

        final TextView folderTitle = this.view.findViewById(R.id.folder_title);
        folderTitle.setText(folder.getTitle());

        final ImageView addPlayableButton = view.findViewById(R.id.addPlayableButton);
        addPlayableButton.setOnClickListener(v -> multipleContentsResultLauncher.launch(new String[]{"audio/*"}));

    }


    private String getRandomPath(){

        final String[] paths = {
                "https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/4eba7d2d-7087-4eec-a9da-a4a7dfcf2e27_0_ID192MP3.mp3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/877abeb6-caec-496e-b0e8-c5f23d71940c_0_ID192MP3.mp3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/b2bca94a-005a-4e2f-86c5-f289706a1e45_0_ID192MP3.mp3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/364bf729-a1f7-4fa4-9f5c-9b010d2f4ea7_0_ID192MP3.mp3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/eefd99e1-f58b-47cb-a1e4-feca1f07f2da_0_ID192MP3.mp3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/1c1bafff-ff21-475b-abbe-a3863170eb8c_0_ID192MP3.mp3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/74999db7-8755-4f80-8eaf-629a9fd84440_0_ID192MP3.mp3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/e50eb26c-2d13-41ea-94af-8cb9bf07f253_0_ID192MP3.mp3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/d662e5fd-283a-42a5-8c3c-bfa7bb7c47f5_0_ID192MP3.mp3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/1af50e39-a439-4dc2-a390-8d5250f560cb_0_ID192MP3.mp3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-28_0406_4019.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-28_0403_1497.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-28_0400_1218.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-27_0406_1011.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-27_0403_1084.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-27_0400_219.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-26_0403_1156.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-26_0401_1823.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-26_0400_2485.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-25_0406_3463.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-25_0403_2169.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-25_0400_2789.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-24_0406_3854.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-24_0403_568.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-24_0400_181.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-21_0406_3109.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-21_0403_3972.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-21_0400_999.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-20_0406_722.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-20_0403_2206.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-20_0400_3156.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-19_0406_1259.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-19_0403_1390.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-19_0400_773.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-18_1102_3180.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-18_1101_1063.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-18_0400_2999.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-17_0406_1416.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-17_0403_3279.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-17_0400_3212.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-14_0406_1562.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-14_0403_997.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-14_0400_1718.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-13_0406_3707.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-13_0403_1521.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-13_0400_1260.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-12_0406_1135.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-12_0403_93.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-12_0400_1532.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-11_0406_2536.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-11_0403_2473.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-11_0400_3824.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-10_0402_2806.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-10_0401_3645.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-10_0400_3118.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-07_0406_136.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-07_0403_3852.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-07_0400_3319.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-06_0406_3146.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-06_0403_3576.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-06_0400_2261.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-05_0406_1433.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-05_0403_2253.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-05_0400_1412.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-04_0406_33.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-04_0403_123.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-04_0400_772.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-03_0403_3300.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-03_0402_2252.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-06-03_0400_1280.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-05-31_0406_1601.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-05-31_0403_2523.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-05-31_0400_3534.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-05-30_0400_2274.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-05-30_0400_2518.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-05-30_0400_3119.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-05-29_0406_3821.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-05-29_0403_3740.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-05-29_0400_1315.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-05-28_0400_964.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-05-28_0400_3215.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-05-28_0400_688.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-05-27_0400_1097.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-05-27_0400_3883.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-05-27_0400_931.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-05-24_0406_2169.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-05-24_0403_3354.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-05-24_0400_839.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-05-23_0406_2303.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-05-23_0403_3928.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-05-23_0400_2673.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-05-22_0400_491.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-05-22_0400_2981.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-05-22_0400_3598.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-05-21_0400_1346.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-05-21_0400_1145.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-05-21_0400_2629.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-05-20_0400_108.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-05-20_0400_3790.MP3"
                ,"https://podkast.nrk.no/fil/radioresepsjonens_arkivpodkast/radioresepsjonens_arkivpodkast_2019-05-20_0400_2652.MP3"
        };

        final int randInt = new Random().nextInt(paths.length);

        return paths[randInt];

    }


    private void initPlayablesRecyclerView() {

        final RecyclerView playablesRecyclerView = view.findViewById(R.id.folders_recycler_view);

        playablesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        playablesRecyclerView.setHasFixedSize(true);

        this.playablesRecyclerAdapter = new PlayablesRecyclerAdapter(playable -> {

            new PlayablePlayerControlsHandle(requireActivity())
                    .play(playable);

            playerViewModel.getPlayablePlayerState().setPlayable(playable);

        });
        playablesRecyclerAdapter.setHasStableIds(true);

        playablesRecyclerView.setAdapter(playablesRecyclerAdapter);

    }


}
