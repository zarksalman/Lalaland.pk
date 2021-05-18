package com.lalaland.ecommerce.data.models.productDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductMultimedium {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("source")
    @Expose
    private String source;

    private String mediaDescription;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getMediaDescription() {
        return mediaDescription;
    }

    public void setMediaDescription(String mediaDescription) {
        this.mediaDescription = mediaDescription;
    }
}