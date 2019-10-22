package com.lalaland.ecommerce.data.models.returnAndReplacement.claimListing;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClaimListingData {

    @SerializedName("all_claims")
    @Expose
    private Integer allClaims;
    @SerializedName("all_returns")
    @Expose
    private Integer allReturns;
    @SerializedName("all_replacements")
    @Expose
    private Integer allReplacements;
    @SerializedName("claims")
    @Expose
    private List<Claim> claims = null;

    public Integer getAllClaims() {
        return allClaims;
    }

    public void setAllClaims(Integer allClaims) {
        this.allClaims = allClaims;
    }

    public Integer getAllReturns() {
        return allReturns;
    }

    public void setAllReturns(Integer allReturns) {
        this.allReturns = allReturns;
    }

    public Integer getAllReplacements() {
        return allReplacements;
    }

    public void setAllReplacements(Integer allReplacements) {
        this.allReplacements = allReplacements;
    }

    public List<Claim> getClaims() {
        return claims;
    }

    public void setClaims(List<Claim> claims) {
        this.claims = claims;
    }

}