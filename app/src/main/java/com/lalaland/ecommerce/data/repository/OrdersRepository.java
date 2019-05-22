package com.lalaland.ecommerce.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.lalaland.ecommerce.data.models.DeliveryChargesData.DeliveryChargesContainer;
import com.lalaland.ecommerce.data.models.logout.BasicResponse;
import com.lalaland.ecommerce.data.retrofit.LalalandServiceApi;
import com.lalaland.ecommerce.data.retrofit.RetrofitClient;
import com.lalaland.ecommerce.helpers.AppConstants;
import com.lalaland.ecommerce.helpers.AppPreference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersRepository {

    private static OrdersRepository repository;
    private LalalandServiceApi lalalandServiceApi;
    private AppPreference appPreference;
    String recommendedCat;

    private MutableLiveData<BasicResponse> basicResponseMutableLiveData;
    private MutableLiveData<DeliveryChargesContainer> deliveryChargesContainerMutableLiveData;

    private OrdersRepository() {
        lalalandServiceApi = RetrofitClient.getInstance().createClient();
        appPreference = AppPreference.getInstance(AppConstants.mContext);
    }

    public static OrdersRepository getInstance() {
        if (repository == null)
            repository = new OrdersRepository();

        return repository;
    }


    public LiveData<DeliveryChargesContainer> getDeliveryCharges(String token, String cityId) {

        deliveryChargesContainerMutableLiveData = new MutableLiveData<>();

        lalalandServiceApi.getDeliveryCharges(token, cityId).enqueue(new Callback<DeliveryChargesContainer>() {
            @Override
            public void onResponse(Call<DeliveryChargesContainer> call, Response<DeliveryChargesContainer> response) {

                if (response.isSuccessful()) {

                    deliveryChargesContainerMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<DeliveryChargesContainer> call, Throwable t) {

            }
        });

        return deliveryChargesContainerMutableLiveData;
    }

    private void checkResponseSource(Response response) {

        Log.d("response_source", String.valueOf(response.code()) + response.errorBody());

        if (response.raw().networkResponse() != null) {
            Log.d("response_source", "Response is from network");
        } else {
            Log.d("response_source", "Response is from cache");
        }
    }
}