package com.lalaland.ecommerce.data.models.productDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductDetails {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("brand_name")
    @Expose
    private String brandName;
    @SerializedName("brand_id")
    @Expose
    private Integer brandId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("product_num")
    @Expose
    private String productNum;
    @SerializedName("primary_image")
    @Expose
    private String primaryImage;
    @SerializedName("merchant_id")
    @Expose
    private Integer merchantId;
    @SerializedName("merchant_name")
    @Expose
    private String merchantName;
    @SerializedName("general_description")
    @Expose
    private String generalDescription;
    @SerializedName("material_description")
    @Expose
    private String materialDescription;
    @SerializedName("is_wish_list_item")
    @Expose
    private Object isWishListItem;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductNum() {
        return productNum;
    }

    public void setProductNum(String productNum) {
        this.productNum = productNum;
    }

    public String getPrimaryImage() {
        return primaryImage;
    }

    public void setPrimaryImage(String primaryImage) {
        this.primaryImage = primaryImage;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
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

    public Object getIsWishListItem() {
        return isWishListItem;
    }

    public void setIsWishListItem(Object isWishListItem) {
        this.isWishListItem = isWishListItem;
    }

}