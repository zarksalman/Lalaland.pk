package com.lalaland.ecommerce.helpers;

import android.content.Context;

import com.lalaland.ecommerce.data.models.category.Category;
import com.lalaland.ecommerce.data.models.category.City;
import com.lalaland.ecommerce.data.models.login.User;
import com.lalaland.ecommerce.data.models.userAddressBook.UserAddresses;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class AppConstants {

    public static final String BASE_URL = "https://api.uat.lalaland.pk/api/";
    public static String ABOUT_US_URL = "";
    public static String PRIVACY_POLICY_URL = "";
    public static String RETURN_POLICY_URL = "";
    public static String TERMS_AND_CONDITIONS_URL = "";
    public static String FAQ_URL = "";


    public static final String APP_NAME = "Lalaland";
    public static Context mContext;
    public static List<Category> staticCategoryList = new ArrayList<>();
    public static List<City> staticCitiesList = new ArrayList<>();
    public static List<String> testImagesUrl = new ArrayList<>();
    public static UserAddresses userAddresses;
    public static User user;
    // -------------------------------------------------------------------------------------------
    public static final Double PAYMENT_LOWEST_LIMIT = 40000.00;
// -------------------------------------------------------------------------------------------

    public static final String PREF_NAME = "com.lalaland.pk.PREF_NAME";
    public static final String SHARE_CONTENT = "Lalaland.pk";
    public static final String GOOGLE_PLAY_URL = "https://play.icon.com/store/apps/details?id=";
    public static final String DATE_FORMAT_TEXT = "yyyy-MM-dd";
    public static final String ACTION_NAME = "action_name";
    public static final String ACTION_ID = "action_id";

    public static final String PRODUCT_TYPE = "product_type";

    public static final String SALE_PRODUCT = "sale";
    public static final String PICK_OF_THE_WEEK_PRODUCTS = "picks_of_the_week";
    public static final String NEW_ARRIVAL_PRODUCTS = "new_arrival";
    public static final String BRANDS_IN_FOCUS_PRODUCTS = "brands_in_focus";
    public static final String CATEGORY_PRODUCTS = "category";
    public static final String CUSTOM_LIST_PRODUCTS = "custom_list";


    public static final String PRODUCT_ID = "product_id";
    public static final String CART_ID = "cart_id[0]"; // for only one id
    public static final String IS_WISH_LIST = "is_wish_list";
    public static final String PRODUCT_VARIATION_ID = "product_variation_group_id";
    public static final String QUANTITY = "quantity";

    public static final String PRODUCT_STORAGE_BASE_URL = "https://api.uat.lalaland.pk/storage/products/";
    public static final String BANNER_STORAGE_BASE_URL = "https://api.uat.lalaland.pk/storage/home_banners/";
    public static final String BRAND_FOCUS_STORAGE_BASE_URL = "https://api.uat.lalaland.pk/storage/featured_brands/";
    public static final String ACTION_STORAGE_BASE_URL = "https://api.uat.lalaland.pk/storage/mobile_actions/";
    public static final String USER_STORAGE_BASE_URL = "https://api.uat.lalaland.pk/storage/user/";

    public static final String FULL_NAME = "full_name";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String DATE_OF_BIRTH = "dob";
    public static final String GENDER = "gender";
    public static final String PHONE_NUMBER = "phone";
    public static final String NEW_PASSWORD = "new_password";
    public static final String OLD_PASSWORD = "old_password";
    public static final String EMAIL = "email";

    public static final String SIGN_IN = "SIGN IN";
    public static final String REGISTER = "REGISTER";
    public static final String RECOMMENDED_CAT_TOKEN = "recommended_cat";

    public static final String SEARCHES = "searches";

    public static int CASH_TRANSFER_TYPE = 1;
    public static int LOAD_HOME_FRAGMENT_INDEX = 0;
    public static final String START_INDEX = "start";
    public static final String LENGTH = "length";

    public static final String ORDER_STATUS = "order_status";
    public static final String ORDER_ID = "order_id";
    public static final String ORDER_DATE = "order_date";
    public static final String ORDER_MERCHANT = "order_merchant";
    public static final String ORDER_ADDRESS = "order_address";
    public static final String ORDER_TOTAL = "order_total";

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
    public static final String SERVER_ERROR = "PLEASE LOGIN FIRST ";
    public static final String ADD_TO_CART = "Successfully added to cart";
    public static final String ITEM_SOLD = "This item is sold out";
    public static final String ADD_TO_WISH_LIST = "Successfully added to wish list";
    public static final String NO_ITEM_IN_LIST = "No such item is in list.";
    public static final String REMOVE_FROM_WISH_LIST = "Successfully remove from wish list";
    public static final String ADD_TO_READY_PRODUCT = "Added to list";
    public static final String REMOVE_FROM_READY_PRODUCT = "Remove from list";
    public static final String REMOVED_FROM_CART = "Removed from cart";
    public static final String ITEM_SOLD_OUT = "Items sold out";

    // now for testing purpose, in future save in db
    public static String USER_NAME = "user_name";
    public static String USER_AVATAR = "user_avatar";

    public static String SIGNIN_TOKEN = "token";
    public static String CART_SESSION_TOKEN = "cart-session";
    public static final String TAG = "TAG";


    public static final int TYPE = 1;
    public static final int CONFIRM_TYPE = 2;

    public static final int FORM_SIGN_UP = 1;
    public static final int FACEBOOK_SIGN_UP_IN = 2;
    public static final int GOOGLE_SIGN_UP_IN = 3;

    public static final Pattern PASSWORD
            = Pattern.compile("^" +
            "(?=.*[0-9])" +
            "(?=.*[a-zA-Z])" +
            "(?=\\S+$)" +
            ".{6,}" +
            "$");

}


