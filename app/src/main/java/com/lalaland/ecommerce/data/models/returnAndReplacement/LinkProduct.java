package com.lalaland.ecommerce.data.models.returnAndReplacement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LinkProduct {

@SerializedName("attribute_value_primary_key")
@Expose
private Integer attributeValuePrimaryKey;
@SerializedName("attribute_value_name")
@Expose
private String attributeValueName;
@SerializedName("product_id")
@Expose
private Integer productId;

public Integer getAttributeValuePrimaryKey() {
return attributeValuePrimaryKey;
}

public void setAttributeValuePrimaryKey(Integer attributeValuePrimaryKey) {
this.attributeValuePrimaryKey = attributeValuePrimaryKey;
}

public String getAttributeValueName() {
return attributeValueName;
}

public void setAttributeValueName(String attributeValueName) {
this.attributeValueName = attributeValueName;
}

public Integer getProductId() {
return productId;
}

public void setProductId(Integer productId) {
this.productId = productId;
}

}