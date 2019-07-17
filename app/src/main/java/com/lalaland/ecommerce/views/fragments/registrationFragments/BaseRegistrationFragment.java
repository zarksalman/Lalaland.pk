package com.lalaland.ecommerce.views.fragments.registrationFragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.helpers.AppPreference;
import com.lalaland.ecommerce.interfaces.LoadingLogin;
import com.lalaland.ecommerce.viewModels.user.LoginViewModel;
import com.lalaland.ecommerce.viewModels.user.RegistrationViewModel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.lalaland.ecommerce.helpers.AppConstants.ACCOUNT_CREATION_ERROR;
import static com.lalaland.ecommerce.helpers.AppConstants.AUTHORIZATION_FAIL_CODE;
import static com.lalaland.ecommerce.helpers.AppConstants.AVATER;
import static com.lalaland.ecommerce.helpers.AppConstants.CART_SESSION_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.DATE_OF_BIRTH;
import static com.lalaland.ecommerce.helpers.AppConstants.FACEBOOK_SIGN_UP_IN;
import static com.lalaland.ecommerce.helpers.AppConstants.FB_LOGIN_CANCLED;
import static com.lalaland.ecommerce.helpers.AppConstants.GENDER;
import static com.lalaland.ecommerce.helpers.AppConstants.GOOGLE_SIGN_UP_IN;
import static com.lalaland.ecommerce.helpers.AppConstants.NAME;
import static com.lalaland.ecommerce.helpers.AppConstants.PHONE_NUMBER;
import static com.lalaland.ecommerce.helpers.AppConstants.SIGNIN_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;
import static com.lalaland.ecommerce.helpers.AppConstants.TAG;
import static com.lalaland.ecommerce.helpers.AppConstants.USER_AVATAR;
import static com.lalaland.ecommerce.helpers.AppConstants.USER_NAME;
import static com.lalaland.ecommerce.helpers.AppConstants.VALIDATION_FAIL_CODE;
import static com.lalaland.ecommerce.helpers.AppConstants.WRONG_CREDENTIAL;
import static com.lalaland.ecommerce.helpers.AppConstants.mContext;

public class BaseRegistrationFragment extends Fragment {

    private RegistrationViewModel registrationViewModel;
    private LoginViewModel loginViewModel;

    private Map<String, String> parameter = new HashMap<>();
    private CallbackManager callbackManager;

    private static final String EMAIL = "email";
    private static final String PUBLIC_PROFILE = "public_profile";

    private String token, cart_session;
    private AppPreference appPreference;
    GoogleSignInOptions gso;
    GoogleApiClient mGoogleApiClient;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount account;
    LoadingLogin mLoadingLogin;

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


    public void signInOrSignUpWithGoogle(SignInButton signInButton, LoadingLogin loadingLogin) {

        mLoadingLogin = loadingLogin;

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
                .requestScopes(new Scope(Scopes.PROFILE))
                .requestScopes(new Scope(Scopes.PLUS_ME))
                .requestIdToken(getString(R.string.client_id))
                .requestServerAuthCode(getString(R.string.client_id))
                .requestEmail()
                .requestProfile()
                .build();

        // Build GoogleAPIClient with the Google Sign-In API and the above options.
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

        account = GoogleSignIn.getLastSignedInAccount(getContext());

        // means user is login otherwise not login
        if (account == null) {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, 201);
        }
    }

    public void logoutGoogle() {
        mGoogleSignInClient.signOut();
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {

            mLoadingLogin.checkLoading(false);

            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            Log.d(TAG, "account_info" + account.getDisplayName());
            Log.d(TAG, "account_info" + account.getFamilyName());
            Log.d(TAG, "account_info" + account.getEmail());
            Log.d(TAG, "account_info" + account.getIdToken());
            Log.d(TAG, "account_info" + account.getId());
            Log.d(TAG, "account_info" + account.getPhotoUrl());

            parameter.clear();

            try {

                parameter.put(EMAIL, account.getEmail());
                parameter.put(NAME, account.getDisplayName());
                parameter.put(AVATER, account.getPhotoUrl().toString());
                parameter.put("google_id", account.getId());

                mGoogleSignInClient.signOut();
                signUpCallToApi(GOOGLE_SIGN_UP_IN);


            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }

            // Signed in successfully, show authenticated UI.
            //updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            //     updateUI(null);
        }
    }

    public void signInOrSignUpWithFb(LoginButton loginButton, LoadingLogin loadingLogin) {

        mLoadingLogin = loadingLogin;
        callbackManager = CallbackManager.Factory.create();  //facebook registration callback

        if (AccessToken.getCurrentAccessToken() != null) {
            LoginManager.getInstance().logOut();
            AccessToken.setCurrentAccessToken(null);
        }

        loginButton.setFragment(this); // specially for fragments
        loginButton.performClick(); // depict as user click on facebook LoginButton but actually clicked on our button

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList(PUBLIC_PROFILE, EMAIL));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                loadingLogin.checkLoading(false);
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
                mLoadingLogin.checkLoading(true);
            }

            @Override
            public void onError(FacebookException error) {
                String errorMessage = error.getMessage();
                if (getContext() != null) {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }

                mLoadingLogin.checkLoading(true);

            }
        });

    }


    private void signUpCallToApi(int signUpType) {

        registrationViewModel = ViewModelProviders.of(this).get(RegistrationViewModel.class);

        registrationViewModel.registerUser(cart_session, parameter, signUpType).observe(this, registrationContainer -> {

            if (registrationContainer != null) {

                switch (registrationContainer.getCode()) {
                    case SUCCESS_CODE:
                        Log.d("registerUser", registrationContainer.getData().getUser().getName() + ":" + registrationContainer.getData().getUser().getEmail());
                        Log.d("registerUser", AppPreference.getInstance(getContext()).getString(SIGNIN_TOKEN));

                        AppPreference.getInstance(mContext).setString(USER_NAME, registrationContainer.getData().getUser().getName());
                        AppPreference.getInstance(mContext).setString(DATE_OF_BIRTH, registrationContainer.getData().getUser().getDateOfBirth());
                        AppPreference.getInstance(mContext).setString(PHONE_NUMBER, registrationContainer.getData().getUser().getPhone());
                        AppPreference.getInstance(mContext).setString(GENDER, registrationContainer.getData().getUser().getGender());
                        AppPreference.getInstance(mContext).setString(EMAIL, registrationContainer.getData().getUser().getEmail());
                        AppPreference.getInstance(mContext).setString(USER_AVATAR, registrationContainer.getData().getUser().getAvatar().toString());


                        getActivity().setResult(Activity.RESULT_OK);
                        getActivity().finish();
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

            mLoadingLogin.checkLoading(true);
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 201) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);

        } else
            callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
