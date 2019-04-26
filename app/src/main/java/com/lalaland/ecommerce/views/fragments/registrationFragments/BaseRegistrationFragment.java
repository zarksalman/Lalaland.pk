package com.lalaland.ecommerce.views.fragments.registrationFragments;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

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
import com.lalaland.ecommerce.viewModels.RegistrationViewModel;
import com.lalaland.ecommerce.views.activities.MainActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.lalaland.ecommerce.helpers.AppConstants.FACEBOOK_SIGN_UP_IN;
import static com.lalaland.ecommerce.helpers.AppConstants.FAIL_CODE;
import static com.lalaland.ecommerce.helpers.AppConstants.FB_LOGIN_CANCLED;
import static com.lalaland.ecommerce.helpers.AppConstants.FORM_SIGN_UP;
import static com.lalaland.ecommerce.helpers.AppConstants.SIGNIN_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;

public class BaseRegistrationFragment extends Fragment {


    private RegistrationViewModel registrationViewModel;
    private Map<String, String> parameter = new HashMap<>();
    private CallbackManager callbackManager;

    private static final String EMAIL = "email";
    private static final String PUBLIC_PROFILE = "public_profile";

    public BaseRegistrationFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public void loginOrRegisterWithFb(LoginButton loginButton) {


        callbackManager = CallbackManager.Factory.create();  //facebook registration callback

        loginButton.setFragment(this); // specially for fragments
        loginButton.performClick(); // depict as user click on facebook LoginButton but actually clicked on our button

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList(PUBLIC_PROFILE, EMAIL));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String token = loginResult.getAccessToken().getToken();
                Toast.makeText(getContext(), token, Toast.LENGTH_SHORT).show();
                Log.d("token", token);
                Log.d(EMAIL, loginResult.getAccessToken().getPermissions().toString());

                parameter.put("fb_token", token); // token got from facebook
                callToApi(FACEBOOK_SIGN_UP_IN);
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

    public void loginWithForm(Map<String, String> mParameter) {
        parameter = mParameter;
        callToApi(FORM_SIGN_UP);
    }

    public void callToApi(int signUpType) {

        callbackManager = CallbackManager.Factory.create();  //facebook registration callback

        registrationViewModel = ViewModelProviders.of(this).get(RegistrationViewModel.class);

        registrationViewModel.registerUser(parameter, signUpType).observe(this, registrationContainer -> {

            if (registrationContainer != null && getContext() != null) {

                if (registrationContainer.getCode().equals(SUCCESS_CODE)) {
                    Log.d("registerUser", registrationContainer.getData().getName() + ":" + registrationContainer.getData().getEmail());
                    Log.d("registerUser", AppPreference.getInstance(getContext()).getString(SIGNIN_TOKEN));
                    startActivity();

                } else if (registrationContainer.getCode().equals(FAIL_CODE)) {
                    Toast.makeText(getContext(), registrationContainer.getMsg(), Toast.LENGTH_SHORT).show();
                }
            } else
                Toast.makeText(getContext(), "Could not register at this time", Toast.LENGTH_SHORT).show();
        });
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
