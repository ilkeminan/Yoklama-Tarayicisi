package com.example.yoklamataraticisi;

import android.content.BroadcastReceiver;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

public class BroadcastReceiverReminder extends BroadcastReceiver {
    private static final String channelID = "channelID";
    private static final String channelName = "Channel Name";
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent resultIntent=new Intent(context,ActivityLogin.class);
        PendingIntent resultPendingIntent=PendingIntent.getActivity(context,1,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder nb=new NotificationCompat.Builder(context);
        nb.setContentTitle("Yoklama Tarayıcısı");
        nb.setSmallIcon(R.mipmap.ic_launcher);
        nb.setPriority(NotificationCompat.PRIORITY_HIGH);
        nb.setAutoCancel(true);
        nb.setContentIntent(resultPendingIntent);
        nb.setContentText("Ders bitti. Yoklama taratabilirsin.");
        NotificationManager mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Description");
            mManager.createNotificationChannel(channel);
            nb.setChannelId(channelID);
        }
        mManager.notify(1,nb.build());
        Vibrator vibrator=(Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(2000);
    }
}
