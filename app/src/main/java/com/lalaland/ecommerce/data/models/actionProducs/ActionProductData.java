package com.lalaland.ecommerce.data.models.actionProducs;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActionProductData {

    @SerializedName("products")
    @Expose
    private List<ActionProducts> products = null;

    public List<ActionProducts> getProducts() {
        return products;
    }

    public void setProducts(List<ActionProducts> products) {
        this.products = products;
    }

}