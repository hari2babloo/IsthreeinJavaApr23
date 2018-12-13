package com.example.hari.isthreeinjava;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.ViewPager.FeedbackNotification;
import com.ViewPager.WalletHead;
import com.a3x3conect.mobile.isthreeinjava.GetContacts;
import com.a3x3conect.mobile.isthreeinjava.Notifications;
import com.a3x3conect.mobile.isthreeinjava.OrderHead;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.net.URL;

/**
 * Created by hari on 12/6/18.
 */

    public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Bitmap image;
    String bannerimage;
    NotificationCompat.Builder builder;
    private static int notificationCount=0;

    //TinyDB tinyDB = new TinyDB(this);
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

      //  sendNotification(remoteMessage.getData().get("title"),remoteMessage.getData().get("body"));



        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "notification: " + remoteMessage.getData());

//        tinyDB = new TinyDB(this);

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
//                builder =
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
//                builder =
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
//                builder =
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
                Log.d(TAG, "Key24: " + key + " Value: " + value);
                String CHANNEL_ID = "Isthree";
                String type = intent.getExtras().get("notificationType").toString();
                String msg = intent.getExtras().get("gcm.notification.body").toString();
                
                bannerimage  =  intent.getExtras().get("image").toString();


                if (bannerimage.equalsIgnoreCase("0")){

                        image = null;

                }

                else {


                    try {
                        URL url = new URL(bannerimage);
                        image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    } catch(IOException e) {
                        System.out.println(e);
                    }
//                    return; // or break, continue, throw

                }





                preferences = PreferenceManager.getDefaultSharedPreferences(this);
                notificationCount = preferences.getInt("count",0);
                editor = preferences.edit();
                editor.putInt("count",notificationCount+5);
                editor.apply();
                Log.e(TAG, String.valueOf(notificationCount));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    CharSequence name = getString(R.string.project_id);
                    String description = getString(R.string.baseurl);
                    int importance = NotificationManager.IMPORTANCE_DEFAULT;
                    NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                    channel.setDescription(description);
                    // Register the channel with the system; you can't change the importance
                    // or other notification behaviors after this
                    NotificationManager notificationManager = getSystemService(NotificationManager.class);
                    notificationManager.createNotificationChannel(channel);
                }
