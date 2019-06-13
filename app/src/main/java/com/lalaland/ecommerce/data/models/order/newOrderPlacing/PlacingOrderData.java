package com.lalaland.ecommerce.data.models.order.newOrderPlacing;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lalaland.ecommerce.data.models.products.Product;

import java.util.List;

public class PlacingOrderData {


    @SerializedName("recommendation")
    @Expose
    private List<Product> recommendation = null;
    @SerializedName("order_id")
    @Expose
    private Integer orderId;

    public List<Product> getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(List<Product> recommendation) {
        this.recommendation = recommendation;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
}