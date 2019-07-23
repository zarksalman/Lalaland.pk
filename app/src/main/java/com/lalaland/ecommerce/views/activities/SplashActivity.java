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
import com.lalaland.ecommerce.databinding.ActivitySplashBinding;
import com.lalaland.ecommerce.helpers.AppConstants;
import com.lalaland.ecommerce.helpers.AppPreference;
import com.lalaland.ecommerce.helpers.AppUtils;
import com.lalaland.ecommerce.interfaces.NetworkInterface;
import com.lalaland.ecommerce.viewModels.products.CategoryViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

        //AppUtils.printHashKey(this);

      //  hashFromSHA1("73:BF:E3:18:F2:D5:4E:56:A8:65:3B:D5:B3:66:7A:B1:15:1E:7A:18");

        appPreference = AppPreference.getInstance(this);
        headers.put("cart-session", appPreference.getString(AppConstants.CART_SESSION_TOKEN));
        headers.put("token", appPreference.getString(AppConstants.SIGNIN_TOKEN));

        Log.d(AppConstants.TAG, "user_info" + AppConstants.DEVICE_ID);
        Log.d(AppConstants.TAG, "user_info" + AppConstants.APP_BUILD_VERSION);
        Log.d(AppConstants.TAG, "user_info" + AppConstants.DEVICE_NAME);
        Log.d(AppConstants.TAG, "user_info" + AppConstants.DEVICE_MODEL);
        Log.d(AppConstants.TAG, "user_info" + AppConstants.DEVICE_OS);
        Log.d(AppConstants.TAG, "user_info" + AppConstants.DEVICE_TYPE);
        Log.d(AppConstants.TAG, "user_info" + AppConstants.FCM_TOKEN);
        Log.d(AppConstants.TAG, "user_info" + AppConstants.USER_ID);

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

                        AppConstants.ABOUT_US_URL = categoryContainer.getData().getAboutUs();
                        AppConstants.PRIVACY_POLICY_URL = categoryContainer.getData().getPrivacy();
                        AppConstants.RETURN_POLICY_URL = categoryContainer.getData().getReturns();
                        AppConstants.TERMS_AND_CONDITIONS_URL = categoryContainer.getData().getTerms();
                        AppConstants.FAQ_URL = categoryContainer.getData().getFaq();
                        AppConstants.BLOGS = categoryContainer.getData().getBlogs();

                        AppConstants.CART_COUNTER = categoryContainer.getData().getCartCount();

                        activitySplashBinding.tvReload.setVisibility(View.GONE);

                        // remove Gift category
                        for (Category category : new ArrayList<>(AppConstants.staticCategoryList)) {
                            if (category.getName().equals("Gift"))
                                AppConstants.staticCategoryList.remove(category);
                        }


                        new Handler().postDelayed(() -> {

                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
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
