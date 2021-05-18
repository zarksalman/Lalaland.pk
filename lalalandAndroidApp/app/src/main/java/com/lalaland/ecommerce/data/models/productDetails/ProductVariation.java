package com.lalaland.ecommerce.data.models.productDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductVariation {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("attribute_name")
    @Expose
    private String attributeName;
    @SerializedName("attribute_value")
    @Expose
    private String attributeValue;
    @SerializedName("remaining_quantity")
    @Expose
    private String remainingQuantity;
    @SerializedName("sale_price")
    @Expose
    private String salePrice;
    @SerializedName("actual_price")
    @Expose
    private String actualPrice;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public String getRemainingQuantity() {
        return remainingQuantity;
    }

    public void setRemainingQuantity(String remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(String actualPrice) {
        this.actualPrice = actualPrice;
    }

}