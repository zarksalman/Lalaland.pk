package com.lalaland.ecommerce.data.models.filters;

import java.util.List;

public class PvFilterCustomModel {
    private Integer id;
    private List<Filter> filterList;

    public PvFilterCustomModel() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Filter> getFilterList() {
        return filterList;
    }

    public void setFilterList(List<Filter> filterList) {
        this.filterList = filterList;
    }
}
