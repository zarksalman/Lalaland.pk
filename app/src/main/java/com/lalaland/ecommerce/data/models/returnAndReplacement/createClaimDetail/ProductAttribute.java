package com.lalaland.ecommerce.data.models.returnAndReplacement.createClaimDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductAttribute {
    @SerializedName("attribute_value_id")
    @Expose
    private Integer sizeId;
    @SerializedName("attribute_value_name")
    @Expose
    private String sizeName;

    public Integer getSizeId() {
        return sizeId;
    }

    public void setSizeId(Integer attributeValueId) {
        this.sizeId = attributeValueId;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String attributeValueName) {
        this.sizeName = attributeValueName;
    }


}