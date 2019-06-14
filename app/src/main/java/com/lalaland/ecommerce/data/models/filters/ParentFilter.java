package com.lalaland.ecommerce.data.models.filters;

public class ParentFilter {

    private Integer id;
    private String ParentFilterName;
    private String filterSelected;

    public String getFilterSelected() {
        return filterSelected;
    }

    public void setFilterSelected(String filterSelected) {
        this.filterSelected = filterSelected;
    }

    public ParentFilter() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getParentFilterName() {
        return ParentFilterName;
    }

    public void setParentFilterName(String parentFilterName) {
        ParentFilterName = parentFilterName;
    }
}
