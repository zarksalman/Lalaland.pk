package com.lalaland.ecommerce.data.models.cart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartContainer {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private CartData data;

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

    public CartData getData() {
        return data;
    }

    public void setData(CartData data) {
        this.data = data;
    }

}