package com.lalaland.ecommerce.data.models.userAddressBook;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddressDataContainer {
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private AddressData data;

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

    public AddressData getData() {
        return data;
    }

    public void setData(AddressData data) {
        this.data = data;
    }

}