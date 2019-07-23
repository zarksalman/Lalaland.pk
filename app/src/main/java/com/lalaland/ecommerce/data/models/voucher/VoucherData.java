package com.lalaland.ecommerce.data.models.voucher;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VoucherData {

@SerializedName("discounted_total")
@Expose
private Integer discountedTotal;
@SerializedName("discount_amount")
@Expose
private String discountAmount;

public Integer getDiscountedTotal() {
return discountedTotal;
}

public void setDiscountedTotal(Integer discountedTotal) {
this.discountedTotal = discountedTotal;
}

public String getDiscountAmount() {
return discountAmount;
}

public void setDiscountAmount(String discountAmount) {
this.discountAmount = discountAmount;
}

}