package com.lalaland.ecommerce.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.databinding.ActivityMainBinding;
import com.lalaland.ecommerce.helpers.AppConstants;
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


        // setting search bar text size
        LinearLayout linearLayout1 = (LinearLayout) activityMainBinding.svItems.getChildAt(0);
        LinearLayout linearLayout2 = (LinearLayout) linearLayout1.getChildAt(2);
        LinearLayout linearLayout3 = (LinearLayout) linearLayout2.getChildAt(1);
        AutoCompleteTextView autoComplete = (AutoCompleteTextView) linearLayout3.getChildAt(0);
        autoComplete.setTextSize(13);

        //  addItemsToBottomNavigation();
    }

    private void addItemsToBottomNavigation() {


        activityMainBinding.bottomNavigation.setForceTint(true);
        // Create items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.title_home, R.drawable.home_bg_selector, android.R.color.white);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.category, R.drawable.category_bg_selector, android.R.color.white);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.cart, R.drawable.cart_bg_selector, android.R.color.white);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.wish, R.drawable.wishlist_bg_selector, android.R.color.white);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem(R.string.account, R.drawable.account_bg_selector, android.R.color.white);

// Add items
        activityMainBinding.bottomNavigation.addItem(item1);
        activityMainBinding.bottomNavigation.addItem(item2);
        activityMainBinding.bottomNavigation.addItem(item3);
        activityMainBinding.bottomNavigation.addItem(item4);
        activityMainBinding.bottomNavigation.addItem(item5);

        activityMainBinding.bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);


// Set background color
        activityMainBinding.bottomNavigation.setDefaultBackgroundColor(getResources().getColor(android.R.color.white));
        activityMainBinding.bottomNavigation.setNotification(String.valueOf(AppConstants.CART_COUNTER), 2);


/*
        activityMainBinding.bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {

                Fragment fragment;

                switch (position) {

                    case 0:

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

                    case 1:

                        if (selectedFragment == 1)
                            return true;

                        activityMainBinding.topBar.setVisibility(View.VISIBLE);
                        activityMainBinding.topBarSearch.setVisibility(View.VISIBLE);
                        activityMainBinding.topBarWithoutSearch.setVisibility(View.GONE);

                        activityMainBinding.topBar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        fragment = CategoryFragment.newInstance();
                        replaceFragment(fragment, 1);
                        return true;
                    case 2:

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

                    case 3:

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

                    case 4:

                        if (selectedFragment == 4)
                            return false;

                        activityMainBinding.topBar.setVisibility(View.GONE);
                        fragment = AccountFragment.newInstance();
                        replaceFragment(fragment, 4);
                        return true;
                }
                return false;
            }
        });
*/
    }

    private void addBadgeView() {
        /*BottomNavigationMenuView menuView = (BottomNavigationMenuView) activityMainBinding.navView.getChildAt(2);
        BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(0);
*/

        BottomNavigationMenuView bottomNavigationMenuView =
                (BottomNavigationMenuView) activityMainBinding.navView.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(2);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;

        View badge = LayoutInflater.from(this)
                .inflate(R.layout.batch_layout, itemView, true);

        //itemView.removeView(badge);
        // itemView.addView(badge, 2);
   /*     MenuItem menuItem = activityMainBinding.navView.getMenu().getItem(2);
        BatchLayoutBinding batchLayoutBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.batch_layout, null, false);

        batchLayoutBinding.badge.setText(String.valueOf(AppConstants.CART_COUNTER));
        menuItem.addView(batchLayoutBinding.getRoot());*/
    }

    void setListeners() {
        activityMainBinding.navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        activityMainBinding.ivSvItemFg.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, GlobalSearchActivity.class));
        });

    }

    void replaceFragment(Fragment fragment, int index) {

        selectedFragment = index;
        activityMainBinding.tvNetwork.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_host, fragment, "fragment_" + index).commit();
    }

    @Override
    public void onBackPressed() {

        if (selectedFragment == 0) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
        } else {

            LOAD_HOME_FRAGMENT_INDEX = 0;
            activityMainBinding.navView.setSelectedItemId(R.id.navigation_home);
        }

    }

    void loadInitialFragment() {

        switch (LOAD_HOME_FRAGMENT_INDEX) {

            case 0:

/*
                activityMainBinding.topBar.setVisibility(View.VISIBLE);
                activityMainBinding.topBarSearch.setVisibility(View.VISIBLE);
                activityMainBinding.topBarWithoutSearch.setVisibility(View.GONE);

                activityMainBinding.topBar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                activityMainBinding.tvAppName.setText(getResources().getString(R.string.app_name));
                replaceFragment(HomeFragment.newInstance(), LOAD_HOME_FRAGMENT_INDEX);
*/

                activityMainBinding.navView.setSelectedItemId(R.id.navigation_home);

                break;
            case 1:

         /*       activityMainBinding.topBar.setVisibility(View.VISIBLE);
                activityMainBinding.topBarSearch.setVisibility(View.VISIBLE);
                activityMainBinding.topBarWithoutSearch.setVisibility(View.GONE);


                activityMainBinding.topBar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                replaceFragment(CategoryFragment.newInstance(), LOAD_HOME_FRAGMENT_INDEX);*/
                // setting it to zero
                selectedFragment = 0;
                activityMainBinding.navView.setSelectedItemId(R.id.navigation_category);
                break;
            case 2:

               /* activityMainBinding.topBar.setVisibility(View.VISIBLE);
                activityMainBinding.topBarSearch.setVisibility(View.GONE);
                activityMainBinding.topBarWithoutSearch.setVisibility(View.VISIBLE);


                activityMainBinding.topBar.setBackgroundColor(getResources().getColor(android.R.color.white));
                activityMainBinding.tvFragmentName.setText(getResources().getString(R.string.cart_items));
                replaceFragment(CartFragment.newInstance(), LOAD_HOME_FRAGMENT_INDEX);*/

                activityMainBinding.navView.setSelectedItemId(R.id.navigation_cart);
                break;

            case 3:

              /*  activityMainBinding.topBar.setVisibility(View.VISIBLE);
                activityMainBinding.topBarSearch.setVisibility(View.GONE);
                activityMainBinding.topBarWithoutSearch.setVisibility(View.VISIBLE);


                activityMainBinding.topBar.setBackgroundColor(getResources().getColor(android.R.color.white));
                activityMainBinding.tvFragmentName.setText(getResources().getString(R.string.wish_items));
                replaceFragment(WishFragment.newInstance(), LOAD_HOME_FRAGMENT_INDEX);*/

                activityMainBinding.navView.setSelectedItemId(R.id.navigation_wish);
                break;
            case 4:

          /*      activityMainBinding.topBar.setVisibility(View.GONE);
                activityMainBinding.topBarSearch.setVisibility(View.VISIBLE);
                activityMainBinding.topBarWithoutSearch.setVisibility(View.GONE);


                activityMainBinding.tvAppName.setText(getResources().getString(R.string.account));
                replaceFragment(AccountFragment.newInstance(), LOAD_HOME_FRAGMENT_INDEX);
*/
                activityMainBinding.navView.setSelectedItemId(R.id.navigation_account);
                break;

            default:

                activityMainBinding.topBar.setVisibility(View.VISIBLE);
                activityMainBinding.topBarSearch.setVisibility(View.VISIBLE);
                activityMainBinding.topBarWithoutSearch.setVisibility(View.GONE);

                activityMainBinding.tvAppName.setText(getResources().getString(R.string.app_name));
                replaceFragment(HomeFragment.newInstance(), 0);
        }
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