package com.lalaland.ecommerce.data.models.globalSearch;

import java.util.List;

public class SearchParentCategory {

    String parentName;
    List<SearchCategory> searchCategories;

    public List<SearchCategory> getSearchCategories() {
        return searchCategories;
    }

    public void setSearchCategories(List<SearchCategory> searchCategories) {
        this.searchCategories = searchCategories;
    }

    public SearchParentCategory() {
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
}
