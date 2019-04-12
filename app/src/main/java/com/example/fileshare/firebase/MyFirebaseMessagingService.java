package com.example.fileshare.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.fileshare.MainActivity;
import com.example.fileshare.R;
import com.example.fileshare.utils.Constants;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage message) {
        super.onMessageReceived(message);

        Log.d("msg", "onMessageReceived: " + message.getNotification().toString());
        sendMyNotification(message.getNotification().getBody());

    }


    private void sendMyNotification(String message) {


        //On click of notification it redirect to this Activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri soundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                //.setContentTitle("You have a new file")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && notificationManager != null) {
            notificationBuilder.setChannelId("hello");
            NotificationChannel notificationChannel = new NotificationChannel(
                    "hello",
                    getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_DEFAULT);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(R.color.background);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 400});
            notificationManager.createNotificationChannel(notificationChannel);
        }


        notificationManager.notify(0, notificationBuilder.build());
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("SAN", "token : " + s);

        SharedPreferences preferences = getApplicationContext().getSharedPreferences(Constants.LOGIN_PREF,0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.TOKEN, s);
        editor.apply();
    }
}