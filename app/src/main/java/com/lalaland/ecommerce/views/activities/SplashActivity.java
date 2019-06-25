package com.lalaland.ecommerce.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.lalaland.ecommerce.helpers.AppUtils;
import com.lalaland.ecommerce.viewModels.products.CategoryViewModel;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {

    ActivitySplashBinding activitySplashBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activitySplashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);

        activitySplashBinding.tvReload.setOnClickListener(v -> {

            if (AppUtils.isNetworkAvailable()) {
                activitySplashBinding.tvReload.setVisibility(View.GONE);
                fetchCategoryData();
            }
        });
        fetchCategoryData();
    }

    private void fetchCategoryData() {

        if (AppUtils.isNetworkAvailable()) {
            CategoryViewModel categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);

            categoryViewModel.getActionProducts().observe(this, categoryContainer ->
            {
                if (categoryContainer != null) {

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
            });
        } else
            activitySplashBinding.tvReload.setVisibility(View.VISIBLE);
    }
}
