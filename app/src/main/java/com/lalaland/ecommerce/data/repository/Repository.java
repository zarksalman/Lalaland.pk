package com.lalaland.ecommerce.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.lalaland.ecommerce.data.models.actionProducs.ActionProducts;
import com.lalaland.ecommerce.data.models.actionProducs.ActionProductsContainer;
import com.lalaland.ecommerce.data.models.login.Login;
import com.lalaland.ecommerce.data.models.logout.BasicResponse;
import com.lalaland.ecommerce.data.models.products.ProductContainer;
import com.lalaland.ecommerce.data.models.registration.RegistrationContainer;
import com.lalaland.ecommerce.data.retrofit.LalalandServiceApi;
import com.lalaland.ecommerce.data.retrofit.RetrofitClient;
import com.lalaland.ecommerce.helpers.AppConstants;
import com.lalaland.ecommerce.helpers.AppPreference;

import java.util.Map;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.lalaland.ecommerce.helpers.AppConstants.CART_SESSION_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.FACEBOOK_SIGN_UP_IN;
import static com.lalaland.ecommerce.helpers.AppConstants.FORM_SIGN_UP;
import static com.lalaland.ecommerce.helpers.AppConstants.GOOGLE_SIGN_UP_IN;
import static com.lalaland.ecommerce.helpers.AppConstants.SIGNIN_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;

public class Repository {

    private static Repository repository;
    private LalalandServiceApi lalalandServiceApi;
    private final MutableLiveData<RegistrationContainer> registrationContainerMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<Login> loginMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<BasicResponse> basicResponseMutableLiveData = new MutableLiveData<>();

    private Repository() {
        lalalandServiceApi = RetrofitClient.getInstance().createClient();
    }

    public static Repository getInstance() {
        if (repository == null)
            repository = new Repository();

        return repository;
    }


    public LiveData<RegistrationContainer> registerUser(String cart_session, Map<String, String> parameters, int signUpType) {

        switch (signUpType) {

            case FORM_SIGN_UP:
                return signUpForm(parameters);

            case FACEBOOK_SIGN_UP_IN:
                return signUpFacebook(cart_session, parameters);

            case GOOGLE_SIGN_UP_IN:
                return signUpFacebook(cart_session, parameters);

            default:
                return null; // if type is no selected any of above
        }
    }

    public LiveData<Login> loginUser(String cart_session, Map<String, String> parameters) {

        lalalandServiceApi.loginUser(cart_session, parameters).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {

                if (!response.isSuccessful()) {
                    loginMutableLiveData.postValue(null);
                    return;
                }

                loginMutableLiveData.postValue(response.body());
                Headers headers = response.headers();
                AppPreference.getInstance(AppConstants.mContext).setString(SIGNIN_TOKEN, headers.get(SIGNIN_TOKEN));
                AppPreference.getInstance(AppConstants.mContext).setString(CART_SESSION_TOKEN, ""); // if login successfully then discard cart session token

                checkResponseSource(response);
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                loginMutableLiveData.postValue(null);
            }
        });

        return loginMutableLiveData;
    }

    public LiveData<BasicResponse> logoutUser() {

        // session token
        String token = AppPreference.getInstance(AppConstants.mContext).getString(SIGNIN_TOKEN);

        lalalandServiceApi.logoutUser(token).enqueue(new Callback<BasicResponse>() {

            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                basicResponseMutableLiveData.postValue(response.body());
                AppPreference.getInstance(AppConstants.mContext).setString(SIGNIN_TOKEN, SIGNIN_TOKEN);
                checkResponseSource(response);
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                basicResponseMutableLiveData.postValue(null);
            }
        });

        return basicResponseMutableLiveData;
    }

    public LiveData<BasicResponse> changePassword(Map<String, String> parameter) {

        // session token
        String token = AppPreference.getInstance(AppConstants.mContext).getString(SIGNIN_TOKEN);

        lalalandServiceApi.changePassword(token, parameter).enqueue(new Callback<BasicResponse>() {

            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                basicResponseMutableLiveData.postValue(response.body());
                AppPreference.getInstance(AppConstants.mContext).setString(SIGNIN_TOKEN, SIGNIN_TOKEN);
                checkResponseSource(response);
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                basicResponseMutableLiveData.postValue(null);
            }
        });

        return basicResponseMutableLiveData;
    }

    LiveData<RegistrationContainer> signUpForm(Map<String, String> parameters) {


        lalalandServiceApi.registerUser(parameters).enqueue(new Callback<RegistrationContainer>() {
            @Override
            public void onResponse(Call<RegistrationContainer> call, Response<RegistrationContainer> response) {
                registrationContainerMutableLiveData.postValue(response.body());
                Headers headers = response.headers();
                AppPreference.getInstance(AppConstants.mContext).setString(SIGNIN_TOKEN, headers.get(SIGNIN_TOKEN));
                checkResponseSource(response);
            }

            @Override
            public void onFailure(Call<RegistrationContainer> call, Throwable t) {
                registrationContainerMutableLiveData.postValue(null);
            }
        });

        return registrationContainerMutableLiveData;
    }

    LiveData<RegistrationContainer> signUpFacebook(String cart_session, Map<String, String> parameters) {


        lalalandServiceApi.registerFromFacebook(cart_session, parameters).enqueue(new Callback<RegistrationContainer>() {
            @Override
            public void onResponse(Call<RegistrationContainer> call, Response<RegistrationContainer> response) {
                registrationContainerMutableLiveData.postValue(response.body());

                // saving header response for different purposes like add to wish list etc
                Headers headers = response.headers();
                AppPreference.getInstance(AppConstants.mContext).setString(SIGNIN_TOKEN, headers.get(SIGNIN_TOKEN));

                // if login successfully then discard cart session token
                AppPreference.getInstance(AppConstants.mContext).setString(CART_SESSION_TOKEN, "");

                checkResponseSource(response);
            }

            @Override
            public void onFailure(Call<RegistrationContainer> call, Throwable t) {
                registrationContainerMutableLiveData.postValue(null);
            }
        });

        return registrationContainerMutableLiveData;
    }

    private void checkResponseSource(Response response) {

        if (response.raw().networkResponse() != null) {
            Log.d("response_source", "Response is from network");
        } else {
            Log.d("response_source", "Response is from cache");
        }
    }
}
