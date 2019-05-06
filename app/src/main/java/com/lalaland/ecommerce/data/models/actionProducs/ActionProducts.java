package com.lalaland.ecommerce.data.models.actionProducs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActionProducts {

@SerializedName("id")
@Expose
private Integer id;
@SerializedName("brand_name")
@Expose
private String brandName;
@SerializedName("category_name")
@Expose
private Object categoryName;
@SerializedName("attribute_values")
@Expose
private String attributeValues;
@SerializedName("is_wish_list_item")
@Expose
private Object isWishListItem;
@SerializedName("name")
@Expose
private String name;
@SerializedName("primary_image")
@Expose
private String primaryImage;
@SerializedName("quantity")
@Expose
private String quantity;
@SerializedName("sale_price")
@Expose
private String salePrice;
@SerializedName("actual_price")
@Expose
private String actualPrice;
@SerializedName("sale_price_sum")
@Expose
private String salePriceSum;
@SerializedName("actual_price_sum")
@Expose
private String actualPriceSum;
@SerializedName("multimedia")
@Expose
private String multimedia;

public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}

public String getBrandName() {
return brandName;
}

public void setBrandName(String brandName) {
this.brandName = brandName;
}

public Object getCategoryName() {
return categoryName;
}

public void setCategoryName(Object categoryName) {
this.categoryName = categoryName;
}

public String getAttributeValues() {
return attributeValues;
}

public void setAttributeValues(String attributeValues) {
this.attributeValues = attributeValues;
}

public Object getIsWishListItem() {
return isWishListItem;
}

public void setIsWishListItem(Object isWishListItem) {
this.isWishListItem = isWishListItem;
}

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public String getPrimaryImage() {
return primaryImage;
}

public void setPrimaryImage(String primaryImage) {
this.primaryImage = primaryImage;
}

public String getQuantity() {
return quantity;
}

public void setQuantity(String quantity) {
this.quantity = quantity;
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

public String getSalePriceSum() {
return salePriceSum;
}

public void setSalePriceSum(String salePriceSum) {
this.salePriceSum = salePriceSum;
}

public String getActualPriceSum() {
return actualPriceSum;
}

public void setActualPriceSum(String actualPriceSum) {
this.actualPriceSum = actualPriceSum;
}

public String getMultimedia() {
return multimedia;
}

public void setMultimedia(String multimedia) {
this.multimedia = multimedia;
}

}