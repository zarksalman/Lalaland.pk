package com.lalaland.ecommerce.data.models.filters;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Filter implements Parcelable {

    private boolean isSelected;

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("filter_name")
    @Expose
    private String filterName;
    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("filter_value")
    @Expose
    private String filterValue;
    @SerializedName("product_variation_value_id")
    @Expose
    private Integer productVariationValueId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFilterValue() {
        return filterValue;
    }

    public void setFilterValue(String filterValue) {
        this.filterValue = filterValue;
    }

    public Integer getProductVariationValueId() {
        return productVariationValueId;
    }

    public void setProductVariationValueId(Integer productVariationValueId) {
        this.productVariationValueId = productVariationValueId;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public Filter() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
        dest.writeValue(this.id);
        dest.writeString(this.filterName);
        dest.writeString(this.displayName);
        dest.writeString(this.filterValue);
        dest.writeValue(this.productVariationValueId);
    }

    protected Filter(Parcel in) {
        this.isSelected = in.readByte() != 0;
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.filterName = in.readString();
        this.displayName = in.readString();
        this.filterValue = in.readString();
        this.productVariationValueId = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<Filter> CREATOR = new Parcelable.Creator<Filter>() {
        @Override
        public Filter createFromParcel(Parcel source) {
            return new Filter(source);
        }

        @Override
        public Filter[] newArray(int size) {
            return new Filter[size];
        }
    };
}