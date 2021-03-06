package com.lalaland.ecommerce.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.lalaland.ecommerce.data.dao.SearchCategoryDao;
import com.lalaland.ecommerce.data.models.logout.BasicResponse;
import com.lalaland.ecommerce.data.models.returnAndReplacement.claimListing.ClaimListingDataContainer;
import com.lalaland.ecommerce.data.models.returnAndReplacement.claimListingDetail.ClaimDataContainer;
import com.lalaland.ecommerce.data.models.returnAndReplacement.createClaimDetail.ReturnAndReplacementDataContainer;
import com.lalaland.ecommerce.data.retrofit.LalalandServiceApi;
import com.lalaland.ecommerce.data.retrofit.RetrofitClient;
import com.lalaland.ecommerce.helpers.AppConstants;
import com.lalaland.ecommerce.helpers.AppPreference;
import com.lalaland.ecommerce.helpers.AppUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.lalaland.ecommerce.helpers.AppConstants.CART_SESSION_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.SIGNIN_TOKEN;

public class ReturnAndReplacementRepository {

    private static ReturnAndReplacementRepository repository;
    private LalalandServiceApi lalalandServiceApi;
    private static AppPreference appPreference;
    static String recommendedCat, token, cartSession;
    static Map<String, String> userInfo = new HashMap<>();

    private MutableLiveData<ReturnAndReplacementDataContainer> returnAndReplacementDataContainerMutableLiveData;
    private MutableLiveData<BasicResponse> basicResponseMutableLiveData;
    private MutableLiveData<ClaimListingDataContainer> claimListingDataContainerMutableLiveData;
    private MutableLiveData<ClaimDataContainer> claimDataContainerMutableLiveData;
    private MutableLiveData<ResponseBody> responseBodyMutableLiveData;

    private SearchCategoryDao searchCategoryDao;

    private ReturnAndReplacementRepository() {
        lalalandServiceApi = RetrofitClient.getInstance().createClient();
        appPreference = AppPreference.getInstance(AppConstants.mContext);

    }

    public static ReturnAndReplacementRepository getInstance() {
        if (repository == null) {
            repository = new ReturnAndReplacementRepository();
        }

        return repository;
    }

    private void setUserInfo() {

        if (AppConstants.BANNER_STORAGE_BASE_URL.isEmpty()) {
            AppUtils.getStaticsFromPreferences();
        }

        userInfo.clear();
        token = appPreference.getString(SIGNIN_TOKEN);
        cartSession = appPreference.getString(CART_SESSION_TOKEN);

        if (token.equals("token"))
            token = "";

        userInfo.put("device-id", AppConstants.DEVICE_ID);
        userInfo.put("app-version", AppConstants.APP_BUILD_VERSION);
        userInfo.put("user-id", AppConstants.USER_ID);
        userInfo.put("device-name", AppConstants.DEVICE_NAME);
        userInfo.put("device-model", AppConstants.DEVICE_MODEL);
        userInfo.put("device-OS-version", AppConstants.DEVICE_OS);
        userInfo.put("fcm-token", AppConstants.FCM_TOKEN);
        userInfo.put("device-type", AppConstants.DEVICE_TYPE);
        userInfo.put(SIGNIN_TOKEN, token);
        userInfo.put(CART_SESSION_TOKEN, cartSession);

    }

    public LiveData<ReturnAndReplacementDataContainer> createNewClaim(String order_product_id) {

        setUserInfo();
        returnAndReplacementDataContainerMutableLiveData = new MutableLiveData<>();

        lalalandServiceApi.createNewClaim(userInfo, order_product_id).enqueue(new Callback<ReturnAndReplacementDataContainer>() {
            @Override
            public void onResponse(Call<ReturnAndReplacementDataContainer> call, Response<ReturnAndReplacementDataContainer> response) {

                if (response.isSuccessful()) {
                    returnAndReplacementDataContainerMutableLiveData.postValue(response.body());
                } else {
                    returnAndReplacementDataContainerMutableLiveData.postValue(null);
                }

            }

            @Override
            public void onFailure(Call<ReturnAndReplacementDataContainer> call, Throwable t) {
                returnAndReplacementDataContainerMutableLiveData.postValue(null);
            }
        });

        return returnAndReplacementDataContainerMutableLiveData;
    }

    public LiveData<BasicResponse> newClaimPost(List<MultipartBody.Part> claimImages, Map<String, String> parameter) {

        setUserInfo();
        basicResponseMutableLiveData = new MutableLiveData<>();

        lalalandServiceApi.newClaimPost(userInfo, claimImages, parameter).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {

                if (response.isSuccessful()) {
                    basicResponseMutableLiveData.postValue(response.body());
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

    public LiveData<ClaimListingDataContainer> getClaimList() {

        setUserInfo();
        claimListingDataContainerMutableLiveData = new MutableLiveData<>();

        lalalandServiceApi.getClaimsList(userInfo).enqueue(new Callback<ClaimListingDataContainer>() {
            @Override
            public void onResponse(Call<ClaimListingDataContainer> call, Response<ClaimListingDataContainer> response) {
                if (response.isSuccessful()) {
                    claimListingDataContainerMutableLiveData.postValue(response.body());
                } else {
                    claimListingDataContainerMutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<ClaimListingDataContainer> call, Throwable t) {
                claimListingDataContainerMutableLiveData.postValue(null);
            }
        });

        return claimListingDataContainerMutableLiveData;
    }

    public LiveData<ClaimDataContainer> getClaimDetails(String claimId) {

        setUserInfo();
        claimDataContainerMutableLiveData = new MutableLiveData<>();

        lalalandServiceApi.getClaimDetails(userInfo, claimId).enqueue(new Callback<ClaimDataContainer>() {
            @Override
            public void onResponse(Call<ClaimDataContainer> call, Response<ClaimDataContainer> response) {

                if (response.isSuccessful()) {
                    claimDataContainerMutableLiveData.postValue(response.body());
                } else {
                    claimDataContainerMutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<ClaimDataContainer> call, Throwable t) {
                claimDataContainerMutableLiveData.postValue(null);
            }
        });
        return claimDataContainerMutableLiveData;
    }

    public LiveData<ResponseBody> getWayBill(String url) {

        responseBodyMutableLiveData = new MutableLiveData<>();

        lalalandServiceApi.getWaybill(url).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    responseBodyMutableLiveData.postValue(response.body());
                } else {
                    responseBodyMutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                responseBodyMutableLiveData.postValue(null);
            }
        });

        return responseBodyMutableLiveData;
    }
}