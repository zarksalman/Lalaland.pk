package com.lalaland.ecommerce.data.models.returnAndReplacement.claimListing;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Claim {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("fancy_return_id")
    @Expose
    private String fancyReturnId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("return_type")
    @Expose
    private Integer returnType;
    @SerializedName("approval_status")
    @Expose
    private Integer approvalStatus;
    @SerializedName("reject_reason")
    @Expose
    private String rejectReason;
    @SerializedName("shipment_tracking_url")
    @Expose
    private Object shipmentTrackingUrl;
    @SerializedName("order_id")
    @Expose
    private Integer orderId;
    @SerializedName("product_id")
    @Expose
    private Integer productId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("SKU")
    @Expose
    private String sKU;
    @SerializedName("claimed_variants")
    @Expose
    private String claimedVariants;
    @SerializedName("sale_price")
    @Expose
    private String salePrice;
    @SerializedName("item_quantity")
    @Expose
    private Integer itemQuantity;
    @SerializedName("product_variation_description")
    @Expose
    private String productVariationDescription;
    @SerializedName("primary_image")
    @Expose
    private String primaryImage;
    @SerializedName("display_name")
    @Expose
    private String displayName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFancyReturnId() {
        return fancyReturnId;
    }

    public void setFancyReturnId(String fancyReturnId) {
        this.fancyReturnId = fancyReturnId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getReturnType() {
        return returnType;
    }

    public void setReturnType(Integer returnType) {
        this.returnType = returnType;
    }

    public Integer getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(Integer approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public Object getShipmentTrackingUrl() {
        return shipmentTrackingUrl;
    }

    public void setShipmentTrackingUrl(Object shipmentTrackingUrl) {
        this.shipmentTrackingUrl = shipmentTrackingUrl;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSKU() {
        return sKU;
    }

    public void setSKU(String sKU) {
        this.sKU = sKU;
    }

    public String getClaimedVariants() {
        return claimedVariants;
    }

    public void setClaimedVariants(String claimedVariants) {
        this.claimedVariants = claimedVariants;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public Integer getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(Integer itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getProductVariationDescription() {
        return productVariationDescription;
    }

    public void setProductVariationDescription(String productVariationDescription) {
        this.productVariationDescription = productVariationDescription;
    }

    public String getPrimaryImage() {
        return primaryImage;
    }

    public void setPrimaryImage(String primaryImage) {
        this.primaryImage = primaryImage;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}