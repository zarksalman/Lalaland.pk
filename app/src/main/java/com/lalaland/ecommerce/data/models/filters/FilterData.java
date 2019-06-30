package com.lalaland.ecommerce.data.models.filters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FilterData {

@SerializedName("category_filters")
@Expose
private List<CategoryFilter> categoryFilters = null;
@SerializedName("brands")
@Expose
private List<Brand> brands = null;
@SerializedName("filters")
@Expose
private List<Filter> filters = null;

public List<CategoryFilter> getCategoryFilters() {
return categoryFilters;
}

public void setCategoryFilters(List<CategoryFilter> categoryFilters) {
this.categoryFilters = categoryFilters;
}

public List<Brand> getBrands() {
return brands;
}

public void setBrands(List<Brand> brands) {
this.brands = brands;
}

public List<Filter> getFilters() {
return filters;
}

public void setFilters(List<Filter> filters) {
this.filters = filters;
}

}