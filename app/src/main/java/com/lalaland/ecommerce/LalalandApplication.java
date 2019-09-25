package com.lalaland.ecommerce;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.messaging.FirebaseMessaging;
import com.lalaland.ecommerce.Receivers.ConnectivityReceiver;
import com.lalaland.ecommerce.data.database.LalalandDatabases;
import com.lalaland.ecommerce.helpers.AppConstants;

public class LalalandApplication extends Application {

    private static LalalandApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        AppConstants.mContext = getApplicationContext();
        FacebookSdk.sdkInitialize(AppConstants.mContext);
        LalalandDatabases.getInstance(AppConstants.mContext);
        AppEventsLogger.activateApp(this);

        // init cloud messaging for firebase
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

        /*
        *
        * For Specific top initialization
        * *
               FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(task -> {
*//*                    String msg = "Success";
                    if (!task.isSuccessful()) {
                        msg = "Failed";
                    }

                    Toast.makeText(SplashActivity.this, msg, Toast.LENGTH_SHORT).show();*//*
                });*/

        //    Stetho.initializeWithDefaults(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static synchronized LalalandApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
