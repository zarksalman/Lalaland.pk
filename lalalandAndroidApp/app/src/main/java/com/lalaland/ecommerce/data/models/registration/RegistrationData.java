package com.lalaland.ecommerce.data.models.registration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lalaland.ecommerce.data.models.login.User;

public class RegistrationData {
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("last_login")
    @Expose
    private String lastLogin;
    @SerializedName("cart_count")
    @Expose
    private Integer cartCount;
    @SerializedName("recommended_cat")
    @Expose
    private String recommendedCat;
    @SerializedName("user")
    @Expose
    private User user;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Integer getCartCount() {
        return cartCount;
    }

    public void setCartCount(Integer cartCount) {
        this.cartCount = cartCount;
    }

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
}