package com.lalaland.ecommerce.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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

import static com.lalaland.ecommerce.helpers.AppConstants.FACEBOOK_SIGN_UP_IN;
import static com.lalaland.ecommerce.helpers.AppConstants.FORM_SIGN_UP;
import static com.lalaland.ecommerce.helpers.AppConstants.GOOGLE_SIGN_UP_IN;
import static com.lalaland.ecommerce.helpers.AppConstants.SIGNIN_TOKEN;

public class Repository {

    private static Repository repository;
    private LalalandServiceApi lalalandServiceApi;
    private final MutableLiveData<RegistrationContainer> registrationContainerMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<ProductContainer> productContainerMutableLiveData = new MutableLiveData<>();
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

    public LiveData<ProductContainer> getRangeProducts(Map<String, String> parameters) {

        lalalandServiceApi.getRangeProducts(parameters).enqueue(new Callback<ProductContainer>() {
            @Override
            public void onResponse(Call<ProductContainer> call, Response<ProductContainer> response) {
                productContainerMutableLiveData.postValue(response.body());
                checkResponseSource(response);
            }

            @Override
            public void onFailure(Call<ProductContainer> call, Throwable t) {
                productContainerMutableLiveData.postValue(null);
            }
        });

        return productContainerMutableLiveData;
    }

    public LiveData<RegistrationContainer> registerUser(Map<String, String> parameters, int signUpType) {

        switch (signUpType) {

            case FORM_SIGN_UP:
                return signUpForm(parameters);

            case FACEBOOK_SIGN_UP_IN:
                return signUpFacebook(parameters);

            case GOOGLE_SIGN_UP_IN:
                return signUpFacebook(parameters);

            default:
                return null; // if type is no selected any of above
        }
    }

    public LiveData<Login> loginUser(Map<String, String> parameters) {

        lalalandServiceApi.loginUser(parameters).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {

                if (!response.isSuccessful()) {
                    loginMutableLiveData.postValue(null);
                    return;
                }

                loginMutableLiveData.postValue(response.body());
                Headers headers = response.headers();
                AppPreference.getInstance(AppConstants.mContext).setString(SIGNIN_TOKEN, headers.get(SIGNIN_TOKEN));
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

    LiveData<RegistrationContainer> signUpFacebook(Map<String, String> parameters) {


        lalalandServiceApi.registerFromFacebook(parameters).enqueue(new Callback<RegistrationContainer>() {
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

    private void checkResponseSource(Response response) {

        if (response.raw().networkResponse() != null) {
            Log.d("response_source", "Response is from network");
        } else {
            Log.d("response_source", "Response is from cache");
        }
    }
}
