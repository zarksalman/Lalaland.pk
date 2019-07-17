package com.lalaland.ecommerce.views.fragments.homeFragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.databinding.FragmentAccountBinding;
import com.lalaland.ecommerce.helpers.AppConstants;
import com.lalaland.ecommerce.helpers.AppPreference;
import com.lalaland.ecommerce.helpers.AppUtils;
import com.lalaland.ecommerce.viewModels.user.LoginViewModel;
import com.lalaland.ecommerce.views.activities.AccountInformationActivity;
import com.lalaland.ecommerce.views.activities.ChangeShippingAddress;
import com.lalaland.ecommerce.views.activities.ContactUsActivity;
import com.lalaland.ecommerce.views.activities.OrderListingActivity;
import com.lalaland.ecommerce.views.activities.RegistrationActivity;
import com.lalaland.ecommerce.views.activities.WebViewActivity;

import static com.lalaland.ecommerce.helpers.AppConstants.ORDER_STATUS;
import static com.lalaland.ecommerce.helpers.AppConstants.SIGNIN_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.USER_AVATAR;
import static com.lalaland.ecommerce.helpers.AppConstants.USER_NAME;
import static com.lalaland.ecommerce.helpers.AppConstants.USER_STORAGE_BASE_URL;


public class AccountFragment extends Fragment implements View.OnClickListener {

    private FragmentAccountBinding fragmentAccountBinding;
    private String signInToken, userName, userAvatar;
    private AppPreference appPreference;
    private Intent intent;

    public AccountFragment() {
        // Required empty public constructor
    }

    public static AccountFragment newInstance() {
        AccountFragment fragment = new AccountFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        fragmentAccountBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false);

        appPreference = AppPreference.getInstance(getContext());
        signInToken = appPreference.getString(SIGNIN_TOKEN);

        // if user is login
        if (signInToken.isEmpty()) {

            fragmentAccountBinding.tvLoginLogout.setText("Login");
            fragmentAccountBinding.tvUserName.setText("Login / Create account");

            intent = new Intent(getContext(), RegistrationActivity.class);

            Glide.with(AppConstants.mContext)
                    .load(R.drawable.placeholder_products)
                    .into(fragmentAccountBinding.ivDisplayPicture);

        } else {

            userName = appPreference.getString(USER_NAME);
            userAvatar = appPreference.getString(USER_AVATAR);

            intent = new Intent(getContext(), OrderListingActivity.class);

            fragmentAccountBinding.tvUserName.setText(userName);
            String avatarImagePath = USER_STORAGE_BASE_URL.concat(userAvatar);

            fragmentAccountBinding.tvLoginLogout.setText("Logout");

            Glide.with(AppConstants.mContext)
                    .load(avatarImagePath)
                    .placeholder(R.drawable.placeholder_products)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(fragmentAccountBinding.ivDisplayPicture);
        }

        initListeners();

