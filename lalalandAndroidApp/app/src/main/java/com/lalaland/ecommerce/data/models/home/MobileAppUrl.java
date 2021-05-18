package com.lalaland.ecommerce.data.models.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MobileAppUrl {

@SerializedName("id")
@Expose
private Integer id;
@SerializedName("action_name")
@Expose
private String actionName;
@SerializedName("title")
@Expose
private String title;

public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}

public String getActionName() {
return actionName;
}

public void setActionName(String actionName) {
this.actionName = actionName;
}

public String getTitle() {
return title;
}

public void setTitle(String title) {
this.title = title;
}

}