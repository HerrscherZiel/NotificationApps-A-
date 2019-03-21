package com.example.notifiactionapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.net.URI;

public class MainActivity extends AppCompatActivity {

    private NotificationManager notificationManager;
    private static final int NOTIFICATION_ID=0;
    private Button notifButton;
    private Button updateButton;
    private Button cancelButton;
    private static final String NOTIFICATION_URL="http://google.com";
    private static final String ACTION_UPDATE_NOTIFICATION=
            "com.vokasi.notification.ACTION_UPDATE_NOTIFICATION";
    private static final String ACTION_CANCEL_NOTIFICATION=
            "com.vokasi.notification.ACTION_CANCEL_NOTIFICATION";
    private NotificationReciever reciever= new NotificationReciever();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notifButton = findViewById(R.id.notify);
        updateButton = findViewById(R.id.update);
        cancelButton = findViewById(R.id.cancel);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_UPDATE_NOTIFICATION);
        intentFilter.addAction(ACTION_CANCEL_NOTIFICATION);
        registerReceiver(reciever, intentFilter);


        notificationManager
                =(NotificationManager)getSystemService((NOTIFICATION_SERVICE));
        notifButton.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                sendNotification();
            }
        });
        updateButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                updateNotification();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                cancelNotification();
            }
        });
  }

    protected void onDestroy() {
        unregisterReceiver(reciever);
        super.onDestroy();
    }

    private void updateNotification(){
        Bitmap image= BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent notificationPendinfIntent=PendingIntent
                .getActivity(this, NOTIFICATION_ID, notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        Intent learnMoreIntent= new Intent(Intent.ACTION_VIEW,
                Uri.parse(NOTIFICATION_URL));
        PendingIntent learnMorePendingIntent=PendingIntent
                .getActivity(this,NOTIFICATION_ID, learnMoreIntent,
                        PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notifyBuilder=
                new NotificationCompat.Builder(this)
                        .setContentTitle("TITTLE")
                        .setContentText("Content")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentIntent(notificationPendinfIntent)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(image)
                        .setBigContentTitle("Notification Updated"));
        notificationManager.notify(NOTIFICATION_ID, notifyBuilder.build());
    }

    private void cancelNotification(){
        notificationManager.cancel(NOTIFICATION_ID);
    }

    private void sendNotification(){
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent notificationPendinfIntent=PendingIntent
                .getActivity(this, NOTIFICATION_ID, notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        Intent learnMoreIntent= new Intent(Intent.ACTION_VIEW,
                Uri.parse(NOTIFICATION_URL));
        PendingIntent learnMorePendingIntent=PendingIntent
                .getActivity(this,NOTIFICATION_ID, learnMoreIntent,
                        PendingIntent.FLAG_ONE_SHOT);

        Intent updateIntent = new Intent(ACTION_UPDATE_NOTIFICATION);
        PendingIntent updatePendingIntent = PendingIntent
                .getBroadcast(this, NOTIFICATION_ID, updateIntent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notifyBuilder=
                new NotificationCompat.Builder(this)
                        .setContentTitle("TITTLE")
                        .setContentText("Content")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .addAction(R.mipmap.ic_launcher,"Learn More",
                                learnMorePendingIntent)
                        .addAction(R.mipmap.ic_launcher,"Update", updatePendingIntent)
                        .setContentIntent(notificationPendinfIntent)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setDefaults(NotificationCompat.DEFAULT_ALL);
        notificationManager.notify(NOTIFICATION_ID, notifyBuilder.build());
    }

    private class NotificationReciever extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case ACTION_UPDATE_NOTIFICATION:
                    updateNotification();
                    break;
                case ACTION_CANCEL_NOTIFICATION:
                    cancelNotification();
                    break;
            }
        }
    }
}
