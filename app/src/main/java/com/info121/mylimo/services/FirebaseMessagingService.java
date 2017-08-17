package com.info121.mylimo.services;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.RemoteMessage;
import com.info121.mylimo.App;
import com.info121.mylimo.R;
import com.info121.mylimo.activities.CopyToClipboardActivity;
import com.info121.mylimo.activities.LoginActivity;
import com.info121.mylimo.activities.WebViewActivity;


import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by KZHTUN on 7/28/2017.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = "FirebaseMessagingService";
    private static final String EXTRA_MESSAGE = "extra.message";
    public final String text = "";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        //Toast.makeText(getApplicationContext(), "Message Received !" , Toast.LENGTH_SHORT).show();
//
//       if(remoteMessage.getNotification() != null) {
//           String title = (remoteMessage.getNotification().getTitle() != null) ? remoteMessage.getNotification().getTitle() : "";
//           String body = (remoteMessage.getNotification().getBody() != null) ? remoteMessage.getNotification().getBody() : "";
//
//           //showNotification(title, remoteMessage);
//           Log.e("Notification : ", body);
//       }

       if(remoteMessage.getData() !=null){
           sendNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("body"));
       }

    }

    private void sendNotification(String title, String messageBody) {

        Intent intent = new Intent(this, CopyToClipboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.my_limo_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setColor(ContextCompat.getColor(this, R.color.colorAccent))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());

    }

    private  void showNotification(String title, RemoteMessage message){
        final Context context = this;

        final Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        final Notification notification = getNotificationBuilder(context, message)
                .setSmallIcon(R.mipmap.my_limo_launcher)
                .setContentTitle(title)
                .setContentText(message.getNotification().getBody())
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                .addAction(0, "Copy", PendingIntent.getActivity(context, 0, intent, 0))
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notification);

    }

    @NonNull
    final NotificationCompat.Builder getNotificationBuilder(@NonNull Context context, RemoteMessage message) {

        return new NotificationCompat.Builder(context)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setContentIntent(createPendingIntent(context, message))
                .setLocalOnly(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE);
    }

    public static PendingIntent createPendingIntent(@NonNull Context context, @NonNull RemoteMessage message) {
        return PendingIntent.getActivity(context, message.getMessageId().hashCode(), createIntent(context, message), 0);
    }

    public static Intent createIntent(@NonNull Context context, @NonNull RemoteMessage message) {
        Intent i = new Intent(context, WebViewActivity.class);
        i.putExtra(EXTRA_MESSAGE, message);
        return i;
    }


//    private void showSnackBar(String messageBody){
//       // Snackbar snackbar = Snackbar.make(this.getApplication().getDecorView().getRootView(), messageBody, Snackbar.LENGTH_LONG);
//        View snackbarLayout = snackbar.getView();
//        TextView textView = (TextView)snackbarLayout.findViewById(android.support.design.R.id.snackbar_text);
//        textView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.my_limo_launcher, 0, 0, 0);
//        textView.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(16));
//        snackbar.show();
//    }

}
