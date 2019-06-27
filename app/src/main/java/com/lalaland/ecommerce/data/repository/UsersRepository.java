package com.lalaland.ecommerce.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.lalaland.ecommerce.data.models.login.LoginDataContainer;
import com.lalaland.ecommerce.data.models.logout.BasicResponse;
import com.lalaland.ecommerce.data.models.registration.RegistrationContainer;
import com.lalaland.ecommerce.data.models.updateUserData.UpdateUserDataContainer;
import com.lalaland.ecommerce.data.models.userAddressBook.AddressDataContainer;
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
import static com.lalaland.ecommerce.helpers.AppConstants.RECOMMENDED_CAT_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.SIGNIN_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;

public class UsersRepository {

    private static UsersRepository usersRepository;
    private LalalandServiceApi lalalandServiceApi;
    private AppPreference appPreference;
    private String recommendedCategry;

    private MutableLiveData<RegistrationContainer> registrationContainerMutableLiveData;
    private MutableLiveData<LoginDataContainer> loginMutableLiveData;
    private MutableLiveData<BasicResponse> basicResponseMutableLiveData;
    private MutableLiveData<AddressDataContainer> addressDataContainerMutableLiveData;
    private MutableLiveData<UpdateUserDataContainer> updateUserDataContainerMutableLiveData;

    private UsersRepository() {
        lalalandServiceApi = RetrofitClient.getInstance().createClient();
        appPreference = AppPreference.getInstance(AppConstants.mContext);
    }

