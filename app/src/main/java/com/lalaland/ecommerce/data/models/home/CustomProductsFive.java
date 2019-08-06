package com.lalaland.ecommerce.data.models.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomProductsFive {

@SerializedName("action_id")
@Expose
private Integer actionId;
@SerializedName("name")
@Expose
private String name;
@SerializedName("image")
@Expose
private String image;
@SerializedName("action_name")
@Expose
private String actionName;

public Integer getActionId() {
return actionId;
}

public void setActionId(Integer actionId) {
this.actionId = actionId;
}

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public String getImage() {
return image;
}

public void setImage(String image) {
this.image = image;
}

public String getActionName() {
return actionName;
}

public void setActionName(String actionName) {
this.actionName = actionName;
}

}