package com.mzom.aural.player;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

class NotificationConstants {

    static final String PLAYABLE_PLAYER_NOTIFICATION_CHANNEL_ID = "PlayablePlayerService_CHANNEL";
    private static final String PLAYABLE_PLAYER_NOTIFICATION_NAME = "PlayablePlayerService";
    private static final String PLAYABLE_PLAYER_NOTIFICATION_DESCRIPTION = "PlayablePlayerService";

    @RequiresApi(api = Build.VERSION_CODES.O)
    static NotificationChannel NOTIFICATION_CHANNEL(){
        final NotificationChannel notificationChannel = new NotificationChannel(
                PLAYABLE_PLAYER_NOTIFICATION_CHANNEL_ID, PLAYABLE_PLAYER_NOTIFICATION_NAME, NotificationManager.IMPORTANCE_DEFAULT
        );
        notificationChannel.setDescription(PLAYABLE_PLAYER_NOTIFICATION_DESCRIPTION);
        return notificationChannel;
    }

    static final int PLAYABLE_PLAYER_NOTIFICATION_ID = 69;

}