    public static UsersRepository getInstance() {
        if (usersRepository == null)
            usersRepository = new UsersRepository();

        return usersRepository;
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

    public LiveData<LoginDataContainer> loginUser(String cart_session, Map<String, String> parameters) {

        recommendedCategry = appPreference.getString(RECOMMENDED_CAT_TOKEN);
        parameters.put(RECOMMENDED_CAT_TOKEN, recommendedCategry);

        loginMutableLiveData = new MutableLiveData<>();

        lalalandServiceApi.loginUser(cart_session, parameters).enqueue(new Callback<LoginDataContainer>() {
            @Override
            public void onResponse(Call<LoginDataContainer> call, Response<LoginDataContainer> response) {

                if (response.isSuccessful()) {

                    loginMutableLiveData.postValue(response.body());

                    if (response.body().getCode().equals(SUCCESS_CODE)) {
                        Headers headers = response.headers();
                        appPreference.setString(SIGNIN_TOKEN, headers.get(SIGNIN_TOKEN));

                        // if login successfully then discard cart session token
                        appPreference.setString(CART_SESSION_TOKEN, "");

                        recommendedCategry = response.body().getData().getRecommendedCat();
                        appPreference.setString(RECOMMENDED_CAT_TOKEN, recommendedCategry);
                        AppConstants.CART_COUNTER = response.body().getData().getCartCount();
                    }


                    checkResponseSource(response);
                } else {
                    loginMutableLiveData.postValue(null);
                }

            }

            @Override
            public void onFailure(Call<LoginDataContainer> call, Throwable t) {
                loginMutableLiveData.postValue(null);
            }
        });

        return loginMutableLiveData;
    }

    public LiveData<BasicResponse> logoutUser() {

        basicResponseMutableLiveData = new MutableLiveData<>();
        // session token
        String token = AppPreference.getInstance(AppConstants.mContext).getString(SIGNIN_TOKEN);

        lalalandServiceApi.logoutUser(token).enqueue(new Callback<BasicResponse>() {

            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {

                if (response.isSuccessful()) {
                    basicResponseMutableLiveData.postValue(response.body());
                    AppPreference.getInstance(AppConstants.mContext).setString(SIGNIN_TOKEN, "");
                    AppConstants.user = null;
                    AppConstants.userAddresses = null;
                    AppConstants.CART_COUNTER = 0;

                    checkResponseSource(response);
                } else {
                    basicResponseMutableLiveData.postValue(null);
                }

            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                basicResponseMutableLiveData.postValue(null);
            }
        });

        return basicResponseMutableLiveData;
    }

    public LiveData<BasicResponse> changePassword(String token, Map<String, String> parameter) {

        basicResponseMutableLiveData = new MutableLiveData<>();

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

    public LiveData<RegistrationContainer> signUpForm(Map<String, String> parameters) {

        recommendedCategry = appPreference.getString(RECOMMENDED_CAT_TOKEN);
        parameters.put(RECOMMENDED_CAT_TOKEN, recommendedCategry);

        registrationContainerMutableLiveData = new MutableLiveData<>();

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

    public LiveData<RegistrationContainer> signUpFacebook(String cart_session, Map<String, String> parameters) {

        recommendedCategry = appPreference.getString(RECOMMENDED_CAT_TOKEN);
        parameters.put(RECOMMENDED_CAT_TOKEN, recommendedCategry);

        registrationContainerMutableLiveData = new MutableLiveData<>();

        lalalandServiceApi.registerFromFacebook(cart_session, parameters).enqueue(new Callback<RegistrationContainer>() {
            @Override
            public void onResponse(Call<RegistrationContainer> call, Response<RegistrationContainer> response) {
                registrationContainerMutableLiveData.postValue(response.body());

                // saving header response for different purposes like add to wish list etc
                Headers headers = response.headers();
                AppPreference.getInstance(AppConstants.mContext).setString(SIGNIN_TOKEN, headers.get(SIGNIN_TOKEN));
                // if login successfully then discard cart session token
                AppPreference.getInstance(AppConstants.mContext).setString(CART_SESSION_TOKEN, "");
                AppConstants.CART_COUNTER = response.body().getData().getCartCount();
                checkResponseSource(response);
            }

            @Override
            public void onFailure(Call<RegistrationContainer> call, Throwable t) {
                registrationContainerMutableLiveData.postValue(null);
            }
        });

        return registrationContainerMutableLiveData;
    }

    public LiveData<AddressDataContainer> addNewAddress(String token, Map<String, String> parameters) {

        addressDataContainerMutableLiveData = new MutableLiveData<>();
        lalalandServiceApi.addNewAddress(token, parameters).enqueue(new Callback<AddressDataContainer>() {
            @Override
            public void onResponse(Call<AddressDataContainer> call, Response<AddressDataContainer> response) {

                if (response.isSuccessful())
                    addressDataContainerMutableLiveData.postValue(response.body());
                else
                    addressDataContainerMutableLiveData.postValue(null);
            }

            @Override
            public void onFailure(Call<AddressDataContainer> call, Throwable t) {
                addressDataContainerMutableLiveData.postValue(null);
            }
        });

        return addressDataContainerMutableLiveData;
    }

    public LiveData<UpdateUserDataContainer> updateUserDetails(String token, Map<String, String> parameter) {

        updateUserDataContainerMutableLiveData = new MutableLiveData<>();
        lalalandServiceApi.updateUserDetails(token, parameter).enqueue(new Callback<UpdateUserDataContainer>() {
            @Override
            public void onResponse(Call<UpdateUserDataContainer> call, Response<UpdateUserDataContainer> response) {
                if (response.isSuccessful()) {
                    updateUserDataContainerMutableLiveData.postValue(response.body());
                } else {
                    updateUserDataContainerMutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<UpdateUserDataContainer> call, Throwable t) {
                updateUserDataContainerMutableLiveData.postValue(null);
            }
        });

        return updateUserDataContainerMutableLiveData;
    }

    public LiveData<AddressDataContainer> getAddresses(String token) {

        addressDataContainerMutableLiveData = new MutableLiveData<>();

        lalalandServiceApi.getAddress(token).enqueue(new Callback<AddressDataContainer>() {
            @Override
            public void onResponse(Call<AddressDataContainer> call, Response<AddressDataContainer> response) {

                if (response.isSuccessful()) {
                    addressDataContainerMutableLiveData.postValue(response.body());
                } else {
                    addressDataContainerMutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<AddressDataContainer> call, Throwable t) {
                addressDataContainerMutableLiveData.postValue(null);
            }
        });

        return addressDataContainerMutableLiveData;
    }
    private void checkResponseSource(Response response) {

        if (response.raw().networkResponse() != null) {
            Log.d("response_source", "Response is from network");
        } else {
            Log.d("response_source", "Response is from cache");
        }
    }
}
