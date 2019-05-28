package com.lalaland.ecommerce.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.lalaland.ecommerce.data.models.actionProducs.ActionProductsContainer;
import com.lalaland.ecommerce.data.models.cart.CartContainer;
import com.lalaland.ecommerce.data.models.categories.CategoriesContainer;
import com.lalaland.ecommerce.data.models.category.CategoryContainer;
import com.lalaland.ecommerce.data.models.home.HomeDataContainer;
import com.lalaland.ecommerce.data.models.logout.BasicResponse;
import com.lalaland.ecommerce.data.models.order.OrderDataContainer;
import com.lalaland.ecommerce.data.models.productDetails.ProductDetailDataContainer;
import com.lalaland.ecommerce.data.models.products.ProductContainer;
import com.lalaland.ecommerce.data.models.wishList.WishListContainer;
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
import static com.lalaland.ecommerce.helpers.AppConstants.RECOMMENDED_CAT_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;

public class ProductsRepository {

    private static ProductsRepository repository;
    private LalalandServiceApi lalalandServiceApi;
    private AppPreference appPreference;
    String recommendedCat;

    private MutableLiveData<BasicResponse> basicResponseMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<HomeDataContainer> homeDataContainerMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<ProductContainer> productContainerMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<ActionProductsContainer> actionProductsContainerMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<CategoryContainer> categoryContainerMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<ProductDetailDataContainer> productDetailDataContainerMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<CartContainer> cartContainerMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<OrderDataContainer> orderDataContainerMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<WishListContainer> wishListContainerMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<CategoriesContainer> categoriesContainerMutableLiveData = new MutableLiveData<>();

    private ProductsRepository() {
        lalalandServiceApi = RetrofitClient.getInstance().createClient();
        appPreference = AppPreference.getInstance(AppConstants.mContext);
    }

    public static ProductsRepository getInstance() {
        if (repository == null)
            repository = new ProductsRepository();

        return repository;
    }

