package com.lalaland.ecommerce.data.models.order.details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDetailContainer {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("data")
    @Expose
    private OrderDetailData data;



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

    public OrderDetailData getData() {
        return data;
    }

    public void setData(OrderDetailData data) {
        this.data = data;
    }

}