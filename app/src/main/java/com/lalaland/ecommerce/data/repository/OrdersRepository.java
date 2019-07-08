package com.lalaland.ecommerce.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.lalaland.ecommerce.data.models.DeliveryChargesData.DeliveryChargesContainer;
import com.lalaland.ecommerce.data.models.deliveryOption.DeliveryOptionDataContainer;
import com.lalaland.ecommerce.data.models.logout.BasicResponse;
import com.lalaland.ecommerce.data.models.order.details.OrderDetailContainer;
import com.lalaland.ecommerce.data.models.order.myOrders.OrderDataContainer;
import com.lalaland.ecommerce.data.models.order.newOrderPlacing.PlacingOrderDataContainer;
import com.lalaland.ecommerce.data.retrofit.LalalandServiceApi;
import com.lalaland.ecommerce.data.retrofit.RetrofitClient;
import com.lalaland.ecommerce.helpers.AppConstants;
import com.lalaland.ecommerce.helpers.AppPreference;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersRepository {

    private static OrdersRepository repository;
    private LalalandServiceApi lalalandServiceApi;
    private AppPreference appPreference;
    String recommendedCat;
    private Map<String, String> userInfo = new HashMap<>();

    private MutableLiveData<BasicResponse> basicResponseMutableLiveData;
    private MutableLiveData<DeliveryChargesContainer> deliveryChargesContainerMutableLiveData;
    private MutableLiveData<DeliveryOptionDataContainer> deliveryOptionDataContainerMutableLiveData;
    private MutableLiveData<PlacingOrderDataContainer> orderDataContainerMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<OrderDataContainer> myOrderDataContainerMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<OrderDetailContainer> orderDetailContainerMutableLiveData = new MutableLiveData<>();

    private OrdersRepository() {
        lalalandServiceApi = RetrofitClient.getInstance().createClient();
        appPreference = AppPreference.getInstance(AppConstants.mContext);

        userInfo.put("device-id", AppConstants.DEVICE_ID);
        userInfo.put("app-version", AppConstants.APP_BUILD_VERSION);
        userInfo.put("user-id", AppConstants.USER_ID);
        userInfo.put("device-name", AppConstants.DEVICE_NAME);
        userInfo.put("device-OS-version", AppConstants.DEVICE_OS);
    }

    public static OrdersRepository getInstance() {
        if (repository == null)
            repository = new OrdersRepository();
        return repository;
    }


    public LiveData<DeliveryChargesContainer> getDeliveryCharges(String token, String cityId) {

        //userInfo.put("token", token);
        deliveryChargesContainerMutableLiveData = new MutableLiveData<>();

        lalalandServiceApi.getDeliveryCharges(userInfo, cityId).enqueue(new Callback<DeliveryChargesContainer>() {
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

    public LiveData<DeliveryOptionDataContainer> getDeliveryOption(Map<String, String> parameter) {

        deliveryOptionDataContainerMutableLiveData = new MutableLiveData<>();

        lalalandServiceApi.getDeliveryOption(parameter).enqueue(new Callback<DeliveryOptionDataContainer>() {
            @Override
            public void onResponse(Call<DeliveryOptionDataContainer> call, Response<DeliveryOptionDataContainer> response) {

                if (response.isSuccessful())
                    deliveryOptionDataContainerMutableLiveData.postValue(response.body());
                else
                    deliveryOptionDataContainerMutableLiveData.postValue(null);
            }

            @Override
            public void onFailure(Call<DeliveryOptionDataContainer> call, Throwable t) {
                deliveryOptionDataContainerMutableLiveData.postValue(null);
            }
        });

        return deliveryOptionDataContainerMutableLiveData;
    }


    public LiveData<PlacingOrderDataContainer> confirmOrder(String header, Map<String, String> parameter) {

        orderDataContainerMutableLiveData = new MutableLiveData<>();
        lalalandServiceApi.confirmOrder(header, parameter).enqueue(new Callback<PlacingOrderDataContainer>() {
            @Override
            public void onResponse(Call<PlacingOrderDataContainer> call, Response<PlacingOrderDataContainer> response) {
                if (response.isSuccessful()) {
                    orderDataContainerMutableLiveData.postValue(response.body());
                } else
                    orderDataContainerMutableLiveData.postValue(null);
            }

            @Override
            public void onFailure(Call<PlacingOrderDataContainer> call, Throwable t) {
                orderDataContainerMutableLiveData.postValue(null);
            }
        });

        return orderDataContainerMutableLiveData;
    }

    public LiveData<OrderDataContainer> getMyOrders(String header, String status) {

        myOrderDataContainerMutableLiveData = new MutableLiveData<>();
        lalalandServiceApi.getMyOrders(header, status).enqueue(new Callback<OrderDataContainer>() {
            @Override
            public void onResponse(Call<OrderDataContainer> call, Response<OrderDataContainer> response) {

                if (response.isSuccessful()) {
                    myOrderDataContainerMutableLiveData.postValue(response.body());
                } else {
                    myOrderDataContainerMutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<OrderDataContainer> call, Throwable t) {
                myOrderDataContainerMutableLiveData.postValue(null);
            }
        });

        return myOrderDataContainerMutableLiveData;
    }

    public LiveData<OrderDetailContainer> getMyOrdersProducts(String header, String orderId) {

        orderDetailContainerMutableLiveData = new MutableLiveData<>();

        lalalandServiceApi.getMyOrdersProducts(header, orderId).enqueue(new Callback<OrderDetailContainer>() {
            @Override
            public void onResponse(Call<OrderDetailContainer> call, Response<OrderDetailContainer> response) {

                if (response.isSuccessful())
                    orderDetailContainerMutableLiveData.postValue(response.body());
                else
                    orderDetailContainerMutableLiveData.postValue(null);
            }

            @Override
            public void onFailure(Call<OrderDetailContainer> call, Throwable t) {
                orderDetailContainerMutableLiveData.postValue(null);
            }
        });

        return orderDetailContainerMutableLiveData;
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