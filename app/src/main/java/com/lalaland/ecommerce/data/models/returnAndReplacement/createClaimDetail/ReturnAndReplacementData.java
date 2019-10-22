package com.lalaland.ecommerce.data.models.returnAndReplacement.createClaimDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lalaland.ecommerce.data.models.order.details.OrderProduct;

import java.util.List;

public class ReturnAndReplacementData {

    @SerializedName("order_product")
    @Expose
    private OrderProduct orderProduct;
    @SerializedName("product_attributes")
    @Expose
    private List<ProductAttribute> productAttributes = null;
    @SerializedName("link_products")
    @Expose
    private List<LinkProduct> linkProducts = null;
    @SerializedName("ordered_attribute_value_id")
    @Expose
    private Integer orderedColor;
    @SerializedName("ordered_attribute_value_primary_key")
    @Expose
    private Integer orderedSize;
    @SerializedName("return_reasons")
    @Expose
    private List<ReturnReason> returnReasons = null;
    @SerializedName("replacement_reasons")
    @Expose
    private List<ReplacementReason> replacementReasons = null;

    public OrderProduct getOrderProduct() {
        return orderProduct;
    }

    public void setOrderProduct(OrderProduct orderProduct) {
        this.orderProduct = orderProduct;
    }

    public List<ProductAttribute> getProductAttributes() {
        return productAttributes;
    }

    public void setProductAttributes(List<ProductAttribute> productAttributes) {
        this.productAttributes = productAttributes;
    }

    public List<LinkProduct> getLinkProducts() {
        return linkProducts;
    }

    public void setLinkProducts(List<LinkProduct> linkProducts) {
        this.linkProducts = linkProducts;
    }

    public Integer getOrderedAttributeValueId() {
        return orderedColor;
    }

    public void setOrderedAttributeValueId(Integer orderedAttributeValueId) {
        this.orderedColor = orderedAttributeValueId;
    }

    public Integer getOrderedAttributeValuePrimaryKey() {
        return orderedSize;
    }

    public void setOrderedAttributeValuePrimaryKey(Integer orderedAttributeValuePrimaryKey) {
        this.orderedSize = orderedAttributeValuePrimaryKey;
    }

    public List<ReturnReason> getReturnReasons() {
        return returnReasons;
    }

    public void setReturnReasons(List<ReturnReason> returnReasons) {
        this.returnReasons = returnReasons;
    }

    public List<ReplacementReason> getReplacementReasons() {
        return replacementReasons;
    }

    public void setReplacementReasons(List<ReplacementReason> replacementReasons) {
        this.replacementReasons = replacementReasons;
    }
}