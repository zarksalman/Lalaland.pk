package com.lalaland.ecommerce.data.retrofit;

import com.lalaland.ecommerce.data.models.DeliveryChargesData.DeliveryChargesContainer;
import com.lalaland.ecommerce.data.models.actionProducs.ActionProductsContainer;
import com.lalaland.ecommerce.data.models.cart.CartContainer;
import com.lalaland.ecommerce.data.models.categories.CategoriesContainer;
import com.lalaland.ecommerce.data.models.category.CategoryContainer;
import com.lalaland.ecommerce.data.models.deliveryOption.DeliveryOptionDataContainer;
import com.lalaland.ecommerce.data.models.filters.FilterDataContainer;
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
import com.lalaland.ecommerce.data.models.uploadProfileImage.UploadProfileImageContainer;
import com.lalaland.ecommerce.data.models.userAddressBook.AddressDataContainer;
import com.lalaland.ecommerce.data.models.wishList.WishListContainer;

import java.util.Map;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface LalalandServiceApi {

    @GET
    Call<String> testResponse(@Url String url);

    @POST("getGeneralData")
    Call<CategoryContainer> getCategoryGeneralData(@HeaderMap Map<String, String> headers);

    @POST("home")
    Call<HomeDataContainer> getHomeData(@HeaderMap Map<String, String> header, @Query("recommended_cat") String recommendedCategory);

    @POST("products")
    Call<ProductContainer> getRangeProducts(@HeaderMap Map<String, String> header, @QueryMap Map<String, String> parameters);


    @POST("getRecommendations")
    Call<ProductContainer> getRecommendations(@HeaderMap Map<String, String> header, @QueryMap Map<String, String> parameters);

    @POST("wishList")
    Call<WishListContainer> getWishListProducts(@HeaderMap Map<String, String> header);
//    Call<WishListContainer> getWishListProducts(@Header("token") String token);

    @POST("{action}")
    Call<ActionProductsContainer> getActionProducts(@HeaderMap Map<String, String> header, @Path("action") String action, @QueryMap Map<String, String> parameter);

    @POST("searchResults")
    Call<ActionProductsContainer> getSearchResult(@HeaderMap Map<String, String> header, @QueryMap Map<String, String> parameter);

    @POST("addToCart")
    Call<BasicResponse> addToCart(@HeaderMap Map<String, String> headers, @QueryMap Map<String, String> parameter);

    @POST("addToWishList")
    Call<BasicResponse> addRemoveToWishList(@HeaderMap Map<String, String> header, @QueryMap Map<String, String> parameter);

    @POST("productDetails")
    Call<ProductDetailDataContainer> getProductDetail(@HeaderMap Map<String, String> header, @Query("product_id") int product_id, @Query("recommended_cat") String recommendedCategory);

    @POST("categories")
    Call<CategoriesContainer> getCategories(@HeaderMap Map<String, String> header, @Query("id") String id);

    @POST("getDeliveryCharges")
    Call<DeliveryChargesContainer> getDeliveryCharges(@HeaderMap Map<String, String> userInfo, @Query("city_id") String city_id);
//    Call<DeliveryChargesContainer> getDeliveryCharges(@Header("token") String token, @Query("city_id") String city_id);

    @POST("getDeliveryOption")
    Call<DeliveryOptionDataContainer> getDeliveryOption(@HeaderMap Map<String, String> userInfo, @QueryMap Map<String, String> parameter);

    @POST("addressBook")
    Call<AddressDataContainer> getAddress(@HeaderMap Map<String, String> userInfo);

    @POST("cart")
    Call<CartContainer> getCart(@HeaderMap Map<String, String> headers);

    @POST("deleteCartItem")
    Call<BasicResponse> deleteCartItem(@HeaderMap Map<String, String> header, @QueryMap Map<String, String> parameter);

    @POST("changeCartStatusAsReady")
    Call<BasicResponse> addToReadyCartList(@HeaderMap Map<String, String> header, @QueryMap Map<String, String> parameter);

    @POST("changeCartProductQuantity")
    Call<BasicResponse> changeCartProductQuantity(@HeaderMap Map<String, String> header, @QueryMap Map<String, String> parameter);

    @POST("confirmOrder")
    Call<PlacingOrderDataContainer> confirmOrder(@HeaderMap Map<String, String> header, @QueryMap Map<String, String> parameter);
//    Call<PlacingOrderDataContainer> confirmOrder(@Header("token") String token, @QueryMap Map<String, String> parameter);

    @POST("myOrders")
    Call<OrderDataContainer> getMyOrders(@HeaderMap Map<String, String> userInfo, @Query("status") String status);

    @POST("myOrderProducts")
    Call<OrderDetailContainer> getMyOrdersProducts(@HeaderMap Map<String, String> userInfo, @Query("order_id") String orderId);

    @POST("register")
    Call<RegistrationContainer> registerUser(@HeaderMap Map<String, String> userInfo, @QueryMap Map<String, String> parameters);

    @POST("addAddress")
    Call<AddressDataContainer> addNewAddress(@HeaderMap Map<String, String> userInfo, @QueryMap Map<String, String> parameter);

    @POST("editAddress")
    Call<AddressDataContainer> editAddress(@HeaderMap Map<String, String> userInfo, @QueryMap Map<String, String> parameter);


    @POST("updateUserDetails")
    Call<UpdateUserDataContainer> updateUserDetails(@HeaderMap Map<String, String> userInfo, @QueryMap Map<String, String> parameter);

    @Multipart
    @POST("uploadProfileImage")
    Call<UploadProfileImageContainer> uploadProfileImage(@HeaderMap Map<String, String> userInfo, @Part MultipartBody.Part avatar, @Part("avatar") RequestBody description);

    @POST("login")
    Call<LoginDataContainer> loginUser(@HeaderMap Map<String, String> userInfo, @QueryMap Map<String, String> parameters);

    @POST("loginFromFaceBook")
    Call<RegistrationContainer> registerFromFacebook(@HeaderMap Map<String, String> userInfo, @QueryMap Map<String, String> parameter);

    @POST("loginFromGoogleAndroid")
    Call<RegistrationContainer> registerFromGoogle(@HeaderMap Map<String, String> userInfo, @QueryMap Map<String, String> parameter);

    @POST("logout")
    Call<BasicResponse> logoutUser(@HeaderMap Map<String, String> userInfo);

    @POST("changePassword")
    Call<BasicResponse> changePassword(@HeaderMap Map<String, String> userInfo, @QueryMap Map<String, String> parameters);
//    Call<BasicResponse> changePassword(@Header("token") String token, @QueryMap Map<String, String> parameters);

    @POST("forgotPassword")
    Call<BasicResponse> forgotPassword(@HeaderMap Map<String, String> userInfo, @QueryMap Map<String, String> parameters);
    //    Call<BasicResponse> changePassword(@Header("token") String token, @QueryMap Map<String, String> parameters);

    @POST("resetPassword")
    Call<RegistrationContainer> resetPassword(@HeaderMap Map<String, String> userInfo, @QueryMap Map<String, String> parameters);
    //    Call<BasicResponse> changePassword(@Header("token") String token, @QueryMap Map<String, String> parameters);

    @POST("globalSearch")
    Single<SearchDataContainer> globalRxSearch(@HeaderMap Map<String, String> userInfo, @Query("qstr") String queryString);

    @POST("globalSearch")
    Call<SearchDataContainer> globalSearch(@HeaderMap Map<String, String> header, @Query("qstr") String queryString);

    @POST("productsFilters")
    Call<FilterDataContainer> getFilters(@HeaderMap Map<String, String> header, @QueryMap Map<String, String> parameters);

    @POST("categoryProducts")
    Call<ActionProductsContainer> applyFilter(@HeaderMap Map<String, String> header, @QueryMap Map<String, String> parameters);
}
