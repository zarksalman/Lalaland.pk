package com.lalaland.ecommerce.data.models.userAddressBook;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddressData {

    @SerializedName("userAddresses")
    @Expose
    private List<UserAddresses> userAddress = null;

    public List<UserAddresses> getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(List<UserAddresses> userAddress) {
        this.userAddress = userAddress;
    }

}