package com.lalaland.ecommerce;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.lalaland.ecommerce.helpers.AppConstants;
import com.lalaland.ecommerce.helpers.AppPreference;

import static com.lalaland.ecommerce.helpers.AppConstants.SIGNIN_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.SIGNUP_COUNT;

public class LalalandApplciation extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppConstants.mContext = getApplicationContext();
        int count = AppPreference.getInstance(AppConstants.mContext).getInt(SIGNUP_COUNT);
        AppPreference.getInstance(AppConstants.mContext).setInt(SIGNUP_COUNT, ++count);
        AppPreference.getInstance(AppConstants.mContext).setString(SIGNIN_TOKEN, SIGNIN_TOKEN);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
