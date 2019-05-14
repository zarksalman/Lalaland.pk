package com.lalaland.ecommerce.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.lalaland.ecommerce.data.models.userAddressBook.AddressDataContainer;
import com.lalaland.ecommerce.data.retrofit.LalalandServiceApi;
import com.lalaland.ecommerce.data.retrofit.RetrofitClient;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {

    private static UserRepository repository;
    private LalalandServiceApi lalalandServiceApi;
    private final MutableLiveData<AddressDataContainer> addressDataContainerMutableLiveData = new MutableLiveData<>();

    private UserRepository() {
        lalalandServiceApi = RetrofitClient.getInstance().createClient();
    }

    public static UserRepository getInstance() {
        if (repository == null)
            repository = new UserRepository();

        return repository;
    }

    public LiveData<AddressDataContainer> addNewAddress(String headers, Map<String, String> parameters) {

        lalalandServiceApi.addNewAddress(headers, parameters).enqueue(new Callback<AddressDataContainer>() {
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

    private void checkResponseSource(Response response) {

        if (response.raw().networkResponse() != null) {
            Log.d("response_source", "Response is from network");
        } else {
            Log.d("response_source", "Response is from cache");
        }
    }
}
