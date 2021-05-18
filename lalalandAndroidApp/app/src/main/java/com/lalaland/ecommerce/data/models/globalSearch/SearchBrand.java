package com.lalaland.ecommerce.data.models.globalSearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchBrand {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("brand_url_name")
    @Expose
    private String brandUrlName;
    @SerializedName("name")
    @Expose
    private String name;

   /* @SerializedName("total_products")
    @Expose
    private Integer totalProducts;*/

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBrandUrlName() {
        return brandUrlName;
    }

    public void setBrandUrlName(String brandUrlName) {
        this.brandUrlName = brandUrlName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

/*    public Integer getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(Integer totalProducts) {
        this.totalProducts = totalProducts;
    }*/

}