    public LiveData<HomeDataContainer> getHomeData() {

        recommendedCat = appPreference.getString(RECOMMENDED_CAT_TOKEN);
        homeDataContainerMutableLiveData = new MutableLiveData<>();

        lalalandServiceApi.getHomeData(recommendedCat).enqueue(new Callback<HomeDataContainer>() {
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

        productContainerMutableLiveData = new MutableLiveData<>();
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

    public LiveData<ProductContainer> getRecommendations(Map<String, String> parameters) {

        productContainerMutableLiveData = new MutableLiveData<>();
        lalalandServiceApi.getRecommendations(parameters).enqueue(new Callback<ProductContainer>() {
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

        actionProductsContainerMutableLiveData = new MutableLiveData<>();
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

        cartContainerMutableLiveData = new MutableLiveData<>();
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

    public LiveData<ProductDetailDataContainer> getProductDetail(int productId) {

        recommendedCat = appPreference.getString(RECOMMENDED_CAT_TOKEN);
        productDetailDataContainerMutableLiveData = new MutableLiveData<>();

        lalalandServiceApi.getProductDetail(productId, recommendedCat).enqueue(new Callback<ProductDetailDataContainer>() {
            @Override
            public void onResponse(Call<ProductDetailDataContainer> call, Response<ProductDetailDataContainer> response) {

                if (response.isSuccessful()) {
                    productDetailDataContainerMutableLiveData.postValue(response.body());

                    recommendedCat = response.body().getData().getRecommendedCat();
                    appPreference.setString(RECOMMENDED_CAT_TOKEN, recommendedCat);
                }
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

    public LiveData<BasicResponse> addToCart(Map<String, String> headers, Map<String, String> parameter) {

        basicResponseMutableLiveData = new MutableLiveData<>();
        lalalandServiceApi.addToCart(headers, parameter).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {

                if (response.isSuccessful()) {
                    basicResponseMutableLiveData.postValue(response.body());

                    // saving header response for different purposes like add to wish list etc
                    Headers headers = response.headers();
                    AppPreference.getInstance(AppConstants.mContext).setString(CART_SESSION_TOKEN, headers.get(CART_SESSION_TOKEN));

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

    public LiveData<BasicResponse> addRemoveToWishList(Map<String, String> headers, Map<String, String> parameter) {

        basicResponseMutableLiveData = new MutableLiveData<>();

        lalalandServiceApi.addRemoveToWishList(headers, parameter).enqueue(new Callback<BasicResponse>() {
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

    public LiveData<CartContainer> getCart(Map<String, String> headers) {

        lalalandServiceApi.getCart(headers).enqueue(new Callback<CartContainer>() {
            @Override
            public void onResponse(Call<CartContainer> call, Response<CartContainer> response) {

                if (response.isSuccessful()) {
                    cartContainerMutableLiveData.postValue(response.body());
                } else
                    cartContainerMutableLiveData.postValue(null);
            }

            @Override
            public void onFailure(Call<CartContainer> call, Throwable t) {
                cartContainerMutableLiveData.postValue(null);
            }
        });

        return cartContainerMutableLiveData;
    }

    public LiveData<BasicResponse> addToReadyCartList(Map<String, String> header, Map<String, String> parameter) {

        basicResponseMutableLiveData = new MutableLiveData<>();
        lalalandServiceApi.addToReadyCartList(header, parameter).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {

                if (response.isSuccessful()) {

                    if (response.body().getCode().equals(SUCCESS_CODE)) {
                        basicResponseMutableLiveData.postValue(response.body());
                    } else {
                        basicResponseMutableLiveData.postValue(null);
                    }
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                basicResponseMutableLiveData.postValue(null);
            }
        });
        return basicResponseMutableLiveData;
    }

    public LiveData<BasicResponse> deleteCartItem(Map<String, String> header, Map<String, String> parameter) {

        basicResponseMutableLiveData = new MutableLiveData<>();
        lalalandServiceApi.deleteCartItem(header, parameter).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {

                if (response.isSuccessful()) {

                    if (response.body().getCode().equals(SUCCESS_CODE)) {
                        basicResponseMutableLiveData.postValue(response.body());
                    } else {
                        basicResponseMutableLiveData.postValue(null);
                    }
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                basicResponseMutableLiveData.postValue(null);
            }
        });
        return basicResponseMutableLiveData;
    }

    public LiveData<BasicResponse> changeCartProductQuantity(Map<String, String> parameter) {

        basicResponseMutableLiveData = new MutableLiveData<>();
        lalalandServiceApi.changeCartProductQuantity(parameter).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {

                if (response.isSuccessful()) {

                    if (response.body().getCode().equals(SUCCESS_CODE)) {
                        basicResponseMutableLiveData.postValue(response.body());
                    } else {
                        basicResponseMutableLiveData.postValue(null);
                    }
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                basicResponseMutableLiveData.postValue(null);
            }
        });
        return basicResponseMutableLiveData;
    }

    public LiveData<OrderDataContainer> confirmOrder(String header, Map<String, String> parameter) {

        orderDataContainerMutableLiveData = new MutableLiveData<>();
        lalalandServiceApi.confirmOrder(header, parameter).enqueue(new Callback<OrderDataContainer>() {
            @Override
            public void onResponse(Call<OrderDataContainer> call, Response<OrderDataContainer> response) {
                if (response.isSuccessful()) {
                    orderDataContainerMutableLiveData.postValue(response.body());
                } else
                    orderDataContainerMutableLiveData.postValue(null);
            }

            @Override
            public void onFailure(Call<OrderDataContainer> call, Throwable t) {
                orderDataContainerMutableLiveData.postValue(null);
            }
        });

        return orderDataContainerMutableLiveData;
    }

    public LiveData<WishListContainer> getWishListProducts(String token) {
        wishListContainerMutableLiveData = new MutableLiveData<>();

        lalalandServiceApi.getWishListProducts(token).enqueue(new Callback<WishListContainer>() {
            @Override
            public void onResponse(Call<WishListContainer> call, Response<WishListContainer> response) {

                if (response.isSuccessful()) {
                    wishListContainerMutableLiveData.postValue(response.body());
                } else {
                    wishListContainerMutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<WishListContainer> call, Throwable t) {
                wishListContainerMutableLiveData.postValue(null);
            }
        });

        return wishListContainerMutableLiveData;
    }

    public LiveData<CategoriesContainer> getCategories(String id) {

        categoriesContainerMutableLiveData = new MutableLiveData<>();

        lalalandServiceApi.getCategories(id).enqueue(new Callback<CategoriesContainer>() {
            @Override
            public void onResponse(Call<CategoriesContainer> call, Response<CategoriesContainer> response) {

                if (response.isSuccessful()) {
                    categoriesContainerMutableLiveData.postValue(response.body());
                } else {
                    categoriesContainerMutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<CategoriesContainer> call, Throwable t) {
                categoriesContainerMutableLiveData.postValue(null);
            }
        });

        return categoriesContainerMutableLiveData;
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