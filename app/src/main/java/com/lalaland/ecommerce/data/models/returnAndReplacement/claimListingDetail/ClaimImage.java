package com.lalaland.ecommerce.data.models.returnAndReplacement.claimListingDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClaimImage {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("claim_image")
    @Expose
    private String claimImage;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClaimImage() {
        return claimImage;
    }

    public void setClaimImage(String claimImage) {
        this.claimImage = claimImage;
    }

}