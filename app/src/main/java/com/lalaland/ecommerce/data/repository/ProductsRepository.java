package com.lalaland.ecommerce.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.lalaland.ecommerce.data.models.actionProducs.ActionProductsContainer;
import com.lalaland.ecommerce.data.models.category.CategoryContainer;
import com.lalaland.ecommerce.data.models.home.HomeDataContainer;
import com.lalaland.ecommerce.data.models.login.Login;
import com.lalaland.ecommerce.data.models.logout.BasicResponse;
import com.lalaland.ecommerce.data.models.productDetails.ProductDetailDataContainer;
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

public class ProductsRepository {

    private static ProductsRepository repository;
    private LalalandServiceApi lalalandServiceApi;

    private final MutableLiveData<BasicResponse> basicResponseMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<HomeDataContainer> homeDataContainerMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<ProductContainer> productContainerMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<ActionProductsContainer> actionProductsContainerMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<CategoryContainer> categoryContainerMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<ProductDetailDataContainer> productDetailDataContainerMutableLiveData = new MutableLiveData<>();

    private ProductsRepository() {
        lalalandServiceApi = RetrofitClient.getInstance().createClient();
    }

    public static ProductsRepository getInstance() {
        if (repository == null)
            repository = new ProductsRepository();

        return repository;
    }

    public LiveData<HomeDataContainer> getHomeData() {

        lalalandServiceApi.getHomeData().enqueue(new Callback<HomeDataContainer>() {
            @Override
            public void onResponse(Call<HomeDataContainer> call, Response<HomeDataContainer> response) {

                if (!response.isSuccessful()) {
                    homeDataContainerMutableLiveData.postValue(null);
                    return;
                }
                homeDataContainerMutableLiveData.postValue(response.body());

                checkResponseSource(response);
            }

            @Override
            public void onFailure(Call<HomeDataContainer> call, Throwable t) {
                homeDataContainerMutableLiveData.postValue(null);
            }
        });

        return homeDataContainerMutableLiveData;
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

    public LiveData<ActionProductsContainer> getActionProducts(String action, Map<String, String> parameter) {

        lalalandServiceApi.getActionProducts(action, parameter).enqueue(new Callback<ActionProductsContainer>() {
            @Override
            public void onResponse(Call<ActionProductsContainer> call, Response<ActionProductsContainer> response) {

                if (response.isSuccessful()) {
                    actionProductsContainerMutableLiveData.postValue(response.body());
                } else {
                    actionProductsContainerMutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<ActionProductsContainer> call, Throwable t) {
                actionProductsContainerMutableLiveData.postValue(null);
            }
        });

        return actionProductsContainerMutableLiveData;
    }

    public LiveData<CategoryContainer> getCategoryGeneralData() {

        lalalandServiceApi.getCategoryGeneralData().enqueue(new Callback<CategoryContainer>() {
            @Override
            public void onResponse(Call<CategoryContainer> call, Response<CategoryContainer> response) {

                if (response.isSuccessful()) {
                    categoryContainerMutableLiveData.postValue(response.body());
                } else {
                    categoryContainerMutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<CategoryContainer> call, Throwable t) {
                categoryContainerMutableLiveData.postValue(null);
            }
        });

        return categoryContainerMutableLiveData;
    }

    public LiveData<ProductDetailDataContainer> getProductDetail(int product_id) {

        lalalandServiceApi.getProductDetail(product_id).enqueue(new Callback<ProductDetailDataContainer>() {
            @Override
            public void onResponse(Call<ProductDetailDataContainer> call, Response<ProductDetailDataContainer> response) {

                if (response.isSuccessful())
                    productDetailDataContainerMutableLiveData.postValue(response.body());
                else
                    productDetailDataContainerMutableLiveData.postValue(null);

            }

            @Override
            public void onFailure(Call<ProductDetailDataContainer> call, Throwable t) {
                productDetailDataContainerMutableLiveData.postValue(null);
            }
        });

        return productDetailDataContainerMutableLiveData;
    }

    public LiveData<BasicResponse> addToCart(Map<String, String> parameter) {

        lalalandServiceApi.addToCart(parameter).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {

                if (response.isSuccessful()) {
                    basicResponseMutableLiveData.postValue(response.body());
                } else
                    basicResponseMutableLiveData.postValue(null);
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                basicResponseMutableLiveData.postValue(null);
            }
        });

        return basicResponseMutableLiveData;
    }

    public LiveData<BasicResponse> addRemoveToWishList(Map<String, String> parameter) {

        lalalandServiceApi.addRemoveToWishList(parameter).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {

                if (response.isSuccessful()) {
                    basicResponseMutableLiveData.postValue(response.body());
                } else
                    basicResponseMutableLiveData.postValue(null);
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                basicResponseMutableLiveData.postValue(null);
            }
        });

        return basicResponseMutableLiveData;
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
