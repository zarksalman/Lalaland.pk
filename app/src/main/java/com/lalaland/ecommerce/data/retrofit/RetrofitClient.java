package com.lalaland.ecommerce.data.retrofit;

import android.util.Log;

import com.lalaland.ecommerce.helpers.AppConstants;
import com.lalaland.ecommerce.helpers.AppPreference;
import com.lalaland.ecommerce.helpers.AppUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.lalaland.ecommerce.helpers.AppConstants.CART_SESSION_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.SIGNIN_TOKEN;

public class RetrofitClient {

    private static RetrofitClient retrofitClient;
    private Retrofit retrofit;

    private static final String TAG = "ServiceGenerator";
    private static final String HEADER_CACHE_CONTROL = "Cache-Control";
    private static final String HEADER_PRAGMA = "Pragma";
    private static final long cacheSize = 10 * 1024 * 1024; // 10MB
    String token, cartSession;

    AppPreference appPreference;

    private RetrofitClient() {

        appPreference = AppPreference.getInstance(AppConstants.mContext);

        retrofit = new Retrofit.Builder()
                .baseUrl(AppUtils.getBaseUrl())
                .client(okHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static RetrofitClient getInstance() {

        if (retrofitClient == null) {
            retrofitClient = new RetrofitClient();
        }

        AppConstants.DEVICE_ID = AppUtils.getDeviceId();
        AppConstants.APP_BUILD_VERSION = AppUtils.getBuildVersion();
        AppConstants.DEVICE_NAME = AppUtils.getDeviceName();
        AppConstants.DEVICE_MODEL = AppUtils.getDeviceModel();
        AppConstants.DEVICE_OS = AppUtils.getDeviceOS();
        AppConstants.DEVICE_TYPE = "ANDROID";
        AppConstants.FCM_TOKEN = "";
        AppConstants.USER_ID = "";

        return retrofitClient;
    }

    private OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .cache(cache())
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor()) // used if network off OR on
                .addNetworkInterceptor(networkInterceptor()) // only used when network is on
                .addInterceptor(offlineInterceptor())
                .build();
    }

    private HttpLoggingInterceptor httpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor =
                new HttpLoggingInterceptor(message -> Log.d(TAG, "log: http log: " + message));
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    /**
     * This interceptor will be called ONLY if the network is available
     *
     * @return
     */
    private Interceptor networkInterceptor() {
        return chain -> {
            Log.d(TAG, "network interceptor: called.");

            token = appPreference.getString(SIGNIN_TOKEN);
            cartSession = appPreference.getString(CART_SESSION_TOKEN);

            Response response = chain.proceed(chain.request());

            CacheControl cacheControl = new CacheControl.Builder()
                    .maxAge(1, TimeUnit.HOURS)
                    .build();

            /*.addHeader(CART_SESSION_TOKEN, cartSession)
            .addHeader(SIGNIN_TOKEN, token)
            */

            return response.newBuilder()
                    .removeHeader(HEADER_PRAGMA)
                    .removeHeader(HEADER_CACHE_CONTROL)
                    .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                    /*                .header("device-id", AppConstants.DEVICE_ID)
                                    .header("app-version", AppConstants.APP_BUILD_VERSION)
                                    .header("user-id", AppConstants.USER_ID)
                                    .header("device-name", AppConstants.DEVICE_NAME)
                                    .header("device-model", AppConstants.DEVICE_MODEL)
                                    .header("device-OS-version", AppConstants.DEVICE_OS)
                                    .header("fcm-token", AppConstants.FCM_TOKEN)
                                    .header("device-type", AppConstants.DEVICE_TYPE)*/

                    .build();


/*            userInfo.put("app-version", AppConstants.APP_BUILD_VERSION);
            userInfo.put("user-id", AppConstants.USER_ID);
            userInfo.put("device-name", AppConstants.DEVICE_NAME);
            userInfo.put("device-OS-version", AppConstants.DEVICE_OS);
            userInfo.put("fcm-token", AppConstants.FCM_TOKEN);
            userInfo.put("device-type", AppConstants.DEVICE_TYPE);
            userInfo.put(SIGNIN_TOKEN, token);
            userInfo.put(CART_SESSION_TOKEN, cartSession);*/

        };
    }

    private static Interceptor offlineInterceptor() {
        return chain -> {
            Log.d(TAG, "offline interceptor: called.");
            Request request = chain.request();

            // prevent caching when network is on. For that we use the "networkInterceptor"
            if (!AppUtils.isNetworkAvailable()) {
                CacheControl cacheControl = new CacheControl.Builder()
                        .maxStale(7, TimeUnit.DAYS)
                        .build();

                request = request.newBuilder()
                        .removeHeader(HEADER_PRAGMA)
                        .removeHeader(HEADER_CACHE_CONTROL)
                        .cacheControl(cacheControl)
                        .build();
            }

            return chain.proceed(request);
        };
    }

    private static Cache cache() {
        return new Cache(new File(AppConstants.mContext.getCacheDir(), AppConstants.APP_NAME), cacheSize);
    }

    private OkHttpClient getOkHttpClient() {

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();

        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();

        return okHttpClient;
    }

    public LalalandServiceApi createClient() {
        return retrofit.create(LalalandServiceApi.class);
    }
}
