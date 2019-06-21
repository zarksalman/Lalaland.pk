package com.lalaland.ecommerce.data.models.deliveryOption;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeliveryOptionData {

@SerializedName("to_return")
@Expose
private ToReturn toReturn;

public ToReturn getToReturn() {
return toReturn;
}

public void setToReturn(ToReturn toReturn) {
this.toReturn = toReturn;
}

}