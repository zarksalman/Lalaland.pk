package com.lalaland.ecommerce.data.models.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomeData {
    @SerializedName("homeBanners")
    @Expose
    private List<HomeBanner> homeBanners = null;
    @SerializedName("actions")
    @Expose
    private List<Actions> actions = null;
    @SerializedName("picks_of_the_week")
    @Expose
    private List<PicksOfTheWeek> picksOfTheWeek = null;
    @SerializedName("featured_brands")
    @Expose
    private List<FeaturedBrand> featuredBrands = null;
    @SerializedName("custom_products_five")
    @Expose
    private List<CustomProductsFive> customProductsFive = null;
    @SerializedName("featured_categories")
    @Expose
    private List<FeaturedCategory> featuredCategories = null;
    @SerializedName("advertisement")
    @Expose
    private Advertisement advertisement;
    @SerializedName("blog_posts")
    @Expose
    private List<BlogPost> blogPosts = null;
    @SerializedName("recommendation")
    @Expose
    private List<Recommendation> recommendation = null;
    @SerializedName("load_time")
    @Expose
    private Double loadTime;

    public List<HomeBanner> getHomeBanners() {
        return homeBanners;
    }

    public void setHomeBanners(List<HomeBanner> homeBanners) {
        this.homeBanners = homeBanners;
    }

    public List<Actions> getActions() {
        return actions;
    }

    public void setActions(List<Actions> actions) {
        this.actions = actions;
    }

    public List<PicksOfTheWeek> getPicksOfTheWeek() {
        return picksOfTheWeek;
    }

    public void setPicksOfTheWeek(List<PicksOfTheWeek> picksOfTheWeek) {
        this.picksOfTheWeek = picksOfTheWeek;
    }

    public List<FeaturedBrand> getFeaturedBrands() {
        return featuredBrands;
    }

    public void setFeaturedBrands(List<FeaturedBrand> featuredBrands) {
        this.featuredBrands = featuredBrands;
    }

    public List<CustomProductsFive> getCustomProductsFive() {
        return customProductsFive;
    }

    public void setCustomProductsFive(List<CustomProductsFive> customProductsFive) {
        this.customProductsFive = customProductsFive;
    }

    public List<FeaturedCategory> getFeaturedCategories() {
        return featuredCategories;
    }

    public void setFeaturedCategories(List<FeaturedCategory> featuredCategories) {
        this.featuredCategories = featuredCategories;
    }

    public Advertisement getAdvertisement() {
        return advertisement;
    }

    public void setAdvertisement(Advertisement advertisement) {
        this.advertisement = advertisement;
    }

    public List<BlogPost> getBlogPosts() {
        return blogPosts;
    }

    public void setBlogPosts(List<BlogPost> blogPosts) {
        this.blogPosts = blogPosts;
    }

    public List<Recommendation> getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(List<Recommendation> recommendation) {
        this.recommendation = recommendation;
    }

    public Double getLoadTime() {
        return loadTime;
    }

    public void setLoadTime(Double loadTime) {
        this.loadTime = loadTime;
    }


}