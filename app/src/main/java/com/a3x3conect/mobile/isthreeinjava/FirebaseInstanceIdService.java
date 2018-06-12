package com.a3x3conect.mobile.isthreeinjava;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import static android.content.ContentValues.TAG;

/**
 * Created by hari on 6/6/18.
 */

public class FirebaseInstanceIdService extends com.google.firebase.iid.FirebaseInstanceIdService {


    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(refreshedToken);
    }


}
