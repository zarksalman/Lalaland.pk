package com.lalaland.ecommerce.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.lalaland.ecommerce.data.dao.SearchCategoryDao;
import com.lalaland.ecommerce.data.models.returnAndReplacement.ReturnAndReplacementDataContainer;
import com.lalaland.ecommerce.data.retrofit.LalalandServiceApi;
import com.lalaland.ecommerce.data.retrofit.RetrofitClient;
import com.lalaland.ecommerce.helpers.AppConstants;
import com.lalaland.ecommerce.helpers.AppPreference;
import com.lalaland.ecommerce.helpers.AppUtils;

import java.util.HashMap;
import java.util.Map;

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
}