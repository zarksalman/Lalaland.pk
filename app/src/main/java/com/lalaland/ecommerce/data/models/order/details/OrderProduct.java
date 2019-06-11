package com.lalaland.ecommerce.data.models.order.details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderProduct {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("primary_image")
    @Expose
    private String primaryImage;
    @SerializedName("order_id")
    @Expose
    private Integer orderId;
    @SerializedName("actual_price")
    @Expose
    private String actualPrice;
    @SerializedName("sale_price")
    @Expose
    private String salePrice;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("product_variation_description")
    @Expose
    private String productVariationDescription;
    @SerializedName("item_quantity")
    @Expose
    private Integer itemQuantity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(String actualPrice) {
        this.actualPrice = actualPrice;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getProductVariationDescription() {
        return productVariationDescription;
    }

    public void setProductVariationDescription(String productVariationDescription) {
        this.productVariationDescription = productVariationDescription;
    }

    public Integer getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(Integer itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

}