package com.lalaland.ecommerce.data.models.cartListingModel;

import com.lalaland.ecommerce.data.models.cart.CartItem;

import java.util.List;

public class CartListModel {

    public CartListModel() {
    }


    private int merchantId;
    private String totalAmount;
    private String totalCharges;

    public String getTotalCharges() {
        return totalCharges;
    }

    public void setTotalCharges(String totalCharges) {
        this.totalCharges = totalCharges;
    }

    public String getMerchantShippingRate() {
        return merchantShippingRate;
    }

    public void setMerchantShippingRate(String merchantShippingRate) {
        this.merchantShippingRate = merchantShippingRate;
    }

    private String merchantShippingRate;

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(int merchantId) {
        this.merchantId = merchantId;
    }

    private String merchantName;

    public List<CartItem> getCartItemList() {
        return cartItemList;
    }

    public void setCartItemList(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
    }

    private List<CartItem> cartItemList;

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }
}
