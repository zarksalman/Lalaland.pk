package com.lalaland.ecommerce.views.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.databinding.ActivityMainBinding;
import com.lalaland.ecommerce.databinding.ExitDialogueLayoutBinding;
import com.lalaland.ecommerce.helpers.AppPreference;
import com.lalaland.ecommerce.listeners.CloseAppListener;
import com.lalaland.ecommerce.views.fragments.homeFragments.AccountFragment;
import com.lalaland.ecommerce.views.fragments.homeFragments.CartFragment;
import com.lalaland.ecommerce.views.fragments.homeFragments.CategoryFragment;
import com.lalaland.ecommerce.views.fragments.homeFragments.HomeFragment;
import com.lalaland.ecommerce.views.fragments.homeFragments.WishFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.lalaland.ecommerce.helpers.AppConstants.BANNER_STORAGE_BASE_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.LOAD_HOME_FRAGMENT_INDEX;
import static com.lalaland.ecommerce.helpers.AppConstants.LOAD_HOME_FRAGMENT_INDEX_KEY;
import static com.lalaland.ecommerce.helpers.AppConstants.PRODUCT_ID;
import static com.lalaland.ecommerce.helpers.AppConstants.SIGNIN_TOKEN;

public class MainActivity extends AppCompatActivity implements CloseAppListener {
    private ActivityMainBinding activityMainBinding;
    private AppPreference appPreference;
    private int selectedFragment = -1;
    String token;
    AlertDialog exitDialog;
    ExitDialogueLayoutBinding exitDialogueLayoutBinding;
    List<Fragment> fragments = new ArrayList<>();

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
//                replaceFragment(fragments.get(0), 0);

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
                //replaceFragment(fragments.get(1), 1);

                return true;
            case R.id.navigation_cart:

                if (selectedFragment == 2)
                    return false;

                activityMainBinding.topBar.setVisibility(View.VISIBLE);
                activityMainBinding.topBarWithoutSearch.setVisibility(View.VISIBLE);
                activityMainBinding.topBarSearch.setVisibility(View.GONE);

                activityMainBinding.topBar.setBackgroundColor(getResources().getColor(android.R.color.white));
                activityMainBinding.tvFragmentName.setText(getResources().getString(R.string.cart_items));

                fragment = CartFragment.newInstance();
                replaceFragment(fragment, 2);
                //replaceFragment(fragments.get(2), 2);

                return true;

            case R.id.navigation_wish:

                if (selectedFragment == 3)
                    return false;

                activityMainBinding.topBar.setVisibility(View.VISIBLE);
                activityMainBinding.topBarSearch.setVisibility(View.GONE);
                activityMainBinding.topBarWithoutSearch.setVisibility(View.VISIBLE);

                activityMainBinding.topBar.setBackgroundColor(getResources().getColor(android.R.color.white));
                activityMainBinding.tvFragmentName.setText(getResources().getString(R.string.wish_items));

                fragment = WishFragment.newInstance();
                replaceFragment(fragment, 3);
                //replaceFragment(fragments.get(3), 3);

                return true;

            case R.id.navigation_account:

                if (selectedFragment == 4)
                    return false;

                activityMainBinding.topBar.setVisibility(View.GONE);

                fragment = AccountFragment.newInstance();
                replaceFragment(fragment, 4);
                //replaceFragment(fragments.get(4), 4);

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

        fragments.add(HomeFragment.newInstance());
        fragments.add(CategoryFragment.newInstance());
        fragments.add(CartFragment.newInstance());
        fragments.add(WishFragment.newInstance());
        fragments.add(AccountFragment.newInstance());


        setListeners();
        loadInitialFragment();  // load first fragment
        prepareExitDialog();

        // setting search bar text size
        LinearLayout linearLayout1 = (LinearLayout) activityMainBinding.svItems.getChildAt(0);
        LinearLayout linearLayout2 = (LinearLayout) linearLayout1.getChildAt(2);
        LinearLayout linearLayout3 = (LinearLayout) linearLayout2.getChildAt(1);
        AutoCompleteTextView autoComplete = (AutoCompleteTextView) linearLayout3.getChildAt(0);
        autoComplete.setTextSize(13);

        getParameters(getIntent());
    }


    void setListeners() {

        activityMainBinding.navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        activityMainBinding.ivSvItemFg.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, GlobalSearchActivity.class));
        });

    }

    void replaceFragment(Fragment fragment, int index) {

        selectedFragment = index;
        appPreference.setInt(LOAD_HOME_FRAGMENT_INDEX_KEY, selectedFragment);
        activityMainBinding.tvNetwork.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_host, fragment, "fragment_" + index).commit();
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    private void showExitDialog() {

        if (selectedFragment == 0) {
            exitDialog.show();
        } else {

            LOAD_HOME_FRAGMENT_INDEX = 0;
            activityMainBinding.navView.setSelectedItemId(R.id.navigation_home);
        }
    }
    void loadInitialFragment() {

        // if activity is coming from bg and lost its static variable's content
        if (BANNER_STORAGE_BASE_URL.isEmpty())
            LOAD_HOME_FRAGMENT_INDEX = appPreference.getInt(LOAD_HOME_FRAGMENT_INDEX_KEY);

        switch (LOAD_HOME_FRAGMENT_INDEX) {

            case 0:
                activityMainBinding.navView.setSelectedItemId(R.id.navigation_home);
                break;
            case 1:
                selectedFragment = 0;
                activityMainBinding.navView.setSelectedItemId(R.id.navigation_category);
                break;
            case 2:
                activityMainBinding.navView.setSelectedItemId(R.id.navigation_cart);
                break;
            case 3:
                activityMainBinding.navView.setSelectedItemId(R.id.navigation_wish);
                break;
            case 4:
                activityMainBinding.navView.setSelectedItemId(R.id.navigation_account);
                break;
            default:
                activityMainBinding.navView.setSelectedItemId(R.id.navigation_home);
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

    private void prepareExitDialog() {

        exitDialogueLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.exit_dialogue_layout, null, false);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        exitDialog = dialogBuilder.create();
        exitDialog.setCancelable(false);
        exitDialog.setView(exitDialogueLayoutBinding.getRoot());

        exitDialogueLayoutBinding.btnOk.setOnClickListener(v -> {
            exitDialog.dismiss();
            closeApp();
        });

        exitDialogueLayoutBinding.btnCancel.setOnClickListener(v -> {
            exitDialog.dismiss();
        });

    }

    @Override
    public void closeApp() {
        finish();
    }

    private void getParameters(Intent intent) {

        Uri uri = intent.getData();

        if (uri != null) {
            /*server = uri.getAuthority();
            path = uri.getPath();
            protocol = uri.getScheme();
            */

            Set<String> args;
            Object[] keys;

            args = uri.getQueryParameterNames();
            keys = args.toArray();

            if (keys != null && keys.length > 0) {
                String productId = uri.getQueryParameter(keys[0].toString());

                Intent mIntent = new Intent(this, ProductDetailActivity.class);
                mIntent.putExtra(PRODUCT_ID, Integer.parseInt(productId));
                startActivity(mIntent);
            }
        }


    }

}