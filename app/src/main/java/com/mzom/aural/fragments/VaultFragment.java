package com.mzom.aural.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mzom.aural.R;
import com.mzom.aural.adapters.FoldersRecyclerAdapter;
import com.mzom.aural.models.FolderWithPlayables;
import com.mzom.aural.models.Playable;
import com.mzom.aural.viewmodels.FoldersViewModel;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;


public class VaultFragment extends Fragment {


    private static final String TAG = "AUR-VaultFragment";


    private View view;

    private FoldersRecyclerAdapter foldersRecyclerAdapter;


    public VaultFragment() {
        // Required empty public constructor
    }


    private FoldersViewModel filesViewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.filesViewModel = new ViewModelProvider(requireActivity()).get(FoldersViewModel.class);

    }

    private void initFoldersRecyclerView() {

        final RecyclerView foldersRecyclerView = view.findViewById(R.id.folders_recycler_view);

        foldersRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        foldersRecyclerView.setHasFixedSize(true);

        this.foldersRecyclerAdapter = new FoldersRecyclerAdapter(folder -> {
            final NavDirections vaultToFolder = VaultFragmentDirections.actionVaultFragmentToFolderFragment(folder);
            Navigation.findNavController(view).navigate(vaultToFolder);
        });
        foldersRecyclerAdapter.setHasStableIds(true);
        foldersRecyclerView.setAdapter(foldersRecyclerAdapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        this.view = inflater.inflate(R.layout.fragment_vault, container, false);

        return view;

    }


    private final ActivityResultLauncher<Uri> multipleContentsResultLauncher = registerForActivityResult(new ActivityResultContracts.OpenDocumentTree(), treeUri -> {

        final FolderWithPlayables.Factory factory = new FolderWithPlayables.Factory(requireContext());

        final FolderWithPlayables folderWithPlayables = factory.buildFromUri(treeUri);

        filesViewModel.addFolder(folderWithPlayables);

    });

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initFoldersRecyclerView();

        filesViewModel.getFolders().observe(getViewLifecycleOwner(), folders -> {

            if (foldersRecyclerAdapter != null) {

                Log.i(TAG, "Folders change: " + folders);

                for (FolderWithPlayables folder : folders) {
                    Log.i(TAG, "Folder: " + folder);
                    Log.i(TAG, "Folder playables: " + folder.getPlayables());
                }

                foldersRecyclerAdapter.submitList(folders);
            }

        });

        //filesViewModel.addSampleFolders();

        final ImageView addFolderButton = view.findViewById(R.id.addFolderButton);
        addFolderButton.setOnClickListener(v -> {
            Log.i(TAG, "Adding test folder");
            // filesViewModel.addFolder(new FolderWithPlayables(new Folder(String.valueOf((System.currentTimeMillis()))),new ArrayList<>()));
            //filesViewModel.addSampleFolders();
            multipleContentsResultLauncher.launch(null);
        });

        view.findViewById(R.id.vaultTitle).setOnClickListener(v -> filesViewModel.getFolders().observe(getViewLifecycleOwner(), folderWithPlayablesList -> {
            Log.i(TAG,"View Model folders: " + folderWithPlayablesList);
            for(FolderWithPlayables folderWithPlayables : folderWithPlayablesList){
                Log.i(TAG,"Folder: " + folderWithPlayables);
                for(Playable playable : folderWithPlayables.getPlayables()){
                    Log.i(TAG,"Playable: " + playable);
                }
            }
        }));

    }
}
