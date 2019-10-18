package com.lalaland.ecommerce.data.models.returnAndReplacement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReturnAndReplacementDataContainer {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private ReturnAndReplacementData data;

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

    public ReturnAndReplacementData getData() {
        return data;
    }

    public void setData(ReturnAndReplacementData data) {
        this.data = data;
    }
}