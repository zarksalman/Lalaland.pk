package com.lalaland.ecommerce.data.models.otp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OtpDataContainer {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private Otp data;

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

    public Otp getData() {
        return data;
    }

    public void setData(Otp data) {
        this.data = data;
    }

}