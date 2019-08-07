package com.lalaland.ecommerce.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.data.models.category.Category;
import com.lalaland.ecommerce.data.models.category.CategoryData;
import com.lalaland.ecommerce.databinding.ActivitySplashBinding;
import com.lalaland.ecommerce.helpers.AppConstants;
import com.lalaland.ecommerce.helpers.AppPreference;
import com.lalaland.ecommerce.helpers.AppUtils;
import com.lalaland.ecommerce.interfaces.NetworkInterface;
import com.lalaland.ecommerce.viewModels.products.CategoryViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.lalaland.ecommerce.helpers.AppConstants.ABOUT_US_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.ACTION_STORAGE_BASE_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.ADVERTISEMENT_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.BANNER_STORAGE_BASE_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.BLOGS;
import static com.lalaland.ecommerce.helpers.AppConstants.BLOG_URLS;
import static com.lalaland.ecommerce.helpers.AppConstants.BRAND_FOCUS_STORAGE_BASE_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.BRAND_STORAGE_BASE_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.CATEGORY_FOCUS_STORAGE_BASE_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.CUSTOM_PRODUCT_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.FAQ_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.IS_FIRST_TIME;
import static com.lalaland.ecommerce.helpers.AppConstants.MEDIUM_PRODUCT_STORAGE_BASE_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.PRODUCT_STORAGE_BASE_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.RETURN_POLICY_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.SMALL_PRODUCT_STORAGE_BASE_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.TERMS_AND_CONDITIONS_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.THUMBNAIL_PRODUCT_STORAGE_BASE_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.USER_STORAGE_BASE_URL;

public class SplashActivity extends AppCompatActivity implements NetworkInterface {

