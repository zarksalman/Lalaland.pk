package com.lalaland.ecommerce.data.models.category;
/*
 * Created by SalmanHameed on 12/28/2020.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PayProData {

    @SerializedName("key")
    @Expose
    private String key;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("banks")
    @Expose
    private List<String> bankList;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getBankList() {
        return bankList;
    }

    public void setBankList(List<String> bankList) {
        this.bankList = bankList;
    }
}