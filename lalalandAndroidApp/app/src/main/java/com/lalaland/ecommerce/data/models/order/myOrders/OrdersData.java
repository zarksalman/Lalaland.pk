package com.lalaland.ecommerce.data.models.order.myOrders;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrdersData {

@SerializedName("orders")
@Expose
private List<Order> orders = null;
@SerializedName("order_status")
@Expose
private List<OrderStatus> orderStatus = null;

public List<Order> getOrders() {
return orders;
}

public void setOrders(List<Order> orders) {
this.orders = orders;
}

public List<OrderStatus> getOrderStatus() {
return orderStatus;
}

public void setOrderStatus(List<OrderStatus> orderStatus) {
this.orderStatus = orderStatus;
}

}