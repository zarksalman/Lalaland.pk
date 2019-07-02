package com.lalaland.ecommerce.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

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

        String[] prices = price.split("-");

        prices[0] = NumberFormat.getNumberInstance(Locale.US).format(Double.parseDouble(prices[0]));

        if (prices.length > 1) {
            prices[1] = NumberFormat.getNumberInstance(Locale.US).format(Double.parseDouble(prices[1]));
            return "PKR " + prices[0] + "-" + prices[1];
        } else {
            return "PKR " + prices[0];
        }
    }

    public static String formatName(String str) {

        return "Name: " + str;
    }

    public static String showRangePrice(String minPrice, String maxPrice) {

        StringBuilder price = new StringBuilder();

        minPrice = NumberFormat.getNumberInstance(Locale.US).format(Float.parseFloat(minPrice));
        maxPrice = NumberFormat.getNumberInstance(Locale.US).format(Float.parseFloat(maxPrice));

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

    public static String toString(Double value) {

        return String.valueOf(value);
    }

    public static String toLowerCase(String str) {

        return str.substring(0, 1).toUpperCase().concat(str.substring(1).toLowerCase());
    }

    public static boolean isPriceEqual(String salePrice, String actualPrice) {

        return salePrice.equals(actualPrice);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String concatString(String str1, String str2) {

        return str1.concat(" ").concat(str2);
    }

    public static String concatString(Integer str1, String str2) {

        return String.valueOf(str1).concat(" ").concat(str2);
    }

    public static String concatString(String str1, Integer str2) {

        return str1.concat(" ").concat(String.valueOf(str2));
    }

    public static String concatString(Integer str1, Integer str2) {

        return String.valueOf(str1).concat(" ").concat(String.valueOf(str2));
    }

    public static Integer toInteger(String quantity) {

        return Integer.parseInt(quantity);
    }

    public static String caculatePercentage(String actualPrice, String salePrice) {

        Double aPrice, sPrice, difference, percentage;

        aPrice = Double.parseDouble(actualPrice);
        sPrice = Double.parseDouble(salePrice);

        difference = aPrice - sPrice;
        percentage = (difference * 100) / aPrice;

        return String.valueOf(Math.round(percentage)).concat("% OFF");
    }

    public static void showSalePrice(TextView textView) {
        textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);  // making price for sales

    }

}
