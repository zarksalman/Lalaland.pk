package com.lalaland.ecommerce.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.databinding.ActivityMainBinding;
import com.lalaland.ecommerce.views.fragments.homeFragments.AccountFragment;
import com.lalaland.ecommerce.views.fragments.homeFragments.CartFragment;
import com.lalaland.ecommerce.views.fragments.homeFragments.CategoryFragment;
import com.lalaland.ecommerce.views.fragments.homeFragments.HomeFragment;
import com.lalaland.ecommerce.views.fragments.homeFragments.WishFragment;

import static com.lalaland.ecommerce.helpers.AppUtils.isNetworkAvailable;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding activityMainBinding;

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


        setListeners();
        replaceFragment(HomeFragment.newInstance(), 1);

        // startActivity(new Intent(this, RegistrationActivity.class));
    }

    void setListeners() {
        activityMainBinding.navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    void replaceFragment(Fragment fragment, int index) {

        if (isNetworkAvailable())
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_host, fragment, "fragment_" + index).commit();
        else
            activityMainBinding.tvNetwork.setVisibility(View.VISIBLE);
    }
}
