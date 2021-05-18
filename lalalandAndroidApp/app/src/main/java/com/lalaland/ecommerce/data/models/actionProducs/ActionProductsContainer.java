package com.lalaland.ecommerce.data.models.actionProducs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActionProductsContainer {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private ActionProductData data;

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

    public ActionProductData getData() {
        return data;
    }

    public void setData(ActionProductData data) {
        this.data = data;
    }

}