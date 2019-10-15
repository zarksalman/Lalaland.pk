package com.lalaland.ecommerce.data.models.returnAndReplacement;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lalaland.ecommerce.data.models.order.details.OrderProduct;

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
private Integer orderedAttributeValueId;
@SerializedName("ordered_attribute_value_primary_key")
@Expose
private Integer orderedAttributeValuePrimaryKey;
@SerializedName("return_type_id")
@Expose
private String returnTypeId;
@SerializedName("return_reasons")
@Expose
private List<String> returnReasons = null;
@SerializedName("replacement_type_id")
@Expose
private String replacementTypeId;
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
return orderedAttributeValueId;
}

public void setOrderedAttributeValueId(Integer orderedAttributeValueId) {
this.orderedAttributeValueId = orderedAttributeValueId;
}

public Integer getOrderedAttributeValuePrimaryKey() {
return orderedAttributeValuePrimaryKey;
}

public void setOrderedAttributeValuePrimaryKey(Integer orderedAttributeValuePrimaryKey) {
this.orderedAttributeValuePrimaryKey = orderedAttributeValuePrimaryKey;
}

public String getReturnTypeId() {
return returnTypeId;
}

public void setReturnTypeId(String returnTypeId) {
this.returnTypeId = returnTypeId;
}

public List<String> getReturnReasons() {
return returnReasons;
}

public void setReturnReasons(List<String> returnReasons) {
this.returnReasons = returnReasons;
}

public String getReplacementTypeId() {
return replacementTypeId;
}

public void setReplacementTypeId(String replacementTypeId) {
this.replacementTypeId = replacementTypeId;
}

public List<ReplacementReason> getReplacementReasons() {
return replacementReasons;
}

public void setReplacementReasons(List<ReplacementReason> replacementReasons) {
this.replacementReasons = replacementReasons;
}

}