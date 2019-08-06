package com.lalaland.ecommerce.data.models.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BlogPost {

@SerializedName("thumbnail")
@Expose
private String thumbnail;
@SerializedName("post_name")
@Expose
private String postName;
@SerializedName("id")
@Expose
private Integer id;

public String getThumbnail() {
return thumbnail;
}

public void setThumbnail(String thumbnail) {
this.thumbnail = thumbnail;
}

public String getPostName() {
return postName;
}

public void setPostName(String postName) {
this.postName = postName;
}

public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}

}