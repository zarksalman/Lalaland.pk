package com.lalaland.ecommerce.data.models.category;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryBrand {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("products_in_brand")
    @Expose
    private Integer productsInBrand;

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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Integer getProductsInBrand() {
        return productsInBrand;
    }

    public void setProductsInBrand(Integer productsInBrand) {
        this.productsInBrand = productsInBrand;
    }

}
