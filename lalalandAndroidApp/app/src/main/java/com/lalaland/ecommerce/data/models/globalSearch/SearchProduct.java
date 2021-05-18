package com.lalaland.ecommerce.data.models.globalSearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchProduct {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;

   /* @SerializedName("remaining_quantity")
    @Expose
    private String remainingQuantity;*/

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

/*    public String getRemainingQuantity() {
        return remainingQuantity;
    }

    public void setRemainingQuantity(String remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }*/

}