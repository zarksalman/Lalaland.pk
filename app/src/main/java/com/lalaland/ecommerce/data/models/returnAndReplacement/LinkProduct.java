package com.lalaland.ecommerce.data.models.returnAndReplacement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LinkProduct {

    @SerializedName("attribute_value_primary_key")
    @Expose
    private Integer colorKey;
    @SerializedName("attribute_value_name")
    @Expose
    private String colorName;
    @SerializedName("product_id")
    @Expose
    private Integer productId;

    public Integer getColorKey() {
        return colorKey;
    }

    public void setColorKey(Integer attributeValuePrimaryKey) {
        this.colorKey = attributeValuePrimaryKey;
    }

    public String getColorName() {
        return colorName;
    }

    public void setSizeName(String attributeValueName) {
        this.colorName = attributeValueName;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }
}