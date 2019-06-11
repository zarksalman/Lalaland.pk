package com.lalaland.ecommerce.data.models.order.myOrders;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Order {

@SerializedName("order_id")
@Expose
private Integer orderId;
@SerializedName("delivery_city")
@Expose
private String deliveryCity;
@SerializedName("fancy_order_id")
@Expose
private String fancyOrderId;
@SerializedName("user_phone")
@Expose
private String userPhone;
@SerializedName("postal_code")
@Expose
private String postalCode;
@SerializedName("order_group_id")
@Expose
private Integer orderGroupId;
@SerializedName("user_name")
@Expose
private String userName;
@SerializedName("order_package_image")
@Expose
private Object orderPackageImage;
@SerializedName("delivery_address")
@Expose
private String deliveryAddress;
@SerializedName("payment_gateway_id")
@Expose
private String paymentGatewayId;
@SerializedName("merchant_name")
@Expose
private String merchantName;
@SerializedName("land_line_number")
@Expose
private Object landLineNumber;
@SerializedName("merchant_logo")
@Expose
private String merchantLogo;
@SerializedName("order_status")
@Expose
private String orderStatus;
@SerializedName("shipment_tracking_id")
@Expose
private Object shipmentTrackingId;
@SerializedName("grand_total")
@Expose
private String grandTotal;
@SerializedName("created_at")
@Expose
private String createdAt;

public Integer getOrderId() {
return orderId;
}

public void setOrderId(Integer orderId) {
this.orderId = orderId;
}

public String getDeliveryCity() {
return deliveryCity;
}

public void setDeliveryCity(String deliveryCity) {
this.deliveryCity = deliveryCity;
}

public String getFancyOrderId() {
return fancyOrderId;
}

public void setFancyOrderId(String fancyOrderId) {
this.fancyOrderId = fancyOrderId;
}

public String getUserPhone() {
return userPhone;
}

public void setUserPhone(String userPhone) {
this.userPhone = userPhone;
}

public String getPostalCode() {
return postalCode;
}

public void setPostalCode(String postalCode) {
this.postalCode = postalCode;
}

public Integer getOrderGroupId() {
return orderGroupId;
}

public void setOrderGroupId(Integer orderGroupId) {
this.orderGroupId = orderGroupId;
}

public String getUserName() {
return userName;
}

public void setUserName(String userName) {
this.userName = userName;
}

public Object getOrderPackageImage() {
return orderPackageImage;
}

public void setOrderPackageImage(Object orderPackageImage) {
this.orderPackageImage = orderPackageImage;
}

public String getDeliveryAddress() {
return deliveryAddress;
}

public void setDeliveryAddress(String deliveryAddress) {
this.deliveryAddress = deliveryAddress;
}

public String getPaymentGatewayId() {
return paymentGatewayId;
}

public void setPaymentGatewayId(String paymentGatewayId) {
this.paymentGatewayId = paymentGatewayId;
}

public String getMerchantName() {
return merchantName;
}

public void setMerchantName(String merchantName) {
this.merchantName = merchantName;
}

public Object getLandLineNumber() {
return landLineNumber;
}

public void setLandLineNumber(Object landLineNumber) {
this.landLineNumber = landLineNumber;
}

public String getMerchantLogo() {
return merchantLogo;
}

public void setMerchantLogo(String merchantLogo) {
this.merchantLogo = merchantLogo;
}

public String getOrderStatus() {
return orderStatus;
}

public void setOrderStatus(String orderStatus) {
this.orderStatus = orderStatus;
}

public Object getShipmentTrackingId() {
return shipmentTrackingId;
}

public void setShipmentTrackingId(Object shipmentTrackingId) {
this.shipmentTrackingId = shipmentTrackingId;
}

public String getGrandTotal() {
return grandTotal;
}

public void setGrandTotal(String grandTotal) {
this.grandTotal = grandTotal;
}

public String getCreatedAt() {
return createdAt;
}

public void setCreatedAt(String createdAt) {
this.createdAt = createdAt;
}

}
