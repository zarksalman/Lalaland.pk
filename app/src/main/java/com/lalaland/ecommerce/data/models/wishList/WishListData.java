package com.lalaland.ecommerce.data.models.wishList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WishListData {

    @SerializedName("wishListProducts")
    @Expose
    private List<WishListProduct> wishListProducts = null;

    public List<WishListProduct> getWishListProducts() {
        return wishListProducts;
    }

    public void setWishListProducts(List<WishListProduct> wishListProducts) {
        this.wishListProducts = wishListProducts;
    }

}