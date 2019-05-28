package com.lalaland.ecommerce.data.models.categories;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubCategory {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("inner_categories")
    @Expose
    private List<InnerCategory> innerCategories = null;

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

    public List<InnerCategory> getInnerCategories() {
        return innerCategories;
    }

    public void setInnerCategories(List<InnerCategory> innerCategories) {
        this.innerCategories = innerCategories;
    }

}