        return fragmentAccountBinding.getRoot();
    }

    private void initListeners() {

        fragmentAccountBinding.ivSetting.setOnClickListener(this);

        fragmentAccountBinding.tvLoginLogout.setOnClickListener(this);

        fragmentAccountBinding.tvUserName.setOnClickListener(this);
        fragmentAccountBinding.ivDisplayPicture.setOnClickListener(this);

        fragmentAccountBinding.blog.setOnClickListener(this);
        fragmentAccountBinding.shippingAddress.setOnClickListener(this);
        fragmentAccountBinding.appSuggestion.setOnClickListener(this);
        fragmentAccountBinding.contactUs.setOnClickListener(this);
        fragmentAccountBinding.faq.setOnClickListener(this);
        fragmentAccountBinding.termsCondition.setOnClickListener(this);
        fragmentAccountBinding.refund.setOnClickListener(this);

        fragmentAccountBinding.ivViewAll.setOnClickListener(this);
        fragmentAccountBinding.tvViewAll.setOnClickListener(this);
        fragmentAccountBinding.newOrderContainer.setOnClickListener(this);
        fragmentAccountBinding.confirmedOrderContainer.setOnClickListener(this);
        fragmentAccountBinding.recievedOrderContainer.setOnClickListener(this);
        fragmentAccountBinding.inTransitOrderContainer.setOnClickListener(this);
        fragmentAccountBinding.cancelledOrderContainer.setOnClickListener(this);
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {

            case R.id.iv_view_all:
                if (signInToken.isEmpty()) {
                    startActivityForResult(new Intent(getContext(), RegistrationActivity.class), 101);
                } else {
                    intent.putExtra(ORDER_STATUS, "all orders");
                    startActivity();
                }
                break;

            case R.id.tv_view_all:
                if (signInToken.isEmpty()) {
                    startActivityForResult(new Intent(getContext(), RegistrationActivity.class), 101);
                } else {
                    intent.putExtra(ORDER_STATUS, "all");
                    startActivity();
                }
                break;

            case R.id.new_order_container:
                if (signInToken.isEmpty()) {
                    startActivityForResult(new Intent(getContext(), RegistrationActivity.class), 101);
                } else {
                    intent.putExtra(ORDER_STATUS, "new");
                    startActivity();
                }
                break;

            case R.id.confirmed_order_container:
                if (signInToken.isEmpty()) {
                    startActivityForResult(new Intent(getContext(), RegistrationActivity.class), 101);
                } else {
                    intent.putExtra(ORDER_STATUS, "confirm");
                    startActivity();
                }
                break;

            case R.id.cancelled_order_container:
                if (signInToken.isEmpty()) {
                    startActivityForResult(new Intent(getContext(), RegistrationActivity.class), 101);
                } else {
                    intent.putExtra(ORDER_STATUS, "cancel");
                    startActivity();
                }
                break;

            case R.id.in_transit_order_container:
                if (signInToken.isEmpty()) {
                    startActivityForResult(new Intent(getContext(), RegistrationActivity.class), 101);
                } else {
                    intent.putExtra(ORDER_STATUS, "intransit");
                    startActivity();
                }
                break;

            case R.id.recieved_order_container:
                if (signInToken.isEmpty()) {
                    startActivityForResult(new Intent(getContext(), RegistrationActivity.class), 101);
                } else {
                    intent.putExtra(ORDER_STATUS, "receive");
                    startActivity();
                }
                break;

            case R.id.iv_setting:

                if (signInToken.isEmpty())
                    startActivityForResult(new Intent(getContext(), RegistrationActivity.class), 101);
                else
                    startActivityForResult(new Intent(getContext(), AccountInformationActivity.class), 100);

                break;

            case R.id.iv_display_picture:
                if (signInToken.isEmpty())
                    startActivityForResult(new Intent(getContext(), RegistrationActivity.class), 101);
                else
                    startActivityForResult(new Intent(getContext(), AccountInformationActivity.class), 100);

                break;

            case R.id.tv_user_name:
                if (signInToken.isEmpty())
                    startActivityForResult(new Intent(getContext(), RegistrationActivity.class), 100);
                else
                    startActivityForResult(new Intent(getContext(), AccountInformationActivity.class), 101);
                break;

            case R.id.tv_login_logout:

                if (signInToken.isEmpty()) {
                    startActivityForResult(new Intent(getContext(), RegistrationActivity.class), 100);
                } else {
                    logoutUser();
                }
                break;

            case R.id.blog:
                AppConstants.URL_TYPE = 1;
                startActivity(new Intent(getContext(), WebViewActivity.class));
                break;

            case R.id.shipping_address:
                if (signInToken.isEmpty()) {
                    startActivityForResult(new Intent(getContext(), RegistrationActivity.class), 100);
                } else {
                    startActivity(new Intent(getContext(), ChangeShippingAddress.class));
                }
                break;

            case R.id.refund:
                AppConstants.URL_TYPE = 2;
                startActivity(new Intent(getContext(), WebViewActivity.class));
//                startActivity(AppUtils.getOpenUrlIntent(AppConstants.RETURN_POLICY_URL));
                break;

            case R.id.terms_condition:
                AppConstants.URL_TYPE = 3;
                startActivity(new Intent(getContext(), WebViewActivity.class));
                //                startActivity(AppUtils.getOpenUrlIntent(AppConstants.TERMS_AND_CONDITIONS_URL));
                break;

            case R.id.faq:
                AppConstants.URL_TYPE = 4;
                startActivity(new Intent(getContext(), WebViewActivity.class));
                //startActivity(AppUtils.getOpenUrlIntent(AppConstants.FAQ_URL));
                break;

            case R.id.app_suggestion:
                startActivity(AppUtils.getShareIntent());
                break;

            case R.id.contact_us:
                startActivity(new Intent(getContext(), ContactUsActivity.class));
                break;
        }
    }

    void startActivity() {

        startActivity(intent);
    }

    void logoutUser() {

        fragmentAccountBinding.progressBar.setVisibility(View.VISIBLE);
        LoginViewModel loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        loginViewModel.logoutUser().observe(this, logout -> {

            if (logout != null) {

                logoutFacebookUser();

                fragmentAccountBinding.tvLoginLogout.setText(getString(R.string.login));
                fragmentAccountBinding.tvUserName.setText("Login / Create Account");

                signInToken = "";

                Glide.with(AppConstants.mContext)
                        .load(R.drawable.placeholder_products)
                        .into(fragmentAccountBinding.ivDisplayPicture);

                Toast.makeText(getContext(), logout.getMsg(), Toast.LENGTH_SHORT).show();

            }

            fragmentAccountBinding.progressBar.setVisibility(View.GONE);

        });
    }

    public void logoutFacebookUser() {

        if (AccessToken.getCurrentAccessToken() != null) {
            LoginManager.getInstance().logOut();
            AccessToken.setCurrentAccessToken(null);
            signInToken = "";
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == 100 || requestCode == 101) {

                signInToken = appPreference.getString(SIGNIN_TOKEN);
                userName = appPreference.getString(USER_NAME);
                userAvatar = appPreference.getString(USER_AVATAR);
                String avatarImagePath = USER_STORAGE_BASE_URL.concat(userAvatar);

                fragmentAccountBinding.tvUserName.setText(userName);

                fragmentAccountBinding.tvLoginLogout.setText(getString(R.string.logout));

                Glide.with(AppConstants.mContext)
                        .load(avatarImagePath)
                        .placeholder(R.drawable.placeholder_products)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(fragmentAccountBinding.ivDisplayPicture);

                intent = new Intent(getContext(), OrderListingActivity.class);

            }
        }

    }
}
