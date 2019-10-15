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
    @SerializedName("status_id")
    @Expose
    private Integer statusId;
    @SerializedName("claim_id")
    @Expose
    private Object claimId;
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("is_claim_allowed")
    @Expose
    private Object isClaimAllowed;
    @SerializedName("order_product_id")
    @Expose
    private Integer orderProductId;
    @SerializedName("is_claim")
    @Expose
    private String isClaim;

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

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public Object getClaimId() {
        return claimId;
    }

    public void setClaimId(Object claimId) {
        this.claimId = claimId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Object getIsClaimAllowed() {
        return isClaimAllowed;
    }

    public void setIsClaimAllowed(Object isClaimAllowed) {
        this.isClaimAllowed = isClaimAllowed;
    }

    public Integer getOrderProductId() {
        return orderProductId;
    }

    public void setOrderProductId(Integer orderProductId) {
        this.orderProductId = orderProductId;
    }

    public String getIsClaim() {
        return isClaim;
    }

    public void setIsClaim(String isClaim) {
        this.isClaim = isClaim;
    }

}