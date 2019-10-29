package com.lalaland.ecommerce.data.models.reviews;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lalaland.ecommerce.data.models.productDetails.ProductReview;

import java.util.List;

public class ProductReviewsData {

    @SerializedName("product_reviews")
    @Expose
    private List<ProductReview> productReviews = null;
    @SerializedName("reviews_count")
    @Expose
    private Integer reviewsCount;
    @SerializedName("rating_average")
    @Expose
    private Double ratingAverage;

    public List<ProductReview> getProductReviews() {
        return productReviews;
    }

    public void setProductReviews(List<ProductReview> productReviews) {
        this.productReviews = productReviews;
    }

    public Integer getReviewsCount() {
        return reviewsCount;
    }

    public void setReviewsCount(Integer reviewsCount) {
        this.reviewsCount = reviewsCount;
    }

    public Double getRatingAverage() {
        return ratingAverage;
    }

    public void setRatingAverage(Double ratingAverage) {
        this.ratingAverage = ratingAverage;
    }

}