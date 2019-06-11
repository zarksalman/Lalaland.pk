package com.lalaland.ecommerce.helpers;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.lalaland.ecommerce.helpers.AppConstants.DATE_FORMAT_TEXT;

public class AppUtils {

    public static Toast mToast;

    public static long getLongDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static long getOneMonthAgoDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.MONTH, -1);
        return calendar.getTimeInMillis();
    }

    public static String getStringDate() {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        SimpleDateFormat format1 = new SimpleDateFormat(DATE_FORMAT_TEXT);

        return format1.format(calendar.getTime());
    }

    public static Intent getShareIntent() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        sendIntent.putExtra(Intent.EXTRA_TEXT,
                AppConstants.SHARE_CONTENT + "\n " + AppConstants.GOOGLE_PLAY_URL + AppConstants.mContext.getPackageName());
        sendIntent.setType("text/plain");
        return sendIntent;
    }

    public static Intent getLikeUsIntent() {
        return new Intent(Intent.ACTION_VIEW, Uri.parse(AppConstants.GOOGLE_PLAY_URL + AppConstants.mContext.getPackageName()));
    }

    public static Intent getOpenUrlIntent(String url) {
        return new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    }


    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) AppConstants.mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    public static void showAToast(AppCompatActivity activity, String message) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
        mToast.show();
    }

    public static String insertNewLine(String name) {
        return name.replace(" ", "\n");
    }

    public static String formatPriceString(String price) {

        return "PKR " + price;
    }

    public static String formatName(String str) {

        return "Name: " + str;
    }

    public static String showRangePrice(String minPrice, String maxPrice) {

        StringBuilder price = new StringBuilder();

        price.append("PKR ");
        price.append(minPrice);

        if (!minPrice.equals(maxPrice)) {
            price.append("-");
            price.append(maxPrice);
        }
        return price.toString();
    }

    public static String toString(Integer value) {

        return String.valueOf(value);
    }

    public static String toLowerCase(String str) {

        return str.substring(0, 1).toUpperCase().concat(str.substring(1).toLowerCase());
    }

    public static boolean isPriceEqual(String salePrice, String actualPrice) {

        return salePrice.equals(actualPrice);
    }
    
}
