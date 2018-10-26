package com.a3x3conect.mobile.isthreeinjava;

import android.content.Intent;
import android.util.Log;

import com.example.hari.isthreeinjava.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static android.content.ContentValues.TAG;

public class MyService2 extends FirebaseMessagingService {
    public MyService2() {
    }

//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//
//        // TODO(developer): Handle FCM messages here.
//        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
//
//        System.out.println(remoteMessage.getData());
//        Log.d(TAG, "From: " + remoteMessage.getFrom());
//
//        // Check if message contains a data payload.
//        if (remoteMessage.getData().size() > 0) {
//
//            JSONObject json = new JSONObject(remoteMessage.getData());
//
//            Log.e("json",String.valueOf(json));
//            // ...
//
//
//
//
//            Log.d(TAG, "Message data payload: " + remoteMessage.getData().toString());
//
//            Log.e("message",String.valueOf(remoteMessage.getData()));
//
//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
////                scheduleJob();
//
////                Log.e("Schedulekey",remoteMessage.getCollapseKey());
////                Log.e("Schedulefrom",remoteMessage.getFrom());
////                Log.e("Scheduleid",remoteMessage.getMessageId());
////                Log.e("Scheduletype",remoteMessage.getMessageType());
////                Log.e("Scheduleto",remoteMessage.getTo());
//
////                json = new JSONObject(remoteMessage.getData());
////
////                Log.e("json",String.valueOf(json));
//
////                Map<String, String> data = remoteMessage.getData();
////                String myCustomKey = data.get("message");
////
////
////                NotificationCompat.Builder builder =
////                        new NotificationCompat.Builder(this)
////                                .setSmallIcon(R.drawable.ic_launcher)
////                                .setContentTitle("Isthree")
////                                .setContentText(myCustomKey);
////
////                Intent notificationIntent = new Intent(this, MainActivity.class);
////                PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
////                        PendingIntent.FLAG_UPDATE_CURRENT);
////                builder.setContentIntent(contentIntent);
////
////                // Add as notification
////                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
////                manager.notify(0, builder.build());
//
////
// //                   Log.e("message",myCustomKey);
//// Toast.makeText(this, "Schedule", Toast.LENGTH_SHORT).show();
//            } else {
//                // Handle message within 10 seconds
//
//               // Log.e("handlenow","handlenow");
////                Toast.makeText(this, "Handle", Toast.LENGTH_SHORT).show();
////                handleNow();
//            }
//
//        }
//
//        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//        }
//
//        // Also if you intend on generating your own notifications as a result of a received FCM
//        // message, here is where that should be initiated. See sendNotification method below.
//
//
//    }
@Override
public void onMessageReceived(RemoteMessage remoteMessage) {
    // ...

    // TODO(developer): Handle FCM messages here.
    // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
    Log.d(TAG, "From: " + remoteMessage.getFrom());

    // Check if message contains a data payload.
    if (remoteMessage.getData().size() > 0) {
        Log.d(TAG, "Message data payload: " + remoteMessage.getData());

        if (/* Check if data needs to be processed by long running job */ true) {
            // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
          //  scheduleJob();

            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        } else {
            // Handle message within 10 seconds
           // handleNow();
        }

    }

    // Check if message contains a notification payload.
    if (remoteMessage.getNotification() != null) {
        Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
    }

    // Also if you intend on generating your own notifications as a result of a received FCM
    // message, here is where that should be initiated. See sendNotification method below.
}

}
