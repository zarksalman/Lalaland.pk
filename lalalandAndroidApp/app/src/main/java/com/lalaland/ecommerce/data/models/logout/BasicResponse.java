package com.lalaland.ecommerce.data.models.logout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BasicResponse {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private BasicResponseData data;

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

    public BasicResponseData getData() {
        return data;
    }

    public void setData(BasicResponseData data) {
        this.data = data;
    }

}