package com.lalaland.ecommerce.data.retrofit;

import com.lalaland.ecommerce.data.models.actionProducs.ActionProductsContainer;
import com.lalaland.ecommerce.data.models.category.CategoryContainer;
import com.lalaland.ecommerce.data.models.home.HomeDataContainer;
import com.lalaland.ecommerce.data.models.login.Login;
import com.lalaland.ecommerce.data.models.logout.BasicResponse;
import com.lalaland.ecommerce.data.models.productDetails.ProductDetailData;
import com.lalaland.ecommerce.data.models.productDetails.ProductDetailDataContainer;
import com.lalaland.ecommerce.data.models.products.ProductContainer;
import com.lalaland.ecommerce.data.models.registration.RegistrationContainer;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface LalalandServiceApi {

    @GET
    Call<String> testResponse(@Url String url);

    @POST("home")
    Call<HomeDataContainer> getHomeData();

    @POST("products")
    Call<ProductContainer> getRangeProducts(@QueryMap Map<String, String> parameters);

    @POST("{action}")
    Call<ActionProductsContainer> getActionProducts(@Path("action") String action, @QueryMap Map<String, String> parameter);

    @POST("getGeneralData")
    Call<CategoryContainer> getCategoryGeneralData();


    @POST("addToCart")
    Call<BasicResponse> addToCart(@QueryMap Map<String, String> parameter);

    @POST("addToWishList")
    Call<BasicResponse> addRemoveToWishList(@QueryMap Map<String, String> parameter);

    @POST("productDetails")
    Call<ProductDetailDataContainer> getProductDetail(@Query("product_id") int product_id);

    @POST("register")
    Call<RegistrationContainer> registerUser(@QueryMap Map<String, String> parameters);

    @POST("login")
    Call<Login> loginUser(@QueryMap Map<String, String> parameters);

    @POST("logout")
    Call<BasicResponse> logoutUser(@Header("token") String token);

    @POST("changePassword")
    Call<BasicResponse> changePassword(@Header("token") String token, @QueryMap Map<String, String> parameters);

    @POST("loginFromFaceBook")
    Call<RegistrationContainer> registerFromFacebook(@QueryMap Map<String, String> parameter);
}
