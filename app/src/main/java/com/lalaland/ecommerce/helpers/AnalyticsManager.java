package com.lalaland.ecommerce.helpers;

import android.os.Bundle;

import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;

import static com.lalaland.ecommerce.helpers.AppConstants.BASE_URL;

/**
 * Created by salmanHameed on 22/07/2019.
 */

public class AnalyticsManager {

    static AnalyticsManager INSTANCE;

    AppEventsLogger facebookAnalytics;
    FirebaseAnalytics firebaseAnalytics;

    public AnalyticsManager() {
        firebaseAnalytics = FirebaseAnalytics.getInstance(AppConstants.mContext);
        facebookAnalytics = AppEventsLogger.newLogger(AppConstants.mContext);
    }

    public static AnalyticsManager getInstance() {

        INSTANCE = new AnalyticsManager();
        return INSTANCE;
    }

    public void sendAnalytics(String eventName, Bundle bundle) {

        if (BASE_URL.equals("https://api.uat.lalaland.pk/api/"))
            return;

        getFirebaseAnalytics().logEvent(eventName, bundle);
    }

    public void sendFacebookAnalytics(String eventName, Bundle bundle) {

        if (BASE_URL.equals("https://api.uat.lalaland.pk/api/"))
            return;

        getFacebookAnalytics().logEvent(eventName, bundle);
    }

    public FirebaseAnalytics getFirebaseAnalytics() {
        if (firebaseAnalytics == null)
            firebaseAnalytics = FirebaseAnalytics.getInstance(AppConstants.mContext);
        return firebaseAnalytics;
    }

    public AppEventsLogger getFacebookAnalytics() {
        if (facebookAnalytics == null)
            facebookAnalytics = AppEventsLogger.newLogger(AppConstants.mContext);
        return facebookAnalytics;
    }
}
