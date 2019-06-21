package com.lalaland.ecommerce.data.models.deliveryOption;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ToReturn {

    @SerializedName("amount")
    @Expose
    private Integer amount;

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

}