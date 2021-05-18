package com.lalaland.ecommerce.data.models.uploadProfileImage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadProfileData {

@SerializedName("avatar")
@Expose
private String avatar;

public String getAvatar() {
return avatar;
}

public void setAvatar(String avatar) {
this.avatar = avatar;
}

}