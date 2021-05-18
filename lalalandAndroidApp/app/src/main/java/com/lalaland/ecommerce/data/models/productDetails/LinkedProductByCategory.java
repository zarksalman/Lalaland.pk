package com.lalaland.ecommerce.data.models.productDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LinkedProductByCategory {

    @SerializedName("product_id")
    @Expose
    private Integer productId;
    @SerializedName("short_name")
    @Expose
    private String shortName;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

}