package com.lalaland.ecommerce.data.repository;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.lalaland.ecommerce.data.dao.SearchCategoryDao;
import com.lalaland.ecommerce.data.database.LalalandDatabases;
import com.lalaland.ecommerce.data.models.actionProducs.ActionProductsContainer;
import com.lalaland.ecommerce.data.models.cart.CartContainer;
import com.lalaland.ecommerce.data.models.categories.CategoriesContainer;
import com.lalaland.ecommerce.data.models.category.CategoryContainer;
import com.lalaland.ecommerce.data.models.filters.FilterDataContainer;
import com.lalaland.ecommerce.data.models.globalSearch.SearchCategory;
import com.lalaland.ecommerce.data.models.globalSearch.SearchDataContainer;
import com.lalaland.ecommerce.data.models.home.HomeDataContainer;
import com.lalaland.ecommerce.data.models.logout.BasicResponse;
import com.lalaland.ecommerce.data.models.order.newOrderPlacing.PlacingOrderDataContainer;
import com.lalaland.ecommerce.data.models.productDetails.ProductDetailDataContainer;
import com.lalaland.ecommerce.data.models.products.ProductContainer;
import com.lalaland.ecommerce.data.models.voucher.VoucherDataContainer;
import com.lalaland.ecommerce.data.models.wishList.WishListContainer;
import com.lalaland.ecommerce.data.retrofit.LalalandServiceApi;
import com.lalaland.ecommerce.data.retrofit.RetrofitClient;
import com.lalaland.ecommerce.helpers.AppConstants;
import com.lalaland.ecommerce.helpers.AppPreference;
import com.lalaland.ecommerce.helpers.AppUtils;
import com.lalaland.ecommerce.interfaces.NetworkInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.lalaland.ecommerce.helpers.AppConstants.CART_SESSION_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.RECOMMENDED_CAT_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.SIGNIN_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;

public class ProductsRepository {

    private static ProductsRepository repository;
    private LalalandServiceApi lalalandServiceApi;
    private static AppPreference appPreference;
    static String recommendedCat, token, cartSession;
    static Map<String, String> userInfo = new HashMap<>();

    private MutableLiveData<BasicResponse> basicResponseMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<HomeDataContainer> homeDataContainerMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<ProductContainer> productContainerMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<ActionProductsContainer> actionProductsContainerMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<ActionProductsContainer> filterActionProductsContainerMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<CategoryContainer> categoryContainerMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<ProductDetailDataContainer> productDetailDataContainerMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<CartContainer> cartContainerMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<PlacingOrderDataContainer> orderDataContainerMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<WishListContainer> wishListContainerMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<CategoriesContainer> categoriesContainerMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<SearchDataContainer> searchDataContainerMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<FilterDataContainer> filterDataContainerMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<VoucherDataContainer> voucherDataContainerMutableLiveData = new MutableLiveData<>();
    private SearchCategoryDao searchCategoryDao;

    private ProductsRepository() {
        lalalandServiceApi = RetrofitClient.getInstance().createClient();
        appPreference = AppPreference.getInstance(AppConstants.mContext);

        searchCategoryDao = LalalandDatabases.getInstance(AppConstants.mContext).searchCategoryDao();
    }

