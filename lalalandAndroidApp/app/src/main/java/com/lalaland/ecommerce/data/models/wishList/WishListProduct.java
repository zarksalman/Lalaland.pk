package com.lalaland.ecommerce.data.models.wishList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WishListProduct {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("reference_no")
    @Expose
    private String referenceNo;
    @SerializedName("is_on_sale")
    @Expose
    private Object isOnSale;
    @SerializedName("product_variations")
    @Expose
    private String productVariations;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("length")
    @Expose
    private String length;
    @SerializedName("width")
    @Expose
    private String width;
    @SerializedName("height")
    @Expose
    private String height;
    @SerializedName("brand_id")
    @Expose
    private Integer brandId;
    @SerializedName("merchant_id")
    @Expose
    private Integer merchantId;
    @SerializedName("smallest_category_id")
    @Expose
    private Integer smallestCategoryId;
    @SerializedName("approved_by")
    @Expose
    private Integer approvedBy;
    @SerializedName("approval_status")
    @Expose
    private Integer approvalStatus;
    @SerializedName("is_visible")
    @Expose
    private Integer isVisible;
    @SerializedName("added_by")
    @Expose
    private Integer addedBy;
    @SerializedName("general_description")
    @Expose
    private String generalDescription;
    @SerializedName("material_description")
    @Expose
    private String materialDescription;
    @SerializedName("primary_image")
    @Expose
    private String primaryImage;
    @SerializedName("display_price")
    @Expose
    private String displayPrice;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;
    @SerializedName("remaining_stock")
    @Expose
    private String remainingStock;
    @SerializedName("min_sale_price")
    @Expose
    private String minSalePrice;
    @SerializedName("max_sale_price")
    @Expose
    private String maxSalePrice;
    @SerializedName("actual_price")
    @Expose
    private String actualPrice;
    @SerializedName("sale_price")
    @Expose
    private String salePrice;
    @SerializedName("product_rating")
    @Expose
    private Object productRating;
    @SerializedName("wishlist_id")
    @Expose
    private Integer wishlistId;
    @SerializedName("brand_name")
    @Expose
    private String brandName;

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

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public Object getIsOnSale() {
        return isOnSale;
    }

    public void setIsOnSale(Object isOnSale) {
        this.isOnSale = isOnSale;
    }

    public String getProductVariations() {
        return productVariations;
    }

    public void setProductVariations(String productVariations) {
        this.productVariations = productVariations;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getSmallestCategoryId() {
        return smallestCategoryId;
    }

    public void setSmallestCategoryId(Integer smallestCategoryId) {
        this.smallestCategoryId = smallestCategoryId;
    }

    public Integer getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Integer approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Integer getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(Integer approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public Integer getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(Integer isVisible) {
        this.isVisible = isVisible;
    }

    public Integer getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(Integer addedBy) {
        this.addedBy = addedBy;
    }

    public String getGeneralDescription() {
        return generalDescription;
    }

    public void setGeneralDescription(String generalDescription) {
        this.generalDescription = generalDescription;
    }

    public String getMaterialDescription() {
        return materialDescription;
    }

    public void setMaterialDescription(String materialDescription) {
        this.materialDescription = materialDescription;
    }

    public String getPrimaryImage() {
        return primaryImage;
    }

    public void setPrimaryImage(String primaryImage) {
        this.primaryImage = primaryImage;
    }

    public String getDisplayPrice() {
        return displayPrice;
    }

    public void setDisplayPrice(String displayPrice) {
        this.displayPrice = displayPrice;
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

    public Object getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getRemainingStock() {
        return remainingStock;
    }

    public void setRemainingStock(String remainingStock) {
        this.remainingStock = remainingStock;
    }

    public String getMinSalePrice() {
        return minSalePrice;
    }

    public void setMinSalePrice(String minSalePrice) {
        this.minSalePrice = minSalePrice;
    }

    public String getMaxSalePrice() {
        return maxSalePrice;
    }

    public void setMaxSalePrice(String maxSalePrice) {
        this.maxSalePrice = maxSalePrice;
    }

    public String getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(String actualPrice) {
        this.actualPrice = actualPrice;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public Object getProductRating() {
        return productRating;
    }

    public void setProductRating(Object productRating) {
        this.productRating = productRating;
    }

    public Integer getWishlistId() {
        return wishlistId;
    }

    public void setWishlistId(Integer wishlistId) {
        this.wishlistId = wishlistId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

}