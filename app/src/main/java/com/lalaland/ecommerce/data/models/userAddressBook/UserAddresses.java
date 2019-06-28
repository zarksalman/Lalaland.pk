package com.lalaland.ecommerce.data.models.userAddressBook;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserAddresses implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userNameAddress);
        dest.writeString(this.email);
        dest.writeValue(this.cityId);
        dest.writeString(this.cityName);
        dest.writeValue(this.addressId);
        dest.writeValue(this.isPrimary);
        dest.writeString(this.userName);
        dest.writeValue(this.postalCode);
        dest.writeString(this.shippingAddress);
        dest.writeString(this.phone);
    }

    public UserAddresses() {
    }

    protected UserAddresses(Parcel in) {
        this.userNameAddress = in.readString();
        this.email = in.readString();
        this.cityId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.cityName = in.readString();
        this.addressId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.isPrimary = (Integer) in.readValue(Integer.class.getClassLoader());
        this.userName = in.readString();
        this.postalCode = (Integer) in.readValue(Integer.class.getClassLoader());
        this.shippingAddress = in.readString();
        this.phone = in.readString();
    }

    public static final Parcelable.Creator<UserAddresses> CREATOR = new Parcelable.Creator<UserAddresses>() {
        @Override
        public UserAddresses createFromParcel(Parcel source) {
            return new UserAddresses(source);
        }

        @Override
        public UserAddresses[] newArray(int size) {
            return new UserAddresses[size];
        }
    };
}