    ActivitySplashBinding activitySplashBinding;
    Map<String, String> headers = new HashMap<>();
    AppPreference appPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        activitySplashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);

        appPreference = AppPreference.getInstance(this);
        headers.put("cart-session", appPreference.getString(AppConstants.CART_SESSION_TOKEN));
        headers.put("token", appPreference.getString(AppConstants.SIGNIN_TOKEN));

        activitySplashBinding.tvReload.setOnClickListener(v -> {

            if (AppUtils.isNetworkAvailable()) {
                activitySplashBinding.tvReload.setVisibility(View.GONE);
                fetchCategoryData();
            }
        });

        AppConstants.DEVICE_ID = AppUtils.getDeviceId();
        AppConstants.APP_BUILD_VERSION = AppUtils.getBuildVersion();
        AppConstants.DEVICE_NAME = AppUtils.getDeviceName();
        AppConstants.DEVICE_MODEL = AppUtils.getDeviceModel();
        AppConstants.DEVICE_OS = AppUtils.getDeviceOS();
        AppConstants.DEVICE_TYPE = "ANDROID";
        AppConstants.FCM_TOKEN = "";
        AppConstants.USER_ID = "";

        fetchCategoryData();
    }


    private void fetchCategoryData() {

        if (AppUtils.isNetworkAvailable()) {
            CategoryViewModel categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);

            categoryViewModel.getActionProducts(this).observe(this, categoryContainer ->
            {
                if (categoryContainer != null) {

                    if (categoryContainer.getCode().equals(AppConstants.SUCCESS_CODE)) {
                        AppConstants.LOAD_HOME_FRAGMENT_INDEX = 0;
                        AppConstants.staticCategoryList = new ArrayList<>();
                        AppConstants.staticCitiesList = new ArrayList<>();
                        AppConstants.staticCategoryBrandsList = new ArrayList<>();
                        AppConstants.staticCategoryList = categoryContainer.getData().getCategories();
                        AppConstants.staticCitiesList = categoryContainer.getData().getCities();
                        AppConstants.staticCategoryBrandsList = categoryContainer.getData().getBrands();

                        initUrls(categoryContainer.getData());


                        AppConstants.CART_COUNTER = categoryContainer.getData().getCartCount();

                        activitySplashBinding.tvReload.setVisibility(View.GONE);

                        // remove Gift category
                        for (Category category : new ArrayList<>(AppConstants.staticCategoryList)) {
                            if (category.getName().equals("Gift"))
                                AppConstants.staticCategoryList.remove(category);
                        }


                        new Handler().postDelayed(() -> {
                            Intent intent;
                            if (!appPreference.getBoolean(IS_FIRST_TIME)) {
                                intent = new Intent(SplashActivity.this, IntroductionScreenActivity.class);
                            } else {
                                intent = new Intent(SplashActivity.this, MainActivity.class);
                            }

                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                        }, 500);
                    }
                } else {
                    activitySplashBinding.tvReload.setVisibility(View.VISIBLE);
                }

            });
        } else
            activitySplashBinding.tvReload.setVisibility(View.VISIBLE);
    }

    private void initUrls(CategoryData data) {

        AppConstants.ABOUT_US_URL = data.getAboutUs();
        AppConstants.PRIVACY_POLICY_URL = data.getPrivacy();
        AppConstants.RETURN_POLICY_URL = data.getReturns();
        AppConstants.TERMS_AND_CONDITIONS_URL = data.getTerms();
        AppConstants.FAQ_URL = data.getFaq();
        AppConstants.BLOGS = data.getBlogs();

        PRODUCT_STORAGE_BASE_URL = data.getProductsUrl();
        MEDIUM_PRODUCT_STORAGE_BASE_URL = data.getProductsMediumUrl();
        SMALL_PRODUCT_STORAGE_BASE_URL = data.getProductsSmallUrl();
        THUMBNAIL_PRODUCT_STORAGE_BASE_URL = data.getProductsThumbUrl();

        BRAND_STORAGE_BASE_URL = data.getBrandsUrl();

        BRAND_FOCUS_STORAGE_BASE_URL = data.getFeaturedBrandsUrl();
        CATEGORY_FOCUS_STORAGE_BASE_URL = data.getFeaturedCategoriesUrl();

        ACTION_STORAGE_BASE_URL = data.getMobileActionsUrl();

        CUSTOM_PRODUCT_URL = data.getCustomProductsUrl();

        USER_STORAGE_BASE_URL = data.getUsersUrl();

        BANNER_STORAGE_BASE_URL = data.getHomeBannersUrl();

        BLOG_URLS = data.getBlogUrl();

        ADVERTISEMENT_URL = data.getAdvertisementUrl();

        saveInPreferences();
    }

    private void saveInPreferences() {

        appPreference.setString(ABOUT_US_URL, ABOUT_US_URL);
        appPreference.setString(RETURN_POLICY_URL, RETURN_POLICY_URL);
        appPreference.setString(TERMS_AND_CONDITIONS_URL, TERMS_AND_CONDITIONS_URL);
        appPreference.setString(FAQ_URL, FAQ_URL);
        appPreference.setString(BLOGS, BLOGS);

        appPreference.setString(PRODUCT_STORAGE_BASE_URL, PRODUCT_STORAGE_BASE_URL);
        appPreference.setString(MEDIUM_PRODUCT_STORAGE_BASE_URL, MEDIUM_PRODUCT_STORAGE_BASE_URL);
        appPreference.setString(SMALL_PRODUCT_STORAGE_BASE_URL, SMALL_PRODUCT_STORAGE_BASE_URL);
        appPreference.setString(THUMBNAIL_PRODUCT_STORAGE_BASE_URL, THUMBNAIL_PRODUCT_STORAGE_BASE_URL);

        appPreference.setString(BRAND_STORAGE_BASE_URL, BRAND_STORAGE_BASE_URL);
        appPreference.setString(BRAND_FOCUS_STORAGE_BASE_URL, BRAND_FOCUS_STORAGE_BASE_URL);
        appPreference.setString(CATEGORY_FOCUS_STORAGE_BASE_URL, CATEGORY_FOCUS_STORAGE_BASE_URL);
        appPreference.setString(ACTION_STORAGE_BASE_URL, ACTION_STORAGE_BASE_URL);

        appPreference.setString(CUSTOM_PRODUCT_URL, CUSTOM_PRODUCT_URL);
        appPreference.setString(USER_STORAGE_BASE_URL, USER_STORAGE_BASE_URL);
        appPreference.setString(BANNER_STORAGE_BASE_URL, BANNER_STORAGE_BASE_URL);
        appPreference.setString(BLOG_URLS, BLOG_URLS);
        appPreference.setString(ADVERTISEMENT_URL, ADVERTISEMENT_URL);

    }

    @Override
    public void onFailure(boolean isFailed) {

        if (isFailed)
            activitySplashBinding.tvReload.setVisibility(View.VISIBLE);
        else
            activitySplashBinding.tvReload.setVisibility(View.GONE);
    }

    public void hashFromSHA1(String sha1) {
        String[] arr = sha1.split(":");
        byte[] byteArr = new  byte[arr.length];

        for (int i = 0; i< arr.length; i++) {
            byteArr[i] = Integer.decode("0x" + arr[i]).byteValue();
        }

        Log.d("hashKey", Base64.encodeToString(byteArr, Base64.NO_WRAP));
    }
}
