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
    @SerializedName("fit_and_sizing")
    @Expose
    private List<FitAndSizing> fitAndSizing = null;

    public String getRecommendedCat() {
        return recommendedCat;
    }

    public void setRecommendedCat(String recommendedCat) {
        this.recommendedCat = recommendedCat;
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

    public List<FitAndSizing> getFitAndSizing() {
        return fitAndSizing;
    }

    public void setFitAndSizing(List<FitAndSizing> fitAndSizing) {
        this.fitAndSizing = fitAndSizing;
    }

    public CategoryName getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(CategoryName categoryName) {
        this.categoryName = categoryName;
    }
}