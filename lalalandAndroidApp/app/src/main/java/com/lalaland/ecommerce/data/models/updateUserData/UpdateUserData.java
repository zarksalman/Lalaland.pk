package com.lalaland.ecommerce.data.models.updateUserData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lalaland.ecommerce.data.models.login.User;

public class UpdateUserData {

@SerializedName("user")
@Expose
private User user;

public User getUser() {
return user;
}

public void setUser(User user) {
this.user = user;
}

}