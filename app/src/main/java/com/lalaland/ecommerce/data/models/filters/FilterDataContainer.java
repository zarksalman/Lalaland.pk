package com.lalaland.ecommerce.data.models.filters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FilterDataContainer {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private FilterData data;

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

    public FilterData getData() {
        return data;
    }

    public void setData(FilterData data) {
        this.data = data;
    }

}