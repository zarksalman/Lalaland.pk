package com.lalaland.ecommerce.data.models.order.newOrderPlacing;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlacingOrderDataContainer {


    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private PlacingOrderData data;

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

    public PlacingOrderData getData() {
        return data;
    }

    public void setData(PlacingOrderData data) {
        this.data = data;
    }

}