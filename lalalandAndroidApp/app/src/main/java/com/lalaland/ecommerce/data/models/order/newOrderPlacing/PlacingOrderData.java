package com.lalaland.ecommerce.data.models.order.newOrderPlacing;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lalaland.ecommerce.data.models.cart.CartItem;
import com.lalaland.ecommerce.data.models.products.Product;

import java.util.List;

public class PlacingOrderData {


    @SerializedName("recommendation")
    @Expose
    private List<Product> recommendation = null;

    @SerializedName("products")
    @Expose
    private List<CartItem> products = null;

    @SerializedName("order_id")
    @Expose
    private Integer orderId;

    @SerializedName("transaction_id")
    @Expose
    private String transactionId;

    @SerializedName("cart_count")
    @Expose
    private Integer cartCount;

    @SerializedName("Click2Pay")
    @Expose
    private String clickToPayUrl;

    @SerializedName("Click2Pay_return_url")
    @Expose
    private String clickToPayReturnUrl;

    public List<CartItem> getProducts() {
        return products;
    }

    public void setProducts(List<CartItem> products) {
        this.products = products;
    }

    public Integer getCartCount() {
        return cartCount;
    }

    public void setCartCount(Integer cartCount) {
        this.cartCount = cartCount;
    }

    public List<Product> getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(List<Product> recommendation) {
        this.recommendation = recommendation;
    }

    public String getClickToPayUrl() {
        return clickToPayUrl;
    }

    public void setClickToPayUrl(String clickToPayUrl) {
        this.clickToPayUrl = clickToPayUrl;
    }

    public String getClickToPayReturnUrl() {
        return clickToPayReturnUrl;
    }

    public void setClickToPayReturnUrl(String clickToPayReturnUrl) {
        this.clickToPayReturnUrl = clickToPayReturnUrl;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}