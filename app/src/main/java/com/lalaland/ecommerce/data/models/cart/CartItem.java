package com.lalaland.ecommerce.data.models.cart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartItem {

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    private Boolean isSelected = false;


    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("product_id")
    @Expose
    private Integer productId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("primary_image")
    @Expose
    private String primaryImage;
    @SerializedName("cart_id")
    @Expose
    private Integer cartId;
    @SerializedName("item_quantity")
    @Expose
    private Integer itemQuantity;
    @SerializedName("merchant_name")
    @Expose
    private String merchantName;
    @SerializedName("merchant_id")
    @Expose
    private Integer merchantId;
    @SerializedName("actual_price")
    @Expose
    private String actualPrice;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("sale_price")
    @Expose
    private String salePrice;
    @SerializedName("attribute_value")
    @Expose
    private String attributeValue;
    @SerializedName("cart_status")
    @Expose
    private Integer cartStatus;
    @SerializedName("input_type")
    @Expose
    private String inputType;
    @SerializedName("attribute_name")
    @Expose
    private String attributeName;
    @SerializedName("remaining_quantity")
    @Expose
    private String remainingQuantity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getPrimaryImage() {
        return primaryImage;
    }

    public void setPrimaryImage(String primaryImage) {
        this.primaryImage = primaryImage;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Integer getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(Integer itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public String getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(String actualPrice) {
        this.actualPrice = actualPrice;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public Integer getCartStatus() {
        return cartStatus;
    }

    public void setCartStatus(Integer cartStatus) {
        this.cartStatus = cartStatus;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getRemainingQuantity() {
        return remainingQuantity;
    }

    public void setRemainingQuantity(String remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }

}