//
//                 builder =
//                        new (this, CHANNEL_ID)
//                                .setDefaults(Notification.DEFAULT_SOUND)
//                                .setSmallIcon(R.drawable.logo)
//                                .setAutoCancel(true)
//                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                                .setContentTitle("Isthree")
//                                .setContentText(msg);
//                Intent notificationIntent = new Intent(this, Splashscreen.class);
//                PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
//                        PendingIntent.FLAG_UPDATE_CURRENT);
//                builder.setContentIntent(contentIntent);
//                // Add as notification
//                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                manager.notify(0, builder.build());


                if (TextUtils.isDigitsOnly(type)){

                    if (bannerimage.equalsIgnoreCase("0")){

                        builder =
                                new NotificationCompat.Builder(this, CHANNEL_ID)
                                        .setSmallIcon(R.drawable.logo)
                                        .setDefaults(Notification.DEFAULT_SOUND)
                                        .setContentTitle("Isthree")
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                        .setStyle(new NotificationCompat.BigTextStyle()
                                                .bigText(msg))
                                        .setAutoCancel(true)
                                        .setContentText(msg);
                    }

                    else {



                        builder =
                                new NotificationCompat.Builder(this, CHANNEL_ID)
                                        .setSmallIcon(R.drawable.logo)
                                        .setDefaults(Notification.DEFAULT_SOUND)
                                        .setContentTitle("Isthree")
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                        .setStyle(new NotificationCompat.BigTextStyle()
                                                .bigText(msg))


                                        .setStyle(new NotificationCompat.BigPictureStyle()
                                                .bigPicture(image))
                                        .setAutoCancel(true)
                                        .setContentText(msg);
                    }

                    //tinyDB.putString("type",intent.getExtras().get("notificationType").toString());


                    Log.e("type",intent.getExtras().get("notificationType").toString());
                    Intent notificationIntent = new Intent(this, FeedbackNotification.class);

                     preferences = PreferenceManager.getDefaultSharedPreferences(this);
                     editor = preferences.edit();

                     editor.putString("type",type);
                     editor.apply();
//                    editor = preferences.edit();

//                    editor.apply();//

                  //  notificationIntent.putExtra("type",type);
                    PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(contentIntent);
                    // Add as notification
                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(0, builder.build());


                }
                else

                if (type.equalsIgnoreCase("notification")){

                    if (bannerimage.equalsIgnoreCase("0")){

                        builder =
                                new NotificationCompat.Builder(this, CHANNEL_ID)
                                        .setSmallIcon(R.drawable.logo)
                                        .setDefaults(Notification.DEFAULT_SOUND)
                                        .setContentTitle("Isthree")
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                        .setStyle(new NotificationCompat.BigTextStyle()
                                                .bigText(msg))
                                        .setAutoCancel(true)
                                        .setContentText(msg);
                    }

                    else {



                        builder =
                                new NotificationCompat.Builder(this, CHANNEL_ID)
                                        .setSmallIcon(R.drawable.logo)
                                        .setDefaults(Notification.DEFAULT_SOUND)
                                        .setContentTitle("Isthree")
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                        .setStyle(new NotificationCompat.BigTextStyle()
                                                .bigText(msg))


                                        .setStyle(new NotificationCompat.BigPictureStyle()
                                                .bigPicture(image))
                                        .setAutoCancel(true)
                                        .setContentText(msg);
                    }
                    Intent notificationIntent = new Intent(this, Notifications.class);


                    PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(contentIntent);
                    // Add as notification
                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(0, builder.build());
                }

                else

                if (type.equalsIgnoreCase("promotion")){

                    if (bannerimage.equalsIgnoreCase("0")){

                        builder =
                                new NotificationCompat.Builder(this, CHANNEL_ID)
                                        .setSmallIcon(R.drawable.logo)
                                        .setDefaults(Notification.DEFAULT_SOUND)
                                        .setContentTitle("Isthree")
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                        .setStyle(new NotificationCompat.BigTextStyle()
                                                .bigText(msg))
                                        .setAutoCancel(true)
                                        .setContentText(msg);
                    }

                    else {



                        builder =
                                new NotificationCompat.Builder(this, CHANNEL_ID)
                                        .setSmallIcon(R.drawable.logo)
                                        .setDefaults(Notification.DEFAULT_SOUND)
                                        .setContentTitle("Isthree")
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                        .setStyle(new NotificationCompat.BigTextStyle()
                                                .bigText(msg))


                                        .setStyle(new NotificationCompat.BigPictureStyle()
                                                .bigPicture(image))
                                        .setAutoCancel(true)
                                        .setContentText(msg);
                    }
                    Intent notificationIntent = new Intent(this, Splashscreen.class);


                    PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(contentIntent);
                    // Add as notification
                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(0, builder.build());
                }

                else
                    if (type.equalsIgnoreCase("refer")){

                    if (bannerimage.equalsIgnoreCase("0")){

                        builder =
                                new NotificationCompat.Builder(this, CHANNEL_ID)
                                        .setSmallIcon(R.drawable.logo)
                                        .setDefaults(Notification.DEFAULT_SOUND)
                                        .setContentTitle("Isthree")
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                        .setStyle(new NotificationCompat.BigTextStyle()
                                                .bigText(msg))
                                        .setAutoCancel(true)
                                        .setContentText(msg);
                    }

                    else {



                        builder =
                                new NotificationCompat.Builder(this, CHANNEL_ID)
                                        .setSmallIcon(R.drawable.logo)
                                        .setDefaults(Notification.DEFAULT_SOUND)
                                        .setContentTitle("Isthree")
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                        .setStyle(new NotificationCompat.BigTextStyle()
                                                .bigText(msg))


                                        .setStyle(new NotificationCompat.BigPictureStyle()
                                                .bigPicture(image))
                                        .setAutoCancel(true)
                                        .setContentText(msg);
                    }

                    Intent notificationIntent = new Intent(this, GetContacts.class);


                    PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(contentIntent);

                    // Add as notification
                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(0, builder.build());
                }

                else             if (type.equalsIgnoreCase("orderStatus")){



if (bannerimage.equalsIgnoreCase("0")){

     builder =
            new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.logo)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setContentTitle("Isthree")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(msg))
                    .setAutoCancel(true)
                    .setContentText(msg);
}

