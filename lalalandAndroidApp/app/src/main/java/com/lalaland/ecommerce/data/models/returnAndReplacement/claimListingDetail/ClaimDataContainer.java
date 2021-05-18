package com.lalaland.ecommerce.data.models.returnAndReplacement.claimListingDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClaimDataContainer {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private ClaimData data;

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

    public ClaimData getData() {
        return data;
    }

    public void setData(ClaimData data) {
        this.data = data;
    }

}