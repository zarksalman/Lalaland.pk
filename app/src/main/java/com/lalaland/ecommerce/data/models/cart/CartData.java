package com.lalaland.ecommerce.data.models.cart;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lalaland.ecommerce.data.models.userDetails.UserAddresses;

public class CartData {

@SerializedName("cart_items")
@Expose
private List<CartItem> cartItems = null;
@SerializedName("userAddresses")
@Expose
private UserAddresses userAddresses;

public List<CartItem> getCartItems() {
return cartItems;
}

public void setCartItems(List<CartItem> cartItems) {
this.cartItems = cartItems;
}

public UserAddresses getUserAddresses() {
return userAddresses;
}

public void setUserAddresses(UserAddresses userAddresses) {
this.userAddresses = userAddresses;
}

}