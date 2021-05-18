package com.lalaland.ecommerce.data.models.order.details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderDetailData {

    @SerializedName("order_products")
    @Expose
    private List<OrderProduct> orderProducts = null;
    @SerializedName("discount_amount")
    @Expose
    private String discountAmount;
    @SerializedName("shipping_charges")
    @Expose
    private String shippingCharges;
    @SerializedName("order_total")
    @Expose
    private String orderTotal;
    @SerializedName("grand_total")
    @Expose
    private String grandTotal;

    public List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(List<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getShippingCharges() {
        return shippingCharges;
    }

    public void setShippingCharges(String shippingCharges) {
        this.shippingCharges = shippingCharges;
    }

    public String getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(String orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

}