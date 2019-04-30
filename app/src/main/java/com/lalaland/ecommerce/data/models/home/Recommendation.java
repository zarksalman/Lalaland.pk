package com.lalaland.ecommerce.data.models.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Recommendation {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("brand_name")
    @Expose
    private String brandName;
    @SerializedName("is_wish_list_item")
    @Expose
    private Object isWishListItem;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("primary_image")
    @Expose
    private String primaryImage;
    @SerializedName("product_variations")
    @Expose
    private String productVariations;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("sale_price")
    @Expose
    private String salePrice;
    @SerializedName("actual_price")
    @Expose
    private String actualPrice;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Object getIsWishListItem() {
        return isWishListItem;
    }

    public void setIsWishListItem(Object isWishListItem) {
        this.isWishListItem = isWishListItem;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrimaryImage() {
        return primaryImage;
    }

    public void setPrimaryImage(String primaryImage) {
        this.primaryImage = primaryImage;
    }

    public String getProductVariations() {
        return productVariations;
    }

    public void setProductVariations(String productVariations) {
        this.productVariations = productVariations;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(String actualPrice) {
        this.actualPrice = actualPrice;
    }

}