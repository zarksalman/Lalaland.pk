package com.lalaland.ecommerce.views.fragments.registrationFragments;


import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.databinding.FragmentSigninBinding;
import com.lalaland.ecommerce.helpers.AppPreference;
import com.lalaland.ecommerce.viewModels.LoginViewModel;

import java.util.HashMap;
import java.util.Map;

import static com.lalaland.ecommerce.helpers.AppConstants.FAIL_CODE;
import static com.lalaland.ecommerce.helpers.AppConstants.SIGNIN_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;


public class SigninFragment extends BaseRegistrationFragment {

    private FragmentSigninBinding fragmentSigninBinding;
    private LoginViewModel loginViewModel;
    private boolean isFragmentVisible = false;
    private static final String EMAIL = "email";
    private static final String PUBLIC_PROFILE = "public_profile";

    private Map<String, String> parameter = new HashMap<>();

    public SigninFragment() {
        // Required empty public constructor
    }

    public static SigninFragment newInstance() {
        SigninFragment fragment = new SigninFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentSigninBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_signin, container, false);
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);


        fragmentSigninBinding.btnLogin.setOnClickListener(v -> loginOrRegisterWithFb(fragmentSigninBinding.btnFacebookSignin));
        fragmentSigninBinding.btnLogout.setOnClickListener(v -> logoutUser());

        return fragmentSigninBinding.getRoot();
    }

    void loginUser() {
        Map<String, String> parameter = new HashMap<>();

        parameter.put("email", "salman_hameed_23@gmail.com");
        parameter.put("password", "salman123");

        loginViewModel.loginUser(parameter).observe(this, login -> {

            if (login != null) {

                if (login.getCode().equals(SUCCESS_CODE)) {
                    Log.d("registerUser", login.getData().getName() + ":" + login.getData().getEmail());
                    Log.d("registerUser", AppPreference.getInstance(getContext()).getString(SIGNIN_TOKEN));
                } else if (login.getCode().equals(FAIL_CODE)) {
                    Toast.makeText(getContext(), login.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void logoutUser() {

        loginViewModel.logoutUser().observe(this, logout -> {

            if (logout != null) {

                logoutFacebookUser();
                Toast.makeText(getContext(), logout.getMsg(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}
