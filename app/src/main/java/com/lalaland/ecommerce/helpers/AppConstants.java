package com.lalaland.ecommerce.helpers;

import android.content.Context;

import com.lalaland.ecommerce.data.models.category.Category;
import com.lalaland.ecommerce.data.models.category.CategoryBrand;
import com.lalaland.ecommerce.data.models.category.City;
import com.lalaland.ecommerce.data.models.category.PayProData;
import com.lalaland.ecommerce.data.models.login.User;
import com.lalaland.ecommerce.data.models.userAddressBook.UserAddresses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class AppConstants {

    public static final String BASE_URL = "https://api.lalaland.pk/api/";
    public static final String STAGING_BASE_URL = "https://uat.api.lalaland.pk/api/";
    public static final String BASE_URL_PRODUCT_SHARE = "https://www.lalaland.pk/";

    public static String ABOUT_US_URL = "";
    public static String PRIVACY_POLICY_URL = "";
    public static String RETURN_POLICY_URL = "";
    public static String TERMS_AND_CONDITIONS_URL = "";
    public static String FAQ_URL = "";
    public static String BLOGS = "blog";

    public static int URL_TYPE = 0;
    public static String IS_FIRST_TIME = "is_first_time";

    public static final String ABOUT_US_URL_KEY = "about_us_url_key";
    public static final String PRIVACY_POLICY_URL_KEY = "privacy_policy_url_key";
    public static final String RETURN_POLICY_URL_KEY = "return_url_key";
    public static final String TERMS_AND_CONDITIONS_URL_KEY = "term_url_key";
    public static final String FAQ_URL_KEY = "faq_url_key";
    public static final String BLOGS_URL_KEY = "blogs_url_key";

    public static final String APP_NAME = "Lalaland";
    public static final String APP_DATABASE = "lalaland_database";
    public static Context mContext;
    public static List<Category> staticCategoryList = new ArrayList<>();
    public static List<City> staticCitiesList = new ArrayList<>();
    public static List<PayProData> staticBankList = new ArrayList<>();
    public static List<CategoryBrand> staticCategoryBrandsList = new ArrayList<>();
    public static Map<Integer, String> appliedFilter = new HashMap<>();
    public static Map<String, String> appliedSubFilter = new HashMap<>();
    public static String categorySubFilter = "All";

    public static List<String> testImagesUrl = new ArrayList<>();
    public static UserAddresses userAddresses, setUserAddresses;
    public static User user;
    // -------------------------------------------------------------------------------------------
    public static final Double PAYMENT_LOWEST_LIMIT = 40000.00;
// -------------------------------------------------------------------------------------------

    public static final String PREF_NAME = "com.lalaland.pk.PREF_NAME";
    public static final String SHARE_CONTENT = "Lalaland.pk";
    public static final String GOOGLE_PLAY_URL = "https://play.google.com/store/apps/details?id=";
    public static final String DATE_FORMAT_TEXT = "yyyy-MM-dd";
    public static final String ACTION_NAME = "action_name";
    public static final String ACTION_ID = "action_id";
    public static final String FILTER_ID = "id";
    public static final String FILTER_KEY = "key";
    public static final String FILTER_NAME = "filter_name";

    public static final String SELECTED_FILTER_NAME = "selected_filter_name";
    public static final String SELECTED_FILTER_ID = "selected_filter_id";
    public static final String PRICE_FILTER = "price_filter";
    public static final String CATEGORY_FILTER = "category_filter";
    public static final String BRAND_FILTER = "brand_filter";
    public static final String PRICE_RANGE = "price_range";

    public static final String PV_FILTER_ = "pv_filter";

    public static final String PRODUCT_TYPE = "product_type";

    public static final String SALE_PRODUCT = "sale";
    public static final String PICK_OF_THE_WEEK_PRODUCTS = "picks_of_the_week";
    public static final String NEW_ARRIVAL_PRODUCTS = "new_arrival";
    public static final String BRANDS_IN_FOCUS_PRODUCTS = "brands_in_focus";
    public static final String CATEGORY_PRODUCTS = "category";
    public static final String CUSTOM_LIST_PRODUCTS = "custom_list";
    public static final String SEARCH_RESULT_PRODUCTS = "search_list";
    public static final String BLOG_URL = "blog_url";

    public static final String REMAINING_QUANTITY = "remaining_qty";

    public static final String IS_FIRST_UPGRADE = "first_upgrade";
    public static final String PRODUCT_ID = "product_id";
    public static final String CART_ID = "cart_id[0]"; // for only one id
    public static final String CART_ID_1 = "cart_id[1]"; // for only one id
    public static final String CART_ITEM_ID = "cart_id"; // for only one id
    public static final String IS_WISH_LIST = "is_wish_list";
    public static final String PRODUCT_VARIATION_ID = "product_variation_group_id";
    public static final String QUANTITY = "quantity";

    public static String DEVICE_ID;
    public static String APP_BUILD_VERSION;
    public static String USER_ID;
    public static String DEVICE_NAME;
    public static String DEVICE_MODEL;
    public static String DEVICE_OS;
    public static String FCM_TOKEN = "";
    public static String DEVICE_TYPE = "ANDROID";


    public static String PRODUCT_STORAGE_BASE_URL = "";
    public static String CLAIM_STORAGE_BASE_URL = "";
    public static String MEDIUM_PRODUCT_STORAGE_BASE_URL = "";
    public static String SMALL_PRODUCT_STORAGE_BASE_URL = "";
    public static String THUMBNAIL_PRODUCT_STORAGE_BASE_URL = "";
    public static String BRAND_STORAGE_BASE_URL = "";
    public static String BRAND_FOCUS_STORAGE_BASE_URL = "";
    public static String CATEGORY_FOCUS_STORAGE_BASE_URL = "";
    public static String SIZE_CHART_STORAGE_BASE_URL = "";

    public static String CATEGORY_BRAND_STORAGE_BASE_URL;
    public static String BANNER_STORAGE_BASE_URL = "";
    public static String ACTION_STORAGE_BASE_URL = "";
    public static String USER_STORAGE_BASE_URL = "";
    public static String CUSTOM_PRODUCT_URL = "";

    public static String PRODUCT_STORAGE_BASE_URL_KEY = "product_storage_base_url_key";
    public static String CLAIM_STORAGE_BASE_URL_KEY = "claim_storage_base_url_key";
    public static String MEDIUM_PRODUCT_STORAGE_BASE_URL_KEY = "medium_storage_base_url_key";
    public static String SMALL_PRODUCT_STORAGE_BASE_URL_KEY = "small_storage_base_url_key";
    public static String THUMBNAIL_PRODUCT_STORAGE_BASE_URL_KEY = "thumbnail_storage_base_url_key";
    public static String BRAND_STORAGE_BASE_URL_KEY = "brand_storage_base_url_key";
    public static String BRAND_FOCUS_STORAGE_BASE_URL_KEY = "brand_focus_storage_base_url_key";
    public static String CATEGORY_FOCUS_STORAGE_BASE_URL_KEY = "category_storage_base_url_key";
    public static String SIZE_CHART_STORAGE_BASE_URL_KEY = "size_chart_storage_base_url_key";

    public static String CATEGORY_BRAND_STORAGE_BASE_URL_KEY = "category_brand_storage_base_url_key";
    public static String BANNER_STORAGE_BASE_URL_KEY = "banner_storage_base_url_key";
    public static String ACTION_STORAGE_BASE_URL_KEY = "action_storage_base_url_key";
    public static String USER_STORAGE_BASE_URL_KEY = "user_storage_base_url_key";
    public static String CUSTOM_PRODUCT_URL_KEY = "custom_product_storage_base_url_key";
    public static final String LOAD_HOME_FRAGMENT_INDEX_KEY = "load_fragment_key";


    public static String BLOG_URLS = "";
    public static String ADVERTISEMENT_URL = "";

    public static String COLOR_PATCH_URL = "";

    public static String BLOG_URLS_KEY = "blogs_url_key";
    public static String ADVERTISEMENT_URL_KEY = "advertisement_url_key";

    public static final String FULL_NAME = "full_name";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String DATE_OF_BIRTH = "dob";
    public static final String GENDER = "gender";
    public static final String PHONE_NUMBER = "phone";
    public static final String NEW_PASSWORD = "new_password";
    public static final String OLD_PASSWORD = "old_password";
    public static final String EMAIL = "email";
    public static final String RESET_PASSWORD_TOKEN = "token";

    public static final String SIGN_IN = "SIGN IN";
    public static final String REGISTER = "REGISTER";
    public static final String RECOMMENDED_CAT_TOKEN = "recommended_cat";
    public static final String RESET_PASSWORD = "password";
    public static final String CONFIRM_RESET_PASSWORD = "confirm_password";

    public static final String SEARCHES = "searches";

    public static int CASH_TRANSFER_TYPE = 1;
    public static int CLAIM_TYPE = 2; // 2 ==> return and 3==> replacement
    public static int LOAD_HOME_FRAGMENT_INDEX = 0;
    public static int CART_COUNTER = 0;
    public static int FILTER_COUNTER = 0;

    public static final String START_INDEX = "start";
    public static final String LENGTH = "length";

    public static final String ORDER_STATUS = "order_status";
    public static final String ORDER_ID = "order_id";
    public static final String TRANSACTION_ID = "transaction_id";
    public static final String FANCY_ORDER_ID = "fancy_order_id";
    public static final String ORDER_DATE = "order_date";
    public static final String ORDER_MERCHANT = "order_merchant";
    public static final String ORDER_ADDRESS = "order_address";
    public static final String ORDER_TOTAL = "order_total";

    public static final String SUCCESS_CODE = "200";
    public static final String VALIDATION_FAIL_CODE = "400";
    public static final String AUTHORIZATION_FAIL_CODE = "401";
    public static final String OUT_OF_STOCK_CODE = "403";
    public static final String VOUCHER_FAIL_CODE = "403";
    public static final String UPDATE_APP = "403";
    public static boolean IS_COUPON_APPLIED = false;


    // Toast Strings
    public static final String NO_NETWORK = "No network available";
    public static final String FB_LOGIN_CANCLED = "Login Cancel";
    public static final String GENERAL_ERROR = "Something went wrong please try again";
    public static final String ADD_TO_CART = "Successfully added to cart";
    public static final String ITEM_SOLD = "This item is sold out";
    public static final String ADD_TO_READY_PRODUCT = "Added to list";
    public static final String REMOVE_FROM_READY_PRODUCT = "Remove from list";
    public static final String REMOVED_FROM_CART = "Removed from cart";
    public static final String ITEM_SOLD_OUT = "Insufficient Stock";
    public static final String SUCCESS_MESSAGE = "Success";
    public static final String WRONG_CREDENTIAL = "Account information or password is incorrect. Please try later";
    public static final String ACCOUNT_CREATION_ERROR = "Could not register at this time, please try again";
    public static final String SERVER_ERROR = "PLEASE LOGIN FIRST ";
    public static final String ADD_TO_WISH_LIST = "Successfully added to wish list";
    public static final String NO_ITEM_IN_LIST = "No such item is in list.";
    public static final String REMOVE_FROM_WISH_LIST = "Successfully remove from wish list";

    // now for testing purpose, in future save in db
    public static String NAME = "name";
    public static String AVATER = "avatar";
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


