package com.lalaland.ecommerce.data.models.userAddressBook;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserAddresses {

    @SerializedName("user_name_address")
    @Expose
    private String userNameAddress;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("city_id")
    @Expose
    private Integer cityId;
    @SerializedName("city_name")
    @Expose
    private String cityName;
    @SerializedName("address_id")
    @Expose
    private Integer addressId;
    @SerializedName("is_primary")
    @Expose
    private Integer isPrimary;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("postal_code")
    @Expose
    private Integer postalCode;
    @SerializedName("shipping_address")
    @Expose
    private String shippingAddress;
    @SerializedName("phone")
    @Expose
    private String phone;

    public String getUserNameAddress() {
        return userNameAddress;
    }

    public void setUserNameAddress(String userNameAddress) {
        this.userNameAddress = userNameAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public Integer getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Integer isPrimary) {
        this.isPrimary = isPrimary;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}