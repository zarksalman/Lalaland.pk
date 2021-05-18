package com.lalaland.ecommerce.data.models.logout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BasicResponseData {

@SerializedName("cart_count")
@Expose
private Integer cartCount;

public Integer getCartCount() {
return cartCount;
}

public void setCartCount(Integer cartCount) {
this.cartCount = cartCount;
}

}