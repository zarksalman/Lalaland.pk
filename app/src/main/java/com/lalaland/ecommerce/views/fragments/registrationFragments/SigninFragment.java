package com.lalaland.ecommerce.views.fragments.registrationFragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.databinding.FragmentSigninBinding;
import com.lalaland.ecommerce.helpers.AppPreference;
import com.lalaland.ecommerce.viewModels.LoginViewModel;

import java.util.HashMap;
import java.util.Map;

import static com.lalaland.ecommerce.helpers.AppConstants.AUTHORIZATION_FAIL_CODE;
import static com.lalaland.ecommerce.helpers.AppConstants.CART_SESSION_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.GENERAL_ERROR;
import static com.lalaland.ecommerce.helpers.AppConstants.SIGNIN_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;
import static com.lalaland.ecommerce.helpers.AppConstants.VALIDATION_FAIL_CODE;
import static com.lalaland.ecommerce.helpers.AppConstants.WRONG_CREDENTIAL;


public class SigninFragment extends BaseRegistrationFragment {

    private FragmentSigninBinding fragmentSigninBinding;
    private LoginViewModel loginViewModel;

    private String emailOrNumber = "";
    private String password = "";
    private String token = "";
    private String cart_session = "";

    Map<String, String> parameter = new HashMap<>();
    AppPreference appPreference;

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

        appPreference = AppPreference.getInstance(getContext());
        token = appPreference.getString(SIGNIN_TOKEN);
        cart_session = appPreference.getString(CART_SESSION_TOKEN);

        fragmentSigninBinding.btnSignIn.setOnClickListener(v -> signInWithForm());
        fragmentSigninBinding.btnFbSignIn.setOnClickListener(v -> signInOrSignUpWithFb(fragmentSigninBinding.btFacebookSignin));
        return fragmentSigninBinding.getRoot();
    }

    void signInWithForm() {

        if (validateEmail() && validatePassword()) {

            fragmentSigninBinding.pbLoadingSignin.setVisibility(View.VISIBLE);
            parameter.put("email", emailOrNumber);
            parameter.put("password", password);
            signInCallToApi();
        }
    }

    private boolean validateEmail() {

        emailOrNumber = fragmentSigninBinding.etEmailSignin.getText().toString().trim();

        if (emailOrNumber.isEmpty()) {
            fragmentSigninBinding.tiEmailSignin.setError("Email or Number could not be empty");
            return false;
        }

        fragmentSigninBinding.tiEmailSignin.setError(null);
        return true;
    }

    private boolean validatePassword() {


        password = fragmentSigninBinding.etPasswordSignin.getText().toString().trim();

        if (password.isEmpty()) {
            fragmentSigninBinding.tiPasswordSignin.setError("Password could not be empty");
            return false;
        }

        fragmentSigninBinding.tiPasswordSignin.setError(null);
        return true;
    }


    private void signInCallToApi() {

        loginViewModel.loginUser(cart_session, parameter).observe(this, login -> {

            if (login != null) {

                switch (login.getCode()) {
                    case SUCCESS_CODE:
                        Log.d("registerUser", login.getData().getUser().getName() + ":" + login.getData().getUser().getEmail());
                        Log.d("registerUser", AppPreference.getInstance(getContext()).getString(SIGNIN_TOKEN));
                        startActivity();
                        break;
                    case VALIDATION_FAIL_CODE:
                        hideProgressBar();
                        showToast(login.getMsg());
                        break;
                    case AUTHORIZATION_FAIL_CODE:
                        hideProgressBar();
                        showToast(WRONG_CREDENTIAL);
                        break;
                }
            } else {
                showToast(GENERAL_ERROR);
                hideProgressBar();
            }

        });
    }

    void hideProgressBar() {
        fragmentSigninBinding.pbLoadingSignin.setVisibility(View.GONE);
    }

    void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
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
