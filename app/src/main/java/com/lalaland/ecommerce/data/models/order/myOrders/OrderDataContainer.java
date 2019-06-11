package com.lalaland.ecommerce.data.models.order.myOrders;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDataContainer {

@SerializedName("code")
@Expose
private String code;
@SerializedName("msg")
@Expose
private String msg;
@SerializedName("data")
@Expose
private OrdersData data;

public String getCode() {
return code;
}

public void setCode(String code) {
this.code = code;
}

public String getMsg() {
return msg;
}

public void setMsg(String msg) {
this.msg = msg;
}

public OrdersData getData() {
return data;
}

public void setData(OrdersData data) {
this.data = data;
}

}