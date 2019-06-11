package com.lalaland.ecommerce.data.models.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlacingOrderData {
    @SerializedName("order_id")
    @Expose
    private Integer orderId;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
}