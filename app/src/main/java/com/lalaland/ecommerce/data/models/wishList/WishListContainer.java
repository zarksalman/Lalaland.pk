package com.lalaland.ecommerce.data.models.wishList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WishListContainer {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private WishListData data;

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

    public WishListData getData() {
        return data;
    }

    public void setData(WishListData data) {
        this.data = data;
    }

}