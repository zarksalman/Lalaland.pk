package com.lalaland.ecommerce.data.retrofit;

import com.lalaland.ecommerce.data.models.DeliveryChargesData.DeliveryChargesContainer;
import com.lalaland.ecommerce.data.models.actionProducs.ActionProductsContainer;
import com.lalaland.ecommerce.data.models.cart.CartContainer;
import com.lalaland.ecommerce.data.models.categories.CategoriesContainer;
import com.lalaland.ecommerce.data.models.category.CategoryContainer;
import com.lalaland.ecommerce.data.models.globalSearch.SearchDataContainer;
import com.lalaland.ecommerce.data.models.home.HomeDataContainer;
import com.lalaland.ecommerce.data.models.login.LoginDataContainer;
import com.lalaland.ecommerce.data.models.logout.BasicResponse;
import com.lalaland.ecommerce.data.models.order.details.OrderDetailContainer;
import com.lalaland.ecommerce.data.models.order.myOrders.OrderDataContainer;
import com.lalaland.ecommerce.data.models.order.newOrderPlacing.PlacingOrderDataContainer;
import com.lalaland.ecommerce.data.models.productDetails.ProductDetailDataContainer;
import com.lalaland.ecommerce.data.models.products.ProductContainer;
import com.lalaland.ecommerce.data.models.registration.RegistrationContainer;
import com.lalaland.ecommerce.data.models.updateUserData.UpdateUserDataContainer;
import com.lalaland.ecommerce.data.models.userAddressBook.AddressDataContainer;
import com.lalaland.ecommerce.data.models.wishList.WishListContainer;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface LalalandServiceApi {

    @GET
    Call<String> testResponse(@Url String url);

    @POST("home")
    Call<HomeDataContainer> getHomeData(@Query("recommended_cat") String recommendedCategory);

    @POST("getRecommendations")
    Call<ProductContainer> getRecommendations(@QueryMap Map<String, String> parameters);

    @POST("products")
    Call<ProductContainer> getRangeProducts(@QueryMap Map<String, String> parameters);

    @POST("wishList")
    Call<WishListContainer> getWishListProducts(@Header("token") String token);

    @POST("{action}")
    Call<ActionProductsContainer> getActionProducts(@Path("action") String action, @QueryMap Map<String, String> parameter);

    @POST("getGeneralData")
    Call<CategoryContainer> getCategoryGeneralData();

    @POST("addToCart")
    Call<BasicResponse> addToCart(@HeaderMap Map<String, String> headers, @QueryMap Map<String, String> parameter);

    @POST("addToWishList")
    Call<BasicResponse> addRemoveToWishList(@HeaderMap Map<String, String> header, @QueryMap Map<String, String> parameter);

    @POST("productDetails")
    Call<ProductDetailDataContainer> getProductDetail(@Query("product_id") int product_id, @Query("recommended_cat") String recommendedCategory);

    @POST("categories")
    Call<CategoriesContainer> getCategories(@Query("id") String id);

    @POST("getDeliveryCharges")
    Call<DeliveryChargesContainer> getDeliveryCharges(@Header("token") String token, @Query("city_id") String city_id);

    @POST("addressBook")
    Call<DeliveryChargesContainer> getAddress(@Header("token") String token);

    @POST("cart")
    Call<CartContainer> getCart(@HeaderMap Map<String, String> headers);

    @POST("deleteCartItem")
    Call<BasicResponse> deleteCartItem(@HeaderMap Map<String, String> header, @QueryMap Map<String, String> parameter);

    @POST("changeCartStatusAsReady")
    Call<BasicResponse> addToReadyCartList(@HeaderMap Map<String, String> header, @QueryMap Map<String, String> parameter);

    @POST("changeCartProductQuantity")
    Call<BasicResponse> changeCartProductQuantity(@QueryMap Map<String, String> parameter);

    @POST("confirmOrder")
    Call<PlacingOrderDataContainer> confirmOrder(@Header("token") String token, @QueryMap Map<String, String> parameter);

    @POST("myOrders")
    Call<OrderDataContainer> getMyOrders(@Header("token") String token, @Query("status") String status);

    @POST("myOrderProducts")
    Call<OrderDetailContainer> getMyOrdersProducts(@Header("token") String token, @Query("order_id") String orderId);

    @POST("register")
    Call<RegistrationContainer> registerUser(@QueryMap Map<String, String> parameters);

    @POST("addAddress")
    Call<AddressDataContainer> addNewAddress(@Header("token") String cart_session, @QueryMap Map<String, String> parameter);

    @POST("updateUserDetails")
    Call<UpdateUserDataContainer> updateUserDetails(@Header("token") String token, @QueryMap Map<String, String> parameter);

    @POST("login")
    Call<LoginDataContainer> loginUser(@Header("cart-session") String cart_session, @QueryMap Map<String, String> parameters);

    @POST("loginFromFaceBook")
    Call<RegistrationContainer> registerFromFacebook(@Header("cart-session") String cart_session, @QueryMap Map<String, String> parameter);

    @POST("logout")
    Call<BasicResponse> logoutUser(@Header("token") String token);

    @POST("changePassword")
    Call<BasicResponse> changePassword(@Header("token") String token, @QueryMap Map<String, String> parameters);

    @POST("globalSearch")
    Call<SearchDataContainer> globalSearch(@Query("qstr") String queryString);
}
