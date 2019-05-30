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
    private int selectedFragment = 1;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {

        Fragment fragment;
        switch (item.getItemId()) {
            case R.id.navigation_home:

                if (selectedFragment == 1)
                    return false;

                activityMainBinding.tvAppName.setText(getResources().getString(R.string.app_name));
                fragment = HomeFragment.newInstance();
                replaceFragment(fragment, 1);
                return true;
            case R.id.navigation_category:

                if (selectedFragment == 2)
                    return false;

                activityMainBinding.tvAppName.setText(getResources().getString(R.string.category));
                fragment = CategoryFragment.newInstance();
                replaceFragment(fragment, 2);
                return true;
            case R.id.navigation_cart:

                if (selectedFragment == 3)
                    return false;

                activityMainBinding.tvAppName.setText(getResources().getString(R.string.cart));
                fragment = CartFragment.newInstance();
                replaceFragment(fragment, 3);
                return true;

            case R.id.navigation_wish:

                if (selectedFragment == 4)
                    return false;

                activityMainBinding.tvAppName.setText(getResources().getString(R.string.wish_list));
                fragment = WishFragment.newInstance();
                replaceFragment(fragment, 4);
                return true;

            case R.id.navigation_account:

                if (selectedFragment == 5)
                    return false;

                activityMainBinding.tvAppName.setText(getResources().getString(R.string.account));
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
            selectedFragment = index;
            activityMainBinding.tvNetwork.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_host, fragment, "fragment_" + index).commit();
        }
        else
            activityMainBinding.tvNetwork.setVisibility(View.VISIBLE);
    }

    void loadInitialFragment() {

        switch (LOAD_HOME_FRAGMENT_INDEX) {
            case 0:
                activityMainBinding.tvAppName.setText(getResources().getString(R.string.app_name));
                replaceFragment(HomeFragment.newInstance(), LOAD_HOME_FRAGMENT_INDEX);
                break;
            case 1:
                activityMainBinding.tvAppName.setText(getResources().getString(R.string.category));
                replaceFragment(CategoryFragment.newInstance(), LOAD_HOME_FRAGMENT_INDEX);
                break;
            case 2:
                activityMainBinding.tvAppName.setText(getResources().getString(R.string.cart));
                replaceFragment(CartFragment.newInstance(), LOAD_HOME_FRAGMENT_INDEX);
                break;

            case 3:
                activityMainBinding.tvAppName.setText(getResources().getString(R.string.wish_list));
                replaceFragment(WishFragment.newInstance(), LOAD_HOME_FRAGMENT_INDEX);
                break;
            case 4:
                activityMainBinding.tvAppName.setText(getResources().getString(R.string.account));
                replaceFragment(AccountFragment.newInstance(), LOAD_HOME_FRAGMENT_INDEX);
                break;

            default:
                activityMainBinding.tvAppName.setText(getResources().getString(R.string.app_name));
                replaceFragment(HomeFragment.newInstance(), 0);
        }
    }
}
