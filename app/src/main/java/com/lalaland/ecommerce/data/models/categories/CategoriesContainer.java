package com.lalaland.ecommerce.data.models.categories;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoriesContainer {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private CategoriesData data;

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

    public CategoriesData getData() {
        return data;
    }

    public void setData(CategoriesData data) {
        this.data = data;
    }

}