package com.lalaland.ecommerce.data.models.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HomeBanner {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("banner_image")
    @Expose
    private String bannerImage;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

}