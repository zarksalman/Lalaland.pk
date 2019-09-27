package com.lalaland.ecommerce.views.fragments.registrationFragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.databinding.FragmentSigninBinding;
import com.lalaland.ecommerce.helpers.AnalyticsManager;
import com.lalaland.ecommerce.helpers.AppConstants;
import com.lalaland.ecommerce.helpers.AppPreference;
import com.lalaland.ecommerce.helpers.AppUtils;
import com.lalaland.ecommerce.interfaces.LoadingLogin;
import com.lalaland.ecommerce.viewModels.user.LoginViewModel;
import com.lalaland.ecommerce.views.activities.ResetPasswordActivity;

import java.util.HashMap;
import java.util.Map;

import static com.lalaland.ecommerce.helpers.AppConstants.AUTHORIZATION_FAIL_CODE;
import static com.lalaland.ecommerce.helpers.AppConstants.CART_SESSION_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.DATE_OF_BIRTH;
import static com.lalaland.ecommerce.helpers.AppConstants.GENDER;
import static com.lalaland.ecommerce.helpers.AppConstants.PHONE_NUMBER;
import static com.lalaland.ecommerce.helpers.AppConstants.SIGNIN_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;
import static com.lalaland.ecommerce.helpers.AppConstants.USER_AVATAR;
import static com.lalaland.ecommerce.helpers.AppConstants.USER_NAME;
import static com.lalaland.ecommerce.helpers.AppConstants.VALIDATION_FAIL_CODE;
import static com.lalaland.ecommerce.helpers.AppConstants.user;


public class SigninFragment extends BaseRegistrationFragment implements LoadingLogin {

    Map<String, String> parameter = new HashMap<>();
    AppPreference appPreference;
    private FragmentSigninBinding fragmentSigninBinding;
    private LoginViewModel loginViewModel;
    private String emailOrNumber = "";
    private String password = "";
    private String token = "";
    private String cart_session = "";

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

        fragmentSigninBinding.btnFbSignIn.setOnClickListener(v -> {
            signInOrSignUpWithFb(fragmentSigninBinding.btFacebookSignin, this);
        });

        fragmentSigninBinding.btnGoogleSignIn.setOnClickListener(v -> {
            fragmentSigninBinding.pbLoadingSignin.setVisibility(View.VISIBLE);
            signInOrSignUpWithGoogle(fragmentSigninBinding.btGoogleSignin, this);
        });

        fragmentSigninBinding.btnSignIn.setOnClickListener(v -> signInWithForm());

        fragmentSigninBinding.tvForgotPassword.setOnClickListener(v -> {

            Intent intent = new Intent(getContext(), ResetPasswordActivity.class);
            intent.putExtra("is_reset_password", "true");
            startActivity(intent);
        });
        return fragmentSigninBinding.getRoot();
    }

    void signInWithForm() {

        if (validateEmail() && validatePassword()) {

            if (getActivity() != null) {

                AppUtils.hideKeyboard(getActivity());

                getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }

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

                        Bundle bundle = new Bundle();
                        AnalyticsManager.getInstance().sendAnalytics("login_success", bundle);
                        AnalyticsManager.getInstance().sendAnalytics("login_signup", bundle);
                        AnalyticsManager.getInstance().sendFacebookAnalytics("login_success", bundle);
                        AnalyticsManager.getInstance().sendFacebookAnalytics("login_signup", bundle);

                        AppConstants.user = login.getData().getUser();

                        appPreference.setString(USER_NAME, login.getData().getUser().getName());
                        appPreference.setString(DATE_OF_BIRTH, login.getData().getUser().getDateOfBirth());
                        appPreference.setString(PHONE_NUMBER, login.getData().getUser().getPhone());
                        appPreference.setString(GENDER, login.getData().getUser().getGender());

                        if (user.getAvatar() != null)
                            appPreference.setString(USER_AVATAR, user.getAvatar().toString());
                        else
                            appPreference.setString(USER_AVATAR, "");

                        getActivity().setResult(Activity.RESULT_OK);
                        getActivity().finish();

                        break;
                    case VALIDATION_FAIL_CODE:
                        hideProgressBar();
                        showToast(login.getMsg());
                        break;
                    case AUTHORIZATION_FAIL_CODE:
                        hideProgressBar();
                        showToast(login.getMsg());
                        break;
                }
            }

            hideProgressBar();

            if (getActivity() != null)
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        });
    }

    void hideProgressBar() {
        fragmentSigninBinding.pbLoadingSignin.setVisibility(View.GONE);
    }

    void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void checkLoading(boolean isLoadingStop) {
        if (isLoadingStop) {
            fragmentSigninBinding.pbLoadingSignin.setVisibility(View.GONE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        } else {

            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            fragmentSigninBinding.pbLoadingSignin.setVisibility(View.VISIBLE);
        }


    }
}
