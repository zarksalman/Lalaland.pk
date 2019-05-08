package com.lalaland.ecommerce.data.models.cart;

import android.os.Parcel;
import android.os.Parcelable;

public class CartProduct implements Parcelable {

    private String ProductName;
    private String BrandName;
    private String primaryImage;
    private String productSize;
    private String ProductColor;
    private Integer quantity;
    private Integer productId;

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public void setBrandName(String brandName) {
        BrandName = brandName;
    }

    public void setPrimaryImage(String primaryImage) {
        this.primaryImage = primaryImage;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public void setProductColor(String productColor) {
        ProductColor = productColor;
    }

    public String getProductName() {
        return ProductName;
    }

    public String getBrandName() {
        return BrandName;
    }

    public String getPrimaryImage() {
        return primaryImage;
    }

    public String getProductSize() {
        return productSize;
    }

    public String getProductColor() {
        return ProductColor;
    }

    public CartProduct() {
    }

    public CartProduct(String productName, String brandName, String primaryImage, String productSize, String productColor, Integer quantity, Integer productId) {
        ProductName = productName;
        BrandName = brandName;
        this.primaryImage = primaryImage;
        this.productSize = productSize;
        ProductColor = productColor;
        this.quantity = quantity;
        this.productId = productId;
    }


    public Integer getQuantity() {
        return quantity;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ProductName);
        dest.writeString(this.BrandName);
        dest.writeString(this.primaryImage);
        dest.writeString(this.productSize);
        dest.writeString(this.ProductColor);
        dest.writeValue(this.quantity);
        dest.writeValue(this.productId);
    }

    protected CartProduct(Parcel in) {
        this.ProductName = in.readString();
        this.BrandName = in.readString();
        this.primaryImage = in.readString();
        this.productSize = in.readString();
        this.ProductColor = in.readString();
        this.quantity = (Integer) in.readValue(Integer.class.getClassLoader());
        this.productId = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<CartProduct> CREATOR = new Parcelable.Creator<CartProduct>() {
        @Override
        public CartProduct createFromParcel(Parcel source) {
            return new CartProduct(source);
        }

        @Override
        public CartProduct[] newArray(int size) {
            return new CartProduct[size];
        }
    };
}
