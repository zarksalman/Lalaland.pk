package com.lalaland.ecommerce.data.models.returnAndReplacement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReturnReason {

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("show_color")
    @Expose
    private Boolean showColor;
    @SerializedName("show_size")
    @Expose
    private Boolean showSize;
    @SerializedName("claim_type")
    @Expose
    private String claimType;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getShowColor() {
        return showColor;
    }

    public void setShowColor(Boolean showColor) {
        this.showColor = showColor;
    }

    public Boolean getShowSize() {
        return showSize;
    }

    public void setShowSize(Boolean showSize) {
        this.showSize = showSize;
    }

    public String getClaimType() {
        return claimType;
    }

    public void setClaimType(String claimType) {
        this.claimType = claimType;
    }

}