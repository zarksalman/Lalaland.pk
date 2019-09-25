package com.lalaland.ecommerce.data.models.otp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Otp {

@SerializedName("user_otp_id")
@Expose
private Integer userOtpId;

public Integer getUserOtpId() {
return userOtpId;
}

public void setUserOtpId(Integer userOtpId) {
this.userOtpId = userOtpId;
}

}