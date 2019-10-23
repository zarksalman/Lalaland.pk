package com.lalaland.ecommerce.data.models.productDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductDetailData {

    @SerializedName("recommended_cat")
    @Expose
    private String recommendedCat;
    @SerializedName("categoryName")
    @Expose
    private CategoryName categoryName;
    @SerializedName("productDetails")
    @Expose
    private ProductDetails productDetails;
    @SerializedName("productVariations")
    @Expose
    private List<ProductVariation> productVariations = null;
    @SerializedName("product_multimedia")
    @Expose
    private List<ProductMultimedium> productMultimedia = null;
    @SerializedName("linked_products")
    @Expose
    private List<LinkedProduct> linkedProducts = null;
    @SerializedName("fit_and_sizing")
    @Expose
    private List<FitAndSizing> fitAndSizing = null;
    @SerializedName("size_chart")
    @Expose
    private String sizeChart;
    @SerializedName("product_reviews")
    @Expose
    private List<Object> productReviews = null;
    @SerializedName("reviews_count")
    @Expose
    private Integer reviewsCount;
    @SerializedName("rating_average")
    @Expose
    private Integer ratingAverage;

    public String getRecommendedCat() {
        return recommendedCat;
    }

    public void setRecommendedCat(String recommendedCat) {
        this.recommendedCat = recommendedCat;
    }

    public CategoryName getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(CategoryName categoryName) {
        this.categoryName = categoryName;
    }

    public ProductDetails getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(ProductDetails productDetails) {
        this.productDetails = productDetails;
    }

    public List<ProductVariation> getProductVariations() {
        return productVariations;
    }

    public void setProductVariations(List<ProductVariation> productVariations) {
        this.productVariations = productVariations;
    }

    public List<ProductMultimedium> getProductMultimedia() {
        return productMultimedia;
    }

    public void setProductMultimedia(List<ProductMultimedium> productMultimedia) {
        this.productMultimedia = productMultimedia;
    }

    public List<LinkedProduct> getLinkedProducts() {
        return linkedProducts;
    }

    public void setLinkedProducts(List<LinkedProduct> linkedProducts) {
        this.linkedProducts = linkedProducts;
    }

    public List<FitAndSizing> getFitAndSizing() {
        return fitAndSizing;
    }

    public void setFitAndSizing(List<FitAndSizing> fitAndSizing) {
        this.fitAndSizing = fitAndSizing;
    }

    public String getSizeChart() {
        return sizeChart;
    }

    public void setSizeChart(String sizeChart) {
        this.sizeChart = sizeChart;
    }

    public List<Object> getProductReviews() {
        return productReviews;
    }

    public void setProductReviews(List<Object> productReviews) {
        this.productReviews = productReviews;
    }

    public Integer getReviewsCount() {
        return reviewsCount;
    }

    public void setReviewsCount(Integer reviewsCount) {
        this.reviewsCount = reviewsCount;
    }

    public Integer getRatingAverage() {
        return ratingAverage;
    }

    public void setRatingAverage(Integer ratingAverage) {
        this.ratingAverage = ratingAverage;
    }
}