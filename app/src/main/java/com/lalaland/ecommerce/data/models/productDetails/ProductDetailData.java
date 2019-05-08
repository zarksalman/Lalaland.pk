package com.lalaland.ecommerce.data.models.productDetails;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductDetailData {

    @SerializedName("recommended_cat")
    @Expose
    private String recommendedCat;
    @SerializedName("productDetails")
    @Expose
    private ProductDetails productDetails;
    @SerializedName("productVariations")
    @Expose
    private List<ProductVariation> productVariations = null;
    @SerializedName("product_multimedia")
    @Expose
    private List<ProductMultimedium> productMultimedia = null;

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

}