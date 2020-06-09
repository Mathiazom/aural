package com.mzom.aural.utils;

import android.app.Activity;

import java.util.Stack;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class MultiStackManager {

    private static final String TAG = "AUR-MultiStackManager";


    private final Stack<NavController> navBackStack = new Stack<>();

    private boolean isBacking = false;

    private int activeDestinationId;


    public MultiStackManager(final Activity activity, final int... navHostIds){

        for(int navHostId : navHostIds){

            final NavController navController = Navigation.findNavController(activity,navHostId);

            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {

                // Only destination's arguments have been changed, no new destination
                if(destination.getId() == activeDestinationId){
                    return;
                }

                activeDestinationId = destination.getId();

                // Do not register back actions to back stack
                if (isBacking) {
                    isBacking = false;
                    return;
                }

                // Do not register home destination to back stack (adds extra back presses to exit app)
                if (destination.getId() == controller.getGraph().getStartDestination()) {
                    return;
                }

                // Push controller related to destination change to back stack
                navBackStack.push(controller);

            });

        }

    }

    public void onBackPressed(){

        // Prevent back action from being registered in back stack
        isBacking = true;

        // Pop nav controller and pop destination from internal back stack
        navBackStack.pop().popBackStack();

    }




}
