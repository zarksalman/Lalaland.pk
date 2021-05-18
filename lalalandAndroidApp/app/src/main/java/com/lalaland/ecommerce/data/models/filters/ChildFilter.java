package com.lalaland.ecommerce.data.models.filters;

public class ChildFilter {

    private Integer id;
    private String childFilterName;

    public ChildFilter() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChildFilterName() {
        return childFilterName;
    }

    public void setChildFilterName(String childFilterName) {
        this.childFilterName = childFilterName;
    }
}
