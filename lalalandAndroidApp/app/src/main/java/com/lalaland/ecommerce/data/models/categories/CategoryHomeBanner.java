package com.lalaland.ecommerce.data.models.categories;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryHomeBanner {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("banner_id")
    @Expose
    private Integer bannerId;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("deleted_at")
    @Expose
    private String deletedAt;
    @SerializedName("is_home_banner")
    @Expose
    private Integer isHomeBanner;
    @SerializedName("banner_image")
    @Expose
    private String bannerImage;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("banner_sequence")
    @Expose
    private Integer bannerSequence;
    @SerializedName("url_text")
    @Expose
    private String urlText;
    @SerializedName("url")
    @Expose
    private String url;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getBannerId() {
        return bannerId;
    }

    public void setBannerId(Integer bannerId) {
        this.bannerId = bannerId;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Integer getIsHomeBanner() {
        return isHomeBanner;
    }

    public void setIsHomeBanner(Integer isHomeBanner) {
        this.isHomeBanner = isHomeBanner;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getBannerSequence() {
        return bannerSequence;
    }

    public void setBannerSequence(Integer bannerSequence) {
        this.bannerSequence = bannerSequence;
    }

    public String getUrlText() {
        return urlText;
    }

    public void setUrlText(String urlText) {
        this.urlText = urlText;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}