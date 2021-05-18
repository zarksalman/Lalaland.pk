package com.lalaland.ecommerce.data.models.reviews;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductReviewsDataContainer {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private ProductReviewsData data;

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

    public ProductReviewsData getData() {
        return data;
    }

    public void setData(ProductReviewsData data) {
        this.data = data;
    }

}