package com.lalaland.ecommerce.helpers;

import android.content.Context;

import java.util.regex.Pattern;

public class AppConstants {

    public static final String BASE_URL = "https://api.uat.lalaland.pk/api/";
    public static final String TEST_BASE_URL = "https://192.168.1.54/lalaland_api/api/";
    public static final String APP_NAME = "Lalaland";
    public static Context mContext;

    public static final String PREF_NAME = "com.lalaland.pk.PREF_NAME";
    public static final String SHARE_CONTENT = "Lalaland.pk";
    public static final String GOOGLE_PLAY_URL = "https://play.icon.com/store/apps/details?id=";
    public static final String DATE_FORMAT_TEXT = "yyyy-MM-dd";
    public static final String PRODUCT_STORAGE_BASE_URL = "https://api.uat.lalaland.pk/storage/products/";
    public static final String BANNER_STORAGE_BASE_URL = "https://api.uat.lalaland.pk/storage/home_banners/";
    public static final String BRAND_FOCUS_STORAGE_BASE_URL = "https://api.uat.lalaland.pk/storage/featured_brands/";
    public static final String ACTION_STORAGE_BASE_URL = "https://api.uat.lalaland.pk/storage/mobile_actions/";
    public static final String SIGN_IN = "SIGN IN";
    public static final String SIGN_UP = "SIGN UP";
    public static final String REGISTER = "REGISTER";
    public static final String SUCCESS_CODE = "200";
    public static final String VALIDATION_FAIL_CODE = "400";
    public static final String AUTHORIZATION_FAIL_CODE = "401";

    // Toast Strings
    public static final String SUCCESS_MESSAGE = "Success";
    public static final String NO_NETWORK = "No network available";
    public static final String FB_LOGIN_CANCLED = "Login Cancel";
    public static final String WRONG_CREDENTIAL = "Account information or password is incorrect. Please try later";
    public static final String ACCOUNT_CREATION_ERROR = "Could not register at this time, please try again";
    public static final String GENERAL_ERROR = "Something went wrong please try again";
    public static String SIGNIN_TOKEN = "token";
    public static String SIGNUP_COUNT = "count";

    public static final int TYPE = 1;
    public static final int CONFIRM_TYPE = 2;

    public static final int FORM_SIGN_UP = 1;
    public static final int FACEBOOK_SIGN_UP_IN = 2;
    public static final int GOOGLE_SIGN_UP_IN = 3;

    public static final Pattern PASSWORD_PATTERN = Pattern.compile("^"
            + "(?=.*[0-9])" // at least 1 digit
            + "(?=.*[a-zA-Z])" // at least 1 letter
            + "(?=\\s+$)" // no white space
            + ".{6,}"   // at least 6 characters
            + "$");

    public static final Pattern PASSWORD
            = Pattern.compile("^" +
            "(?=.*[0-9])" +
            "(?=.*[a-zA-Z])" +
            "(?=\\S+$)" +
            ".{6,}" +
            "$");

}