else {



     builder =
            new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.logo)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setContentTitle("Isthree")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(msg))


                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(image))
                    .setAutoCancel(true)
                    .setContentText(msg);
}

               

                    Intent notificationIntent = new Intent(this, OrderHead.class);

                    PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(contentIntent);

                    // Add as notification
                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(0, builder.build());
                }



                else             if (type.equalsIgnoreCase("wallet")){
                    if (bannerimage.equalsIgnoreCase("0")){

                        builder =
                                new NotificationCompat.Builder(this, CHANNEL_ID)
                                        .setSmallIcon(R.drawable.logo)
                                        .setDefaults(Notification.DEFAULT_SOUND)
                                        .setContentTitle("Isthree")
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                        .setStyle(new NotificationCompat.BigTextStyle()
                                                .bigText(msg))
                                        .setAutoCancel(true)
                                        .setContentText(msg);
                    }

                    else {



                        builder =
                                new NotificationCompat.Builder(this, CHANNEL_ID)
                                        .setSmallIcon(R.drawable.logo)
                                        .setDefaults(Notification.DEFAULT_SOUND)
                                        .setContentTitle("Isthree")
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                        .setStyle(new NotificationCompat.BigTextStyle()
                                                .bigText(msg))


                                        .setStyle(new NotificationCompat.BigPictureStyle()
                                                .bigPicture(image))
                                        .setAutoCancel(true)
                                        .setContentText(msg);
                    }

                    Intent notificationIntent = new Intent(this, WalletHead.class);

                    PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(contentIntent);

                    // Add as notification
                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(0, builder.build());
                }




                else             if (type.equalsIgnoreCase("placeOrder")){
                    if (bannerimage.equalsIgnoreCase("0")){

                        builder =
                                new NotificationCompat.Builder(this, CHANNEL_ID)
                                        .setSmallIcon(R.drawable.logo)
                                        .setDefaults(Notification.DEFAULT_SOUND)
                                        .setContentTitle("Isthree")
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                        .setStyle(new NotificationCompat.BigTextStyle()
                                                .bigText(msg))
                                        .setAutoCancel(true)
                                        .setContentText(msg);
                    }

                    else {



                        builder =
                                new NotificationCompat.Builder(this, CHANNEL_ID)
                                        .setSmallIcon(R.drawable.logo)
                                        .setDefaults(Notification.DEFAULT_SOUND)
                                        .setContentTitle("Isthree")
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                        .setStyle(new NotificationCompat.BigTextStyle()
                                                .bigText(msg))


                                        .setStyle(new NotificationCompat.BigPictureStyle()
                                                .bigPicture(image))
                                        .setAutoCancel(true)
                                        .setContentText(msg);
                    }
                    Intent notificationIntent = new Intent(this, SchedulePickup.class);

                    PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(contentIntent);
                    // Add as notification
                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(0, builder.build());
                }



                else             if (type.equalsIgnoreCase("signup")){
                    if (bannerimage.equalsIgnoreCase("0")){

                        builder =
                                new NotificationCompat.Builder(this, CHANNEL_ID)
                                        .setSmallIcon(R.drawable.logo)
                                        .setDefaults(Notification.DEFAULT_SOUND)
                                        .setContentTitle("Isthree")
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                        .setStyle(new NotificationCompat.BigTextStyle()
                                                .bigText(msg))
                                        .setAutoCancel(true)
                                        .setContentText(msg);
                    }

                    else {



                        builder =
                                new NotificationCompat.Builder(this, CHANNEL_ID)
                                        .setSmallIcon(R.drawable.logo)
                                        .setDefaults(Notification.DEFAULT_SOUND)
                                        .setContentTitle("Isthree")
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                        .setStyle(new NotificationCompat.BigTextStyle()
                                                .bigText(msg))


                                        .setStyle(new NotificationCompat.BigPictureStyle()
                                                .bigPicture(image))
                                        .setAutoCancel(true)
                                        .setContentText(msg);
                    }
                    Intent notificationIntent = new Intent(this, Splashscreen.class);

                    PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(contentIntent);
                    // Add as notification
                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(0, builder.build());
                }





                else {


                    if (bannerimage.equalsIgnoreCase("0")){

                        builder =
                                new NotificationCompat.Builder(this, CHANNEL_ID)
                                        .setSmallIcon(R.drawable.logo)
                                        .setDefaults(Notification.DEFAULT_SOUND)
                                        .setContentTitle("Isthree")
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                        .setStyle(new NotificationCompat.BigTextStyle()
                                                .bigText(msg))
                                        .setAutoCancel(true)
                                        .setContentText(msg);
                    }

                    else {



                        builder =
                                new NotificationCompat.Builder(this, CHANNEL_ID)
                                        .setSmallIcon(R.drawable.logo)
                                        .setDefaults(Notification.DEFAULT_SOUND)
                                        .setContentTitle("Isthree")
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                        .setStyle(new NotificationCompat.BigTextStyle()
                                                .bigText(msg))


                                        .setStyle(new NotificationCompat.BigPictureStyle()
                                                .bigPicture(image))
                                        .setAutoCancel(true)
                                        .setContentText(msg);
                    }
                    Intent notificationIntent = new Intent(this, Signin.class);

                    PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(contentIntent);
                    // Add as notification
                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(0, builder.build());
                }

//
               // Log.e("message",myCustomKey);
            }
        }
    }
}
