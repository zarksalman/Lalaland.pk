package com.lalaland.ecommerce.data.retrofit;

import com.lalaland.ecommerce.data.models.login.Login;
import com.lalaland.ecommerce.data.models.logout.BasicResponse;
import com.lalaland.ecommerce.data.models.products.ProductContainer;
import com.lalaland.ecommerce.data.models.registration.RegistrationContainer;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface LalalandServiceApi {

    @POST("products")
    Call<ProductContainer> getRangeProducts(@QueryMap Map<String, String> parameters);

    @POST("register")
    Call<RegistrationContainer> registerUser(@QueryMap Map<String, String> parameters);

    @POST("login")
    Call<Login> loginUser(@QueryMap Map<String, String> parameters);

    @POST("logout")
    Call<BasicResponse> logoutUser(@Header("token") String token);

    @POST("changePassword")
    Call<BasicResponse> changePassword(@Header("token") String token, @QueryMap Map<String, String> parameters);
}
