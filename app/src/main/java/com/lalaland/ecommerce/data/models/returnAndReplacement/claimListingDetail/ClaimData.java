package com.lalaland.ecommerce.data.models.returnAndReplacement.claimListingDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClaimData {

    @SerializedName("claimDetails")
    @Expose
    private ClaimDetails claimDetails;
    @SerializedName("claimImages")
    @Expose
    private List<ClaimImage> claimImages = null;

    public ClaimDetails getClaimDetails() {
        return claimDetails;
    }

    public void setClaimDetails(ClaimDetails claimDetails) {
        this.claimDetails = claimDetails;
    }

    public List<ClaimImage> getClaimImages() {
        return claimImages;
    }

    public void setClaimImages(List<ClaimImage> claimImages) {
        this.claimImages = claimImages;
    }

}