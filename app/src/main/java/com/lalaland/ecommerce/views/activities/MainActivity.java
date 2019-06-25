package com.lalaland.ecommerce.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.databinding.ActivityMainBinding;
import com.lalaland.ecommerce.helpers.AppPreference;
import com.lalaland.ecommerce.views.fragments.homeFragments.AccountFragment;
import com.lalaland.ecommerce.views.fragments.homeFragments.CartFragment;
import com.lalaland.ecommerce.views.fragments.homeFragments.CategoryFragment;
import com.lalaland.ecommerce.views.fragments.homeFragments.HomeFragment;
import com.lalaland.ecommerce.views.fragments.homeFragments.WishFragment;

import static com.lalaland.ecommerce.helpers.AppConstants.LOAD_HOME_FRAGMENT_INDEX;
import static com.lalaland.ecommerce.helpers.AppConstants.SIGNIN_TOKEN;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding activityMainBinding;
    private AppPreference appPreference;
    private int selectedFragment = 1;
    boolean doubleBackToExitPressedOnce = false;
    String token;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {

        Fragment fragment;

        switch (item.getItemId()) {

            case R.id.navigation_home:

                if (selectedFragment == 0)
                    return false;

                activityMainBinding.topBar.setVisibility(View.VISIBLE);
                activityMainBinding.topBarSearch.setVisibility(View.VISIBLE);
                activityMainBinding.topBarWithoutSearch.setVisibility(View.GONE);

                activityMainBinding.topBar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                activityMainBinding.tvAppName.setText(getResources().getString(R.string.app_name));
                fragment = HomeFragment.newInstance();
                replaceFragment(fragment, 0);
                return true;
            case R.id.navigation_category:

                if (selectedFragment == 1)
                    return false;

                activityMainBinding.topBar.setVisibility(View.VISIBLE);
                activityMainBinding.topBarSearch.setVisibility(View.VISIBLE);
                activityMainBinding.topBarWithoutSearch.setVisibility(View.GONE);

                activityMainBinding.topBar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                fragment = CategoryFragment.newInstance();
                replaceFragment(fragment, 1);
                return true;
            case R.id.navigation_cart:

                if (selectedFragment == 2)
                    return false;

                activityMainBinding.topBarSearch.setVisibility(View.GONE);
                activityMainBinding.topBarWithoutSearch.setVisibility(View.VISIBLE);
                activityMainBinding.topBar.setVisibility(View.VISIBLE);

                activityMainBinding.topBar.setBackgroundColor(getResources().getColor(android.R.color.white));
                activityMainBinding.tvFragmentName.setText(getResources().getString(R.string.cart_items));

                fragment = CartFragment.newInstance();
                replaceFragment(fragment, 2);
                return true;

            case R.id.navigation_wish:

                if (selectedFragment == 3)
                    return false;

                activityMainBinding.topBarSearch.setVisibility(View.GONE);
                activityMainBinding.topBarWithoutSearch.setVisibility(View.VISIBLE);
                activityMainBinding.topBar.setVisibility(View.VISIBLE);

                activityMainBinding.topBar.setBackgroundColor(getResources().getColor(android.R.color.white));
                activityMainBinding.tvFragmentName.setText(getResources().getString(R.string.wish_items));

                fragment = WishFragment.newInstance();

                replaceFragment(fragment, 3);
                return true;

            case R.id.navigation_account:

                if (selectedFragment == 4)
                    return false;

                activityMainBinding.topBar.setVisibility(View.GONE);
                fragment = AccountFragment.newInstance();
                replaceFragment(fragment, 4);
                return true;
        }
        return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        appPreference = AppPreference.getInstance(this);
        
        activityMainBinding.navView.setItemIconTintList(null);
        token = appPreference.getString(SIGNIN_TOKEN);
        setListeners();
        loadInitialFragment();  // load first fragment
    }

    void setListeners() {
        activityMainBinding.navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        activityMainBinding.ivSvItemFg.setOnClickListener(v -> {

            //  closeKeyboard();
            startActivity(new Intent(MainActivity.this, GlobalSearchActivity.class));

        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    void replaceFragment(Fragment fragment, int index) {

        selectedFragment = index;
        activityMainBinding.tvNetwork.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_host, fragment, "fragment_" + index).commit();
    }

    void loadInitialFragment() {

        activityMainBinding.topBar.setVisibility(View.VISIBLE);
        activityMainBinding.topBarSearch.setVisibility(View.VISIBLE);
        activityMainBinding.topBarWithoutSearch.setVisibility(View.GONE);

        switch (LOAD_HOME_FRAGMENT_INDEX) {

            case 0:
                activityMainBinding.topBar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                activityMainBinding.tvAppName.setText(getResources().getString(R.string.app_name));
                replaceFragment(HomeFragment.newInstance(), LOAD_HOME_FRAGMENT_INDEX);
                break;
            case 1:
                activityMainBinding.topBar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                replaceFragment(CategoryFragment.newInstance(), LOAD_HOME_FRAGMENT_INDEX);
                break;
            case 2:
                activityMainBinding.topBar.setBackgroundColor(getResources().getColor(android.R.color.white));
                activityMainBinding.topBarSearch.setVisibility(View.GONE);
                activityMainBinding.tvFragmentName.setText(getResources().getString(R.string.cart_items));

                activityMainBinding.topBarWithoutSearch.setVisibility(View.VISIBLE);

                replaceFragment(CartFragment.newInstance(), LOAD_HOME_FRAGMENT_INDEX);
                break;

            case 3:

                activityMainBinding.topBar.setBackgroundColor(getResources().getColor(android.R.color.white));
                activityMainBinding.topBarSearch.setVisibility(View.GONE);
                activityMainBinding.tvFragmentName.setText(getResources().getString(R.string.wish_items));
                activityMainBinding.topBarWithoutSearch.setVisibility(View.VISIBLE);
                replaceFragment(WishFragment.newInstance(), LOAD_HOME_FRAGMENT_INDEX);
                break;
            case 4:

                activityMainBinding.topBar.setVisibility(View.GONE);
                activityMainBinding.tvAppName.setText(getResources().getString(R.string.account));
                replaceFragment(AccountFragment.newInstance(), LOAD_HOME_FRAGMENT_INDEX);
                break;

            default:
                activityMainBinding.tvAppName.setText(getResources().getString(R.string.app_name));
                replaceFragment(HomeFragment.newInstance(), 0);
        }
    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {

                LOAD_HOME_FRAGMENT_INDEX = 2;
                loadInitialFragment();
            }
        }
    }
}