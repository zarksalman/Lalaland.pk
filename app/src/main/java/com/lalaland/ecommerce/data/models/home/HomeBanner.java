package com.lalaland.ecommerce.data.models.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class HomeBanner {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("banner_image")
    @Expose
    private String bannerImage;
    @SerializedName("mobile_app_url")
    @Expose
    private MobileAppUrl mobileAppUrl;

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

    public MobileAppUrl getMobileAppUrl() {
        return mobileAppUrl;
    }

    public void setMobileAppUrl(MobileAppUrl mobileAppUrl) {
        this.mobileAppUrl = mobileAppUrl;
    }

}