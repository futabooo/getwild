package com.futabooo.getwild;

import com.google.android.gms.wearable.WearableListenerService;

import android.app.Notification;
import android.app.NotificationManager;

public class DisconnectListenerService extends WearableListenerService {

    private static final int GET_WILD = 1;

    @Override
    public void onPeerDisconnected(com.google.android.gms.wearable.Node peer) {
        Notification.Builder notificationBuilder =
                new Notification.Builder(this).setContentTitle(getString(R.string.get_wild_and_tough))
                        .setContentText(getString(R.string.get_wild_and_tough))
                        .setVibrate(new long[]{0, 200})
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLocalOnly(true)
                        .setPriority(Notification.PRIORITY_MAX);
        Notification notification = notificationBuilder.build();
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).notify(GET_WILD, notification);
    }

    @Override
    public void onPeerConnected(com.google.android.gms.wearable.Node peer) {
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(GET_WILD);
    }
}
