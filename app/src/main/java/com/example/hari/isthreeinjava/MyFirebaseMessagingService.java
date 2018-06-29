package com.example.hari.isthreeinjava;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.ViewPager.TabMyOrders;
import com.ViewPager.WalletHead;
import com.a3x3conect.mobile.isthreeinjava.GetContacts;
import com.a3x3conect.mobile.isthreeinjava.OrderHead;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.wallet.Wallet;

import org.json.JSONObject;

import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by hari on 12/6/18.
 */

    public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    private static int notificationCount=0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

      //  sendNotification(remoteMessage.getData().get("title"),remoteMessage.getData().get("body"));



        Log.d(TAG, "From: " + remoteMessage.getFrom());

//        // Check if message contains a data payload.
//        if (remoteMessage.getData().size() > 0) {
//            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
//        }
//        // ...
//
//        // TODO(developer): Handle FCM messages here.
//        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
//        Log.d(TAG, "From: " + remoteMessage.getFrom());
//        Log.d("Notifidata", "From: " + remoteMessage.getData());
//        Log.e("Key",remoteMessage.getData().get("notificationType"));
//        Log.e("body",remoteMessage.getData().get("message"));
//
//        if (remoteMessage.getNotification().getBody() != null) {
//            Log.e("FIREBASE", "Message Notification Body: " + remoteMessage.getNotification().getBody());
//            //sendNotification(remoteMessage);
//        }
//
////        // Check if message contains a data payload.
////        if (remoteMessage.getData().size() > 0) {
////            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
////
////            if (/* Check if data needs to be processed by long running job */ true) {
////                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
////                // scheduleJob();
////            } else {
////                // Handle message within 10 seconds
////                //  handleNow();
////            }
////
////        }
//
//        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//
//            Log.d(TAG, "Message data payload2: " + remoteMessage.getData());
//            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getTitle());
//            Log.d(TAG, "Message Notification Body: " + remoteMessage.getFrom());
//            Map<String, String> data = remoteMessage.getData();
//                String myCustomKey = data.get("message");
//            String notificationType = data.get("notificationType");
//            Log.d(TAG, "notificationtype: " + notificationType);
//
//
//            if (notificationType.equalsIgnoreCase("Login Message")){
//
//                NotificationCompat.Builder builder =
//                        new NotificationCompat.Builder(this)
//                                .setSmallIcon(R.drawable.logo)
//                                .setContentTitle("Isthree")
//                                .setContentText(myCustomKey);
//                Intent notificationIntent = new Intent(this, OrderHead.class);
//                PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
//                        PendingIntent.FLAG_UPDATE_CURRENT);
//                builder.setContentIntent(contentIntent);
//
//                // Add as notification
//                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                manager.notify(0, builder.build());
//                Log.e("message",myCustomKey);
//            }
//
//            if (notificationType.equalsIgnoreCase("wallet")){
//
//                NotificationCompat.Builder builder =
//                        new NotificationCompat.Builder(this)
//                                .setSmallIcon(R.drawable.logo)
//                                .setContentTitle("Isthree")
//                                .setContentText(myCustomKey);
//                Intent notificationIntent = new Intent(this, Signin.class);
//                PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
//                        PendingIntent.FLAG_UPDATE_CURRENT);
//                builder.setContentIntent(contentIntent);
//
//                // Add as notification
//                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                manager.notify(0, builder.build());
//                Log.e("message",myCustomKey);
//            }
//            else {
//
//
//                NotificationCompat.Builder builder =
//                        new NotificationCompat.Builder(this)
//                                .setSmallIcon(R.drawable.logo)
//                                .setContentTitle("Isthree")
//                                .setContentText(myCustomKey);
//                Intent notificationIntent = new Intent(this, Signin.class);
//                PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
//                        PendingIntent.FLAG_UPDATE_CURRENT);
//                builder.setContentIntent(contentIntent);
//
//                // Add as notification
//                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                manager.notify(0, builder.build());
//                Log.e("message",myCustomKey);
//
//            }
//
//
//
//
//
//
//
//        }
//
//        // Also if you intend on generating your own notifications as a result of a received FCM
//        // message, here is where that should be initiated. See sendNotification method below.
//

    }
