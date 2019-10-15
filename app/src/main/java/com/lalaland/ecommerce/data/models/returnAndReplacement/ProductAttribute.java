package com.lalaland.ecommerce.data.models.returnAndReplacement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductAttribute {

@SerializedName("attribute_value_id")
@Expose
private Integer attributeValueId;
@SerializedName("attribute_value_name")
@Expose
private String attributeValueName;

public Integer getAttributeValueId() {
return attributeValueId;
}

public void setAttributeValueId(Integer attributeValueId) {
this.attributeValueId = attributeValueId;
}

public String getAttributeValueName() {
return attributeValueName;
}

public void setAttributeValueName(String attributeValueName) {
this.attributeValueName = attributeValueName;
}

}