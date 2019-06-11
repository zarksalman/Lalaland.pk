package com.lalaland.ecommerce.data.models.order.details;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDetailData {

@SerializedName("order_products")
@Expose
private List<OrderProduct> orderProducts = null;

public List<OrderProduct> getOrderProducts() {
return orderProducts;
}

public void setOrderProducts(List<OrderProduct> orderProducts) {
this.orderProducts = orderProducts;
}

}