//
//    private void sendNotification(String title, String body) {
//    }


    @Override
    public void handleIntent(Intent intent) {
        if (intent.getExtras() != null) {
            for (String key : intent.getExtras().keySet()) {
                Object value = intent.getExtras().get(key);


                String type = intent.getExtras().get("notificationType").toString();
                String msg = intent.getExtras().get("gcm.notification.body").toString();
                Log.d(TAG, "Key: " + key + " Value: " + value);
                Log.e(TAG,type);
                if (type.equalsIgnoreCase("notification")){
                    NotificationCompat.Builder builder =
                            new NotificationCompat.Builder(this)
                                    .setDefaults(Notification.DEFAULT_SOUND)
                                    .setSmallIcon(R.drawable.logo)
                                    .setAutoCancel(true)
                                    .setContentTitle("Isthree")
                                    .setContentText(msg);
                    Intent notificationIntent = new Intent(this, Splashscreen.class);
                    PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(contentIntent);
                    // Add as notification
                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(0, builder.build());
                }

                else             if (type.equalsIgnoreCase("refer")){
                    NotificationCompat.Builder builder =
                            new NotificationCompat.Builder(this)
                                    .setDefaults(Notification.DEFAULT_SOUND)
                                    .setSmallIcon(R.drawable.logo)
                                    .setContentTitle("Isthree")
                                    .setAutoCancel(true)
                                    .setContentText(msg);

                    Intent notificationIntent = new Intent(this, GetContacts.class);
                    PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(contentIntent);

                    // Add as notification
                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(0, builder.build());
                }

                else             if (type.equalsIgnoreCase("orderStatus")){
                    NotificationCompat.Builder builder =
                            new NotificationCompat.Builder(this)
                                    .setSmallIcon(R.drawable.logo)
                                    .setDefaults(Notification.DEFAULT_SOUND)
                                    .setContentTitle("Isthree")
                                    .setAutoCancel(true)
                                    .setContentText(msg);
                    Intent notificationIntent = new Intent(this, OrderHead.class);
                    PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(contentIntent);

                    // Add as notification
                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(0, builder.build());
                }
                else             if (type.equalsIgnoreCase("wallet")){
                    NotificationCompat.Builder builder =
                            new NotificationCompat.Builder(this)
                                    .setSmallIcon(R.drawable.logo)
                                    .setContentTitle("Isthree")
                                    .setDefaults(Notification.DEFAULT_SOUND)
                                    .setContentText(msg)
                                    .setAutoCancel(true);
                    Intent notificationIntent = new Intent(this, WalletHead.class);
                    PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(contentIntent);

                    // Add as notification
                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(0, builder.build());
                }

                else             if (type.equalsIgnoreCase("placeOrder")){
                    NotificationCompat.Builder builder =
                            new NotificationCompat.Builder(this)
                                    .setSmallIcon(R.drawable.logo)
                                    .setDefaults(Notification.DEFAULT_SOUND)
                                    .setAutoCancel(true)
                                    .setContentTitle("Isthree")
                                    .setContentText(msg);
                    Intent notificationIntent = new Intent(this, SchedulePickup.class);
                    PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(contentIntent);
                    // Add as notification
                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(0, builder.build());
                }
                else             if (type.equalsIgnoreCase("signup")){
                    NotificationCompat.Builder builder =
                            new NotificationCompat.Builder(this)
                                    .setDefaults(Notification.DEFAULT_SOUND)
                                    .setSmallIcon(R.drawable.logo)
                                    .setAutoCancel(true)
                                    .setContentTitle("Isthree")
                                    .setContentText(msg);
                    Intent notificationIntent = new Intent(this, Splashscreen.class);
                    PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(contentIntent);
                    // Add as notification
                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(0, builder.build());
                }
                else {
                    NotificationCompat.Builder builder =
                            new NotificationCompat.Builder(this)
                                    .setSmallIcon(R.drawable.logo)
                                    .setContentTitle("Isthree")
                                    .setDefaults(Notification.DEFAULT_SOUND)
                                    .setAutoCancel(true)
                                    .setContentText(msg);
                    Intent notificationIntent = new Intent(this, Signin.class);
                    PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(contentIntent);
                    // Add as notification
                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(0, builder.build());
                }


               // Log.e("message",myCustomKey);
            }
        }
    }
}
