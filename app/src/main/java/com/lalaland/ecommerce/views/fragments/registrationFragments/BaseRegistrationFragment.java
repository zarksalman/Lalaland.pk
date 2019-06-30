package com.lalaland.ecommerce.views.fragments.registrationFragments;


import android.app.Activity;
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

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.lalaland.ecommerce.helpers.AppPreference;
import com.lalaland.ecommerce.viewModels.user.LoginViewModel;
import com.lalaland.ecommerce.viewModels.user.RegistrationViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.lalaland.ecommerce.helpers.AppConstants.ACCOUNT_CREATION_ERROR;
import static com.lalaland.ecommerce.helpers.AppConstants.AUTHORIZATION_FAIL_CODE;
import static com.lalaland.ecommerce.helpers.AppConstants.CART_SESSION_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.DATE_OF_BIRTH;
import static com.lalaland.ecommerce.helpers.AppConstants.FACEBOOK_SIGN_UP_IN;
import static com.lalaland.ecommerce.helpers.AppConstants.FB_LOGIN_CANCLED;
import static com.lalaland.ecommerce.helpers.AppConstants.GENDER;
import static com.lalaland.ecommerce.helpers.AppConstants.PHONE_NUMBER;
import static com.lalaland.ecommerce.helpers.AppConstants.SIGNIN_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;
import static com.lalaland.ecommerce.helpers.AppConstants.TAG;
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

    public void signInOrSignUpWithGoogle(SignInButton signInButton) {


/*


        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
                .requestIdToken(getString(R.string.server_client_id))
                .requestServerAuthCode(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        // Build GoogleAPIClient with the Google Sign-In API and the above options.
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);


        mGoogleSignInClient.signOut();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());

        // means user is login otherwise not login
        if (account == null) {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, 201);

            //   getAccessToken();
        }
        //updateUI(account);
*/


    }

    void getAccessToken() {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("grant_type", "authorization_code")
                .add("client_id", "122765248262-03m7dmonpdot2a59ertdtovcgkja4sq5.apps.googleusercontent.com")
                .add("client_secret", "hZdYEq9KJxDRp_MzbW1JvXWT")
                .add("redirect_uri", "")
                .add("code", "4/4-GMMhmHCXhWEzkobqIHGG_EnNYYsAkukHspeYUk9E8")
                .build();

        final Request request = new Request.Builder()
                .url("https://www.googleapis.com/oauth2/v4/token")
                .post(requestBody)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {

                    JSONObject jsonObject = new JSONObject(response.body().string());
                    final String message = jsonObject.toString(5);
                    Log.d(TAG, "onResponse:" + message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {

            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            Toast.makeText(getContext(), account.getServerAuthCode(), Toast.LENGTH_SHORT).show();


            // Signed in successfully, show authenticated UI.
            //updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            //     updateUI(null);
        }
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
                        Log.d("registerUser", registrationContainer.getData().getUser().getName() + ":" + registrationContainer.getData().getUser().getEmail());
                        Log.d("registerUser", AppPreference.getInstance(getContext()).getString(SIGNIN_TOKEN));

                        AppPreference.getInstance(mContext).setString(USER_NAME, registrationContainer.getData().getUser().getName());
                        AppPreference.getInstance(mContext).setString(DATE_OF_BIRTH, registrationContainer.getData().getUser().getDateOfBirth());
                        AppPreference.getInstance(mContext).setString(PHONE_NUMBER, registrationContainer.getData().getUser().getPhone());
                        AppPreference.getInstance(mContext).setString(GENDER, registrationContainer.getData().getUser().getGender());
                        AppPreference.getInstance(mContext).setString(EMAIL, registrationContainer.getData().getUser().getEmail());

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

        if (requestCode == 201) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.


            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else
            callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
