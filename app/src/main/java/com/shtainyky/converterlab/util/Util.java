package com.shtainyky.converterlab.util;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.shtainyky.converterlab.R;
import com.shtainyky.converterlab.activities.MainActivity;

import java.util.List;

public final class Util {
    private Util() {
    }

    public static boolean isOnline(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                if (activeNetwork.isConnected())
                    haveConnectedWifi = true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                if (activeNetwork.isConnected())
                    haveConnectedMobile = true;
            }
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public static boolean isIntentSafe(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List activities = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return activities.size() > 0;
    }

    public static void sendNotification(Context context, String msg, int id) {
        Resources resources = context.getResources();
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
        Notification notification = new NotificationCompat.Builder(context)
                .setTicker(msg)
                .setSmallIcon(R.drawable.ic_notif_update)
                .setContentTitle(resources.getString(R.string.app_name))
                .setContentText(msg)
                .setContentIntent(pi)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .build();
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(context);
        notificationManager.notify(id, notification);
        Log.i("myLog", "NotificationAboutLoading");

    }
}
