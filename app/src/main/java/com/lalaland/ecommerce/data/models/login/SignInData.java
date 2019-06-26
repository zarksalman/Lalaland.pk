package com.lalaland.ecommerce.data.models.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignInData {


    @SerializedName("recommended_cat")
    @Expose
    private String recommendedCat;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("cart_count")
    @Expose
    private Integer cartCount;

    public String getRecommendedCat() {
        return recommendedCat;
    }

    public void setRecommendedCat(String recommendedCat) {
        this.recommendedCat = recommendedCat;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getCartCount() {
        return cartCount;
    }

    public void setCartCount(Integer cartCount) {
        this.cartCount = cartCount;
    }

}