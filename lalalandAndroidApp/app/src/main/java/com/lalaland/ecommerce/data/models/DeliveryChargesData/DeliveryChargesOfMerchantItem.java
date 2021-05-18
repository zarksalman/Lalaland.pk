package com.lalaland.ecommerce.data.models.DeliveryChargesData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeliveryChargesOfMerchantItem {

    @SerializedName("volumetric_weight")
    @Expose
    private String volumetricWeight;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("merchant_id")
    @Expose
    private Integer merchantId;
    @SerializedName("merchant_city_name")
    @Expose
    private String merchantCityName;
    @SerializedName("merchant_city_id")
    @Expose
    private Integer merchantCityId;
    @SerializedName("merchant_name")
    @Expose
    private String merchantName;
    @SerializedName("city_id")
    @Expose
    private Integer cityId;
    @SerializedName("cart_id")
    @Expose
    private Integer cartId;
    @SerializedName("item_quantity")
    @Expose
    private Integer itemQuantity;
    @SerializedName("shipping_rate")
    @Expose
    private Integer shippingRate;
    @SerializedName("final_weight")
    @Expose
    private Double finalWeight;

    public String getVolumetricWeight() {
        return volumetricWeight;
    }

    public void setVolumetricWeight(String volumetricWeight) {
        this.volumetricWeight = volumetricWeight;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantCityName() {
        return merchantCityName;
    }

    public void setMerchantCityName(String merchantCityName) {
        this.merchantCityName = merchantCityName;
    }

    public Integer getMerchantCityId() {
        return merchantCityId;
    }

    public void setMerchantCityId(Integer merchantCityId) {
        this.merchantCityId = merchantCityId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Integer getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(Integer itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public Integer getShippingRate() {
        return shippingRate;
    }

    public void setShippingRate(Integer shippingRate) {
        this.shippingRate = shippingRate;
    }

    public Double getFinalWeight() {
        return finalWeight;
    }

    public void setFinalWeight(Double finalWeight) {
        this.finalWeight = finalWeight;
    }

}