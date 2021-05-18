package com.lalaland.ecommerce.data.models.voucher;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VoucherDataContainer {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private VoucherData data;

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

    public VoucherData getData() {
        return data;
    }

    public void setData(VoucherData data) {
        this.data = data;
    }

}