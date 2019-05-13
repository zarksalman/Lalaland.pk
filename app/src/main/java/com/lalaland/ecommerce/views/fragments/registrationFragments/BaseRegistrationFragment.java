package com.lalaland.ecommerce.views.fragments.registrationFragments;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.lalaland.ecommerce.helpers.AppPreference;
import com.lalaland.ecommerce.viewModels.LoginViewModel;
import com.lalaland.ecommerce.viewModels.RegistrationViewModel;
import com.lalaland.ecommerce.views.activities.MainActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.lalaland.ecommerce.helpers.AppConstants.ACCOUNT_CREATION_ERROR;
import static com.lalaland.ecommerce.helpers.AppConstants.AUTHORIZATION_FAIL_CODE;
import static com.lalaland.ecommerce.helpers.AppConstants.CART_SESSION_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.FACEBOOK_SIGN_UP_IN;
import static com.lalaland.ecommerce.helpers.AppConstants.GENERAL_ERROR;
import static com.lalaland.ecommerce.helpers.AppConstants.VALIDATION_FAIL_CODE;
import static com.lalaland.ecommerce.helpers.AppConstants.FB_LOGIN_CANCLED;
import static com.lalaland.ecommerce.helpers.AppConstants.FORM_SIGN_UP;
import static com.lalaland.ecommerce.helpers.AppConstants.SIGNIN_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;
import static com.lalaland.ecommerce.helpers.AppConstants.WRONG_CREDENTIAL;

public class BaseRegistrationFragment extends Fragment {

    private RegistrationViewModel registrationViewModel;
    private LoginViewModel loginViewModel;

    private Map<String, String> parameter = new HashMap<>();
    private CallbackManager callbackManager;

    private static final String EMAIL = "email";
    private static final String PUBLIC_PROFILE = "public_profile";

    private String token, cart_session;
    private AppPreference appPreference;

    public BaseRegistrationFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appPreference = AppPreference.getInstance(getContext());

        cart_session = appPreference.getString(CART_SESSION_TOKEN);
    }

    public void signInOrSignUpWithFb(LoginButton loginButton) {

        callbackManager = CallbackManager.Factory.create();  //facebook registration callback

        loginButton.setFragment(this); // specially for fragments
        loginButton.performClick(); // depict as user click on facebook LoginButton but actually clicked on our button

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList(PUBLIC_PROFILE, EMAIL));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String token = loginResult.getAccessToken().getToken();
                Log.d("token", token);
                Log.d(EMAIL, loginResult.getAccessToken().getPermissions().toString());

                parameter.put("fb_token", token); // token got from facebook
                signUpCallToApi(FACEBOOK_SIGN_UP_IN);
            }

            @Override
            public void onCancel() {
                if (getContext() != null) {
                    Toast.makeText(getContext(), FB_LOGIN_CANCLED, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onError(FacebookException error) {
                String errorMessage = error.getMessage();
                if (getContext() != null) {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });

/*        if (AccessToken.getCurrentAccessToken() != null) {
            fragmentSigninBinding.pbLoading.setVisibility(View.GONE);
        }*/
        //     getHashKeyFb();
        Toast.makeText(getContext(), "Login With Facebook", Toast.LENGTH_SHORT).show();

    }


    private void signUpCallToApi(int signUpType) {


        registrationViewModel = ViewModelProviders.of(this).get(RegistrationViewModel.class);

        registrationViewModel.registerUser(cart_session, parameter, signUpType).observe(this, registrationContainer -> {

            if (registrationContainer != null) {

                switch (registrationContainer.getCode()) {
                    case SUCCESS_CODE:
                        Log.d("registerUser", registrationContainer.getData().getName() + ":" + registrationContainer.getData().getEmail());
                        Log.d("registerUser", AppPreference.getInstance(getContext()).getString(SIGNIN_TOKEN));
                        startActivity();

                        break;
                    case VALIDATION_FAIL_CODE:
                        Toast.makeText(getContext(), registrationContainer.getMsg(), Toast.LENGTH_SHORT).show();
                        break;
                    case AUTHORIZATION_FAIL_CODE:
                        Toast.makeText(getContext(), WRONG_CREDENTIAL, Toast.LENGTH_SHORT).show();
                        break;
                }
            } else
                Toast.makeText(getContext(), ACCOUNT_CREATION_ERROR, Toast.LENGTH_LONG).show();
        });
    }

    void getHashKeyFb() {

        PackageInfo packageInfo = null;
        try {
            packageInfo = getContext().getPackageManager().getPackageInfo("com.lalaland.ecommer", PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        for (Signature signature : packageInfo.signatures) {
            MessageDigest messageDigest = null;
            try {
                messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                Log.d("signatures", Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT));

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void logoutFacebookUser() {

        if (AccessToken.getCurrentAccessToken() != null) {
            LoginManager.getInstance().logOut();
            AccessToken.setCurrentAccessToken(null);
        }
    }

    public void startActivity() {

        if (getContext() != null)
            getContext().startActivity(new Intent(getContext(), MainActivity.class));
    }
}