    public static ProductsRepository getInstance() {
        if (repository == null) {
            repository = new ProductsRepository();
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

    public LiveData<CategoryContainer> getCategoryGeneralData(NetworkInterface networkInterface) {

        categoryContainerMutableLiveData = new MutableLiveData<>();
        setUserInfo();
        lalalandServiceApi.getCategoryGeneralData(userInfo).enqueue(new Callback<CategoryContainer>() {
            @Override
            public void onResponse(Call<CategoryContainer> call, Response<CategoryContainer> response) {

                if (response.isSuccessful()) {
                    networkInterface.onFailure(false);
                    categoryContainerMutableLiveData.postValue(response.body());
                } else {

                    networkInterface.onFailure(true);
                    categoryContainerMutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<CategoryContainer> call, Throwable t) {
                networkInterface.onFailure(true);
                categoryContainerMutableLiveData.postValue(null);
            }
        });

        return categoryContainerMutableLiveData;
    }
    public LiveData<HomeDataContainer> getHomeData() {

        recommendedCat = appPreference.getString(RECOMMENDED_CAT_TOKEN);
        homeDataContainerMutableLiveData = new MutableLiveData<>();
        setUserInfo();

        // temporary fix for action images
        Map<String, String> homeParameter = new HashMap<>();
        homeParameter.put("recommended_cat", recommendedCat);
        homeParameter.put("version", "v2");

        lalalandServiceApi.getHomeData(userInfo, homeParameter).enqueue(new Callback<HomeDataContainer>() {
            @Override
            public void onResponse(Call<HomeDataContainer> call, Response<HomeDataContainer> response) {

                if (response.isSuccessful()) {

                    homeDataContainerMutableLiveData.postValue(response.body());
                } else {
                    homeDataContainerMutableLiveData.postValue(null);
                }


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
        setUserInfo();
        lalalandServiceApi.getRangeProducts(userInfo, parameters).enqueue(new Callback<ProductContainer>() {
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
        setUserInfo();
        lalalandServiceApi.getRecommendations(userInfo, parameters).enqueue(new Callback<ProductContainer>() {
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

    public LiveData<ActionProductsContainer> getSearcResult(Map<String, String> parameter) {

        actionProductsContainerMutableLiveData = new MutableLiveData<>();
        setUserInfo();
        lalalandServiceApi.getSearchResult(userInfo, parameter).enqueue(new Callback<ActionProductsContainer>() {
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

    public LiveData<ActionProductsContainer> getActionProducts(String action, Map<String, String> parameter) {

        actionProductsContainerMutableLiveData = new MutableLiveData<>();
        setUserInfo();
        lalalandServiceApi.getActionProducts(userInfo, action, parameter).enqueue(new Callback<ActionProductsContainer>() {
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

    public LiveData<ProductDetailDataContainer> getProductDetail(int productId) {

        recommendedCat = appPreference.getString(RECOMMENDED_CAT_TOKEN);
        productDetailDataContainerMutableLiveData = new MutableLiveData<>();
        setUserInfo();

        lalalandServiceApi.getProductDetail(userInfo, productId, recommendedCat).enqueue(new Callback<ProductDetailDataContainer>() {
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
        setUserInfo();
        lalalandServiceApi.addToCart(userInfo, parameter).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {

                if (response.isSuccessful()) {
                    basicResponseMutableLiveData.postValue(response.body());

                    if (response.body().getCode().equals(SUCCESS_CODE))
                    {
                        // saving header response for different purposes like add to wish list etc
                        Headers headers = response.headers();
                        AppPreference.getInstance(AppConstants.mContext).setString(CART_SESSION_TOKEN, headers.get(CART_SESSION_TOKEN));
                    }

                    if (response.body().getData() != null)
                        AppConstants.CART_COUNTER = response.body().getData().getCartCount();


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
        setUserInfo();

        lalalandServiceApi.addRemoveToWishList(userInfo, parameter).enqueue(new Callback<BasicResponse>() {
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

        cartContainerMutableLiveData = new MutableLiveData<>();
        setUserInfo();
        lalalandServiceApi.getCart(userInfo).enqueue(new Callback<CartContainer>() {
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
        setUserInfo();
        lalalandServiceApi.addToReadyCartList(userInfo, parameter).enqueue(new Callback<BasicResponse>() {
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
        setUserInfo();
        lalalandServiceApi.deleteCartItem(userInfo, parameter).enqueue(new Callback<BasicResponse>() {
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
        setUserInfo();
        lalalandServiceApi.changeCartProductQuantity(userInfo, parameter).enqueue(new Callback<BasicResponse>() {
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

    public LiveData<PlacingOrderDataContainer> confirmOrder(Map<String, String> parameter, NetworkInterface networkInterface) {

        orderDataContainerMutableLiveData = new MutableLiveData<>();
        setUserInfo();
        lalalandServiceApi.confirmOrder(userInfo, parameter).enqueue(new Callback<PlacingOrderDataContainer>() {
            @Override
            public void onResponse(Call<PlacingOrderDataContainer> call, Response<PlacingOrderDataContainer> response) {
                if (response.isSuccessful()) {
                    orderDataContainerMutableLiveData.postValue(response.body());
                    networkInterface.onFailure(false);
                } else {
                    orderDataContainerMutableLiveData.postValue(null);
                    networkInterface.onFailure(true);
                }
            }

            @Override
            public void onFailure(Call<PlacingOrderDataContainer> call, Throwable t) {
                orderDataContainerMutableLiveData.postValue(null);
                networkInterface.onFailure(true);
            }
        });

        return orderDataContainerMutableLiveData;
    }
    public LiveData<WishListContainer> getWishListProducts(String token) {
        wishListContainerMutableLiveData = new MutableLiveData<>();
        setUserInfo();

        lalalandServiceApi.getWishListProducts(userInfo).enqueue(new Callback<WishListContainer>() {
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
        setUserInfo();

        lalalandServiceApi.getCategories(userInfo, id).enqueue(new Callback<CategoriesContainer>() {
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

    public LiveData<SearchDataContainer> searchItems(String queryString) {

        searchDataContainerMutableLiveData = new MutableLiveData<>();
        setUserInfo();

        lalalandServiceApi.globalSearch(userInfo, queryString).enqueue(new Callback<SearchDataContainer>() {
            @Override
            public void onResponse(Call<SearchDataContainer> call, Response<SearchDataContainer> response) {

                if (response.isSuccessful()) {
                    searchDataContainerMutableLiveData.postValue(response.body());
                } else {
                    searchDataContainerMutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<SearchDataContainer> call, Throwable t) {
                searchDataContainerMutableLiveData.postValue(null);
            }
        });

        return searchDataContainerMutableLiveData;
    }

    public LiveData<List<SearchCategory>> getAllSearchCategory() {

        return searchCategoryDao.getAllCategory();
    }

    public LiveData<FilterDataContainer> getFilters(Map<String, String> parameter) {

        filterDataContainerMutableLiveData = new MutableLiveData<>();

        setUserInfo();
        lalalandServiceApi.getFilters(userInfo, parameter).enqueue(new Callback<FilterDataContainer>() {
            @Override
            public void onResponse(Call<FilterDataContainer> call, Response<FilterDataContainer> response) {

                if (response.isSuccessful())
                    filterDataContainerMutableLiveData.postValue(response.body());
                else
                    filterDataContainerMutableLiveData.postValue(null);
            }

            @Override
            public void onFailure(Call<FilterDataContainer> call, Throwable t) {
                filterDataContainerMutableLiveData.postValue(null);
            }
        });

        return filterDataContainerMutableLiveData;
    }

    public LiveData<ActionProductsContainer> applyFilters(Map<String, String> parameter) {

        filterActionProductsContainerMutableLiveData = new MutableLiveData<>();
        setUserInfo();

        lalalandServiceApi.applyFilter(userInfo, parameter).enqueue(new Callback<ActionProductsContainer>() {
            @Override
            public void onResponse(Call<ActionProductsContainer> call, Response<ActionProductsContainer> response) {

                if (response.isSuccessful())
                    filterActionProductsContainerMutableLiveData.postValue(response.body());
                else
                    filterActionProductsContainerMutableLiveData.postValue(null);
            }

            @Override
            public void onFailure(Call<ActionProductsContainer> call, Throwable t) {
                filterActionProductsContainerMutableLiveData.postValue(null);
            }
        });
        return filterActionProductsContainerMutableLiveData;
    }

    public LiveData<VoucherDataContainer> isVoucherValid(Map<String, String> parameters) {

        setUserInfo();
        voucherDataContainerMutableLiveData = new MutableLiveData<>();

        lalalandServiceApi.isVoucherValid(userInfo, parameters).enqueue(new Callback<VoucherDataContainer>() {
            @Override
            public void onResponse(Call<VoucherDataContainer> call, Response<VoucherDataContainer> response) {
                voucherDataContainerMutableLiveData.postValue(response.isSuccessful() ? response.body() : null);
            }

            @Override
            public void onFailure(Call<VoucherDataContainer> call, Throwable t) {
                voucherDataContainerMutableLiveData.postValue(null);
            }
        });

        return voucherDataContainerMutableLiveData;
    }

    public void insertSearch(SearchCategory searchCategory) {
        new InsertSaveSearchAsyTask(searchCategoryDao).execute(searchCategory);
    }

    public void deleteSearch(SearchCategory searchCategory) {
        new DeleteSaveSearchAsyTask(searchCategoryDao).execute(searchCategory);
    }

    public void deleteAllSearch() {
        new DeleteAllSaveSearchAsyTask(searchCategoryDao).execute();
    }

    private void checkResponseSource(Response response) {

        Log.d("response_source", String.valueOf(response.code()) + response.errorBody());

        if (response.raw().networkResponse() != null) {
            Log.d("response_source", "Response is from network");
        } else {
            Log.d("response_source", "Response is from cache");
        }
    }

    private static class DeleteSaveSearchAsyTask extends AsyncTask<SearchCategory, Void, Void> {

        SearchCategoryDao searchCategoryDao;

        private DeleteSaveSearchAsyTask(SearchCategoryDao searchCategoryDao) {
            this.searchCategoryDao = searchCategoryDao;
        }

        @Override
        protected Void doInBackground(SearchCategory... searchCategories) {
            searchCategoryDao.deleteCategory(searchCategories[0]);
            return null;
        }
    }

    private static class DeleteAllSaveSearchAsyTask extends AsyncTask<Void, Void, Void> {

        SearchCategoryDao searchCategoryDao;

        private DeleteAllSaveSearchAsyTask(SearchCategoryDao searchCategoryDao) {
            this.searchCategoryDao = searchCategoryDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            searchCategoryDao.deleteAllSearches();
            return null;
        }
    }

    private static class InsertSaveSearchAsyTask extends AsyncTask<SearchCategory, Void, Void> {

        SearchCategoryDao searchCategoryDao;

        private InsertSaveSearchAsyTask(SearchCategoryDao searchCategoryDao) {
            this.searchCategoryDao = searchCategoryDao;
        }

        @Override
        protected Void doInBackground(SearchCategory... searchCategories) {
            searchCategoryDao.insertCategory(searchCategories[0]);
            return null;
        }
    }

}