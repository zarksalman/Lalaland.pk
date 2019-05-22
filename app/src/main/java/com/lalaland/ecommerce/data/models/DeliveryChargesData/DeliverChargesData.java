package com.lalaland.ecommerce.data.models.DeliveryChargesData;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeliverChargesData {

@SerializedName("delivery_charges_of_merchant_items")
@Expose
private List<DeliveryChargesOfMerchantItem> deliveryChargesOfMerchantItems = null;

public List<DeliveryChargesOfMerchantItem> getDeliveryChargesOfMerchantItems() {
return deliveryChargesOfMerchantItems;
}

public void setDeliveryChargesOfMerchantItems(List<DeliveryChargesOfMerchantItem> deliveryChargesOfMerchantItems) {
this.deliveryChargesOfMerchantItems = deliveryChargesOfMerchantItems;
}

}