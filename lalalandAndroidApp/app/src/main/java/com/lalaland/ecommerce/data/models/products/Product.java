package com.lalaland.ecommerce.data.models.products;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("brand_name")
    @Expose
    private String brandName;
    @SerializedName("is_wish_list_item")
    @Expose
    private Integer isWishListItem;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("primary_image")
    @Expose
    private String primaryImage;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("sale_price")
    @Expose
    private String salePrice;
    @SerializedName("actual_price")
    @Expose
    private String actualPrice;

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

    public Object getIsWishListItem() {
        return isWishListItem;
    }

    public void setIsWishListItem(Integer isWishListItem) {
        this.isWishListItem = isWishListItem;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrimaryImage() {
        return primaryImage;
    }

    public void setPrimaryImage(String primaryImage) {
        this.primaryImage = primaryImage;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(String actualPrice) {
        this.actualPrice = actualPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.brandName);
        dest.writeValue(this.isWishListItem);
        dest.writeString(this.name);
        dest.writeString(this.primaryImage);
        dest.writeString(this.quantity);
        dest.writeString(this.salePrice);
        dest.writeString(this.actualPrice);
    }

    public Product() {
    }

    protected Product(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.brandName = in.readString();
        this.isWishListItem = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.primaryImage = in.readString();
        this.quantity = in.readString();
        this.salePrice = in.readString();
        this.actualPrice = in.readString();
    }

    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}