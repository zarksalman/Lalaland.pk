package com.lalaland.ecommerce.data.models.home;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
    @SerializedName("recommendation")
    @Expose
    private List<Recommendation> recommendation = null;

    public List<HomeBanner> getHomeBanners() {
        return homeBanners;
    }

    public void setHomeBanners(List<HomeBanner> homeBanners) {
        this.homeBanners = homeBanners;
    }

    public List<Actions> getactions() {
        return actions;
    }

    public void setactions(List<Actions> actions) {
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

    public List<Recommendation> getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(List<Recommendation> recommendation) {
        this.recommendation = recommendation;
    }

}