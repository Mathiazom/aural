package com.mzom.aural;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import android.os.Bundle;
import android.view.ViewGroup;

import com.mzom.aural.models.Playable;
import com.mzom.aural.utils.MultiStackManager;
import com.mzom.aural.viewmodels.FoldersViewModel;
import com.mzom.aural.viewmodels.PlayerViewModel;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "AUR-MainActivity";

    private MultiStackManager multiStackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: Investigate function
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        final FoldersViewModel foldersViewModel = new ViewModelProvider(this).get(FoldersViewModel.class);

        // Request player media controller
        final PlayerViewModel playerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);
        playerViewModel.requestMediaController(this);

        // TODO: Consider relocation
        playerViewModel.addPlayerPositionListener((position, playable) -> foldersViewModel.updatePlayable(playable));

    }

    @Override
    protected void onStart() {
        super.onStart();

        this.multiStackManager = new MultiStackManager(this,
                R.id.nav_host_fragment, R.id.nav_host_player_fragment
        );

        Navigation.findNavController(this,R.id.nav_host_player_fragment).addOnDestinationChangedListener((controller, destination, arguments) -> {

            // TODO: Refactor

            // Layout parameters of player fragment nav host
            final ViewGroup.LayoutParams playerFragmentParams = findViewById(R.id.nav_host_player_fragment).getLayoutParams();

            // Expand to full size in case of PlayerFragment, wrap content otherwise
            playerFragmentParams.height =
                    (destination.getId() == R.id.playerFragment) ?
                    ViewGroup.LayoutParams.MATCH_PARENT :
                    ViewGroup.LayoutParams.WRAP_CONTENT;

        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                multiStackManager.onBackPressed();

            }
        });

    }


}
