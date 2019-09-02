package com.lalaland.ecommerce;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
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
