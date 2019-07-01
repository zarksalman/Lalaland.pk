package com.lalaland.ecommerce;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.lalaland.ecommerce.data.database.LalalandDatabases;
import com.lalaland.ecommerce.helpers.AppConstants;

public class LalalandApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppConstants.mContext = getApplicationContext();
        LalalandDatabases.getInstance(AppConstants.mContext);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        //    Stetho.initializeWithDefaults(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
