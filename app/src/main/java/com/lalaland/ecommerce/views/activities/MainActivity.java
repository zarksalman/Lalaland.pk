package com.lalaland.ecommerce.views.activities;

import android.os.Bundle;
import android.view.View;

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
import static com.lalaland.ecommerce.helpers.AppUtils.isNetworkAvailable;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding activityMainBinding;
    private AppPreference appPreference;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {

        Fragment fragment;
        switch (item.getItemId()) {
            case R.id.navigation_home:
                fragment = HomeFragment.newInstance();
                replaceFragment(fragment, 1);
                return true;
            case R.id.navigation_category:
                fragment = CategoryFragment.newInstance();
                replaceFragment(fragment, 2);
                return true;
            case R.id.navigation_cart:
                fragment = CartFragment.newInstance();
                replaceFragment(fragment, 3);
                return true;

            case R.id.navigation_wish:
                fragment = WishFragment.newInstance();
                replaceFragment(fragment, 4);
                return true;

            case R.id.navigation_account:
                fragment = AccountFragment.newInstance();
                replaceFragment(fragment, 5);
                return true;
        }
        return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        appPreference = AppPreference.getInstance(this);

        setListeners();
        loadInitialFragment();  // load first fragment
    }

    void setListeners() {
        activityMainBinding.navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    void replaceFragment(Fragment fragment, int index) {

        // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_host, fragment, "fragment_" + index).commit();

        if (isNetworkAvailable()) {
            activityMainBinding.tvNetwork.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_host, fragment, "fragment_" + index).commit();
        }
        else
            activityMainBinding.tvNetwork.setVisibility(View.VISIBLE);
    }

    void loadInitialFragment() {

        switch (LOAD_HOME_FRAGMENT_INDEX) {
            case 0:
                replaceFragment(HomeFragment.newInstance(), LOAD_HOME_FRAGMENT_INDEX);
                break;
            case 1:
                replaceFragment(CategoryFragment.newInstance(), LOAD_HOME_FRAGMENT_INDEX);
                break;
            case 2:
                replaceFragment(CartFragment.newInstance(), LOAD_HOME_FRAGMENT_INDEX);
                break;

            case 3:
                replaceFragment(WishFragment.newInstance(), LOAD_HOME_FRAGMENT_INDEX);
                break;
            case 4:
                replaceFragment(AccountFragment.newInstance(), LOAD_HOME_FRAGMENT_INDEX);
                break;

            default:
                replaceFragment(HomeFragment.newInstance(), 0);
        }
    }
}
