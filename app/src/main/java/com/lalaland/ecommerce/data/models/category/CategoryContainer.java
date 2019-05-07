package com.lalaland.ecommerce.data.models.category;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryContainer {

@SerializedName("code")
@Expose
private String code;
@SerializedName("msg")
@Expose
private String msg;
@SerializedName("data")
@Expose
private CategoryData data;

public String getCode() {
return code;
}

public void setCode(String code) {
this.code = code;
}

public String getMsg() {
return msg;
}

public void setMsg(String msg) {
this.msg = msg;
}

public CategoryData getData() {
return data;
}

public void setData(CategoryData data) {
this.data = data;
}

}