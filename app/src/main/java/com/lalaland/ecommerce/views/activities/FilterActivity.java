package com.lalaland.ecommerce.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.adapters.ParentFilterAdapter;
import com.lalaland.ecommerce.data.models.filters.Brand;
import com.lalaland.ecommerce.data.models.filters.CategoryFilter;
import com.lalaland.ecommerce.data.models.filters.Filter;
import com.lalaland.ecommerce.data.models.filters.ParentFilter;
import com.lalaland.ecommerce.databinding.ActivityFilterBinding;
import com.lalaland.ecommerce.helpers.AppConstants;
import com.lalaland.ecommerce.helpers.AppUtils;
import com.lalaland.ecommerce.viewModels.filter.FilterViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lalaland.ecommerce.helpers.AppConstants.ACTION_ID;
import static com.lalaland.ecommerce.helpers.AppConstants.BRAND_FILTER;
import static com.lalaland.ecommerce.helpers.AppConstants.CATEGORY_FILTER;
import static com.lalaland.ecommerce.helpers.AppConstants.FILTER_ID;
import static com.lalaland.ecommerce.helpers.AppConstants.FILTER_KEY;
import static com.lalaland.ecommerce.helpers.AppConstants.FILTER_NAME;
import static com.lalaland.ecommerce.helpers.AppConstants.GENERAL_ERROR;
import static com.lalaland.ecommerce.helpers.AppConstants.PRICE_FILTER;
import static com.lalaland.ecommerce.helpers.AppConstants.PRICE_RANGE;
import static com.lalaland.ecommerce.helpers.AppConstants.PV_FILTER_;
import static com.lalaland.ecommerce.helpers.AppConstants.SELECTED_FILTER_NAME;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;
import static com.lalaland.ecommerce.helpers.AppConstants.appliedFilter;

public class FilterActivity extends AppCompatActivity {

    List<Filter> filterList = new ArrayList<>();
    List<CategoryFilter> categoryFilterList = new ArrayList<>();
    List<Brand> brandList = new ArrayList<>();
    List<ParentFilter> parentFilterList = new ArrayList<>();
    ParentFilterAdapter parentFilterAdapter;

    private ActivityFilterBinding activityFilterBinding;
    private String actionId, key;
    private FilterViewModel filterViewModel;
    private Map<String, String> parameter = new HashMap<>();
    Intent intent;
    Intent resultantIntent;
    static Intent filtersIntent;

    String priceFilter;
    String priceRange;

    Integer filterId;
    String filterName;
    String categoryFilterName;
    String brandFilterName;
    String pvFilters;
    StringBuilder pvFilterIds = new StringBuilder();
    private boolean isFiltersReset = false;
    List<Filter> subFilterList;
    Filter mSubFlter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityFilterBinding = DataBindingUtil.setContentView(this, R.layout.activity_filter);

        actionId = getIntent().getStringExtra(ACTION_ID);
        key = getIntent().getStringExtra(FILTER_KEY);

        parameter.put(FILTER_ID, actionId);
        parameter.put(FILTER_KEY, key);

        intent = new Intent(this, SubFiltersActivity.class);
        resultantIntent = new Intent();
        filterViewModel = ViewModelProviders.of(this).get(FilterViewModel.class);

        initResultantIntent();
        getFilters();

        activityFilterBinding.tvApply.setOnClickListener(v -> {
            if (appliedFilter.size() > 0)
                setResultantIntent();
        });

        activityFilterBinding.tvResetFilter.setOnClickListener(v -> {

            if (!isFiltersReset) {
                parentFilterList.clear();
                parameter = new HashMap<>();
                parameter.put(FILTER_ID, actionId);
                parameter.put(FILTER_KEY, key);
                AppConstants.appliedFilter.clear();

                isFiltersReset = true;
                initResultantIntent();
                getFilters();
            }
        });

        activityFilterBinding.ivBtnBack.setOnClickListener(v -> onBackPressed());
    }

    private void initResultantIntent() {

        resultantIntent.putExtra(PRICE_FILTER, "");
        resultantIntent.putExtra(BRAND_FILTER, "");
        resultantIntent.putExtra(CATEGORY_FILTER, "");
        resultantIntent.putExtra(PV_FILTER_, "");

        filtersIntent = new Intent();
    }

    private void getFilters() {

        filterViewModel.getCategories(parameter).observe(this, filterDataContainer -> {

            if (filterDataContainer != null) {

                if (filterDataContainer.getCode().equals(SUCCESS_CODE)) {

                    if (filterDataContainer.getData().getCategoryFilters() != null)
                        categoryFilterList = filterDataContainer.getData().getCategoryFilters();

                    if (filterDataContainer.getData().getBrands() != null)
                        brandList = filterDataContainer.getData().getBrands();

                    if (filterDataContainer.getData().getCategoryFilters() != null)
                        filterList = filterDataContainer.getData().getFilters();

                    setParentFilter();
                    setParentAdapter();
                } else {
                    Toast.makeText(this, filterDataContainer.getMsg(), Toast.LENGTH_SHORT).show();
                }
            } else
                Toast.makeText(this, GENERAL_ERROR, Toast.LENGTH_SHORT).show();

            activityFilterBinding.pbLoading.setVisibility(View.GONE);

        });
    }

    private void setParentAdapter() {


        parentFilterAdapter = new ParentFilterAdapter(this, filter -> {

            filterName = filter.getParentFilterName();
            intent.putExtra(FILTER_NAME, filterName);

            switch (filterName) {

                case "Price":
                    startActivityForResult(intent, 200);
                    break;

                case "Category":

                    getSubFilters(filterName, 201);
                    break;

                case "Brands":
                    getSubFilters(filterName, 202);
                    break;

                default:
                    getSubFilters(filterName, 203);
            }
        });

        activityFilterBinding.rvFilters.setLayoutManager(new LinearLayoutManager(this));
        activityFilterBinding.rvFilters.setHasFixedSize(true);
        activityFilterBinding.rvFilters.setAdapter(parentFilterAdapter);
        parentFilterAdapter.setData(parentFilterList);


    }


    private void getSubFilters(String filterName, int requestCode) {

        subFilterList = new ArrayList<>();
        mSubFlter = new Filter();

        Filter mFilter;

        if (filterName.equals("Category")) {

            mFilter = new Filter();
            mFilter.setId(0);
            mFilter.setDisplayName("All");
            subFilterList.add(mFilter);

            for (CategoryFilter category : categoryFilterList) {

                mSubFlter = new Filter();
                mSubFlter.setDisplayName(category.getName());
                mSubFlter.setId(category.getId());
                subFilterList.add(mSubFlter);
            }

            /*if (appliedFilter.containsKey(1)) {

                String[] categoryFilters = appliedFilter.get(1).split(",");

                for (int i = 0; i < categoryFilters.length; i++) {
                    for (int j = 0; j < subFilterList.size(); j++) {

                        if (categoryFilters[i].equals(subFilterList.get(j).getDisplayName())) {
                            subFilterList.get(j).setSelected(true);
                        }


                    }
                }
            }*/

        } else if (filterName.equals("Brands")) {

            mFilter = new Filter();
            mFilter.setId(0);
            mFilter.setDisplayName("Any");
            subFilterList.add(mFilter);

            for (Brand brand : brandList) {

                mSubFlter = new Filter();
                mSubFlter.setDisplayName(brand.getName());
                mSubFlter.setId(brand.getId());
                subFilterList.add(mSubFlter);
            }
        } else {

            if (filterName.equals(filterName)) {

                mFilter = new Filter();
                mFilter.setId(0);
                mFilter.setDisplayName("Any");
                mFilter.setFilterName(filterName);
                mFilter.setFilterValue("#00000000");
                mFilter.setProductVariationValueId(1);
                subFilterList.add(mFilter);
            }

            for (Filter filter : filterList) {

                if (filterName.equals(filter.getFilterName())) {
                    subFilterList.add(filter);
                }
            }
        }

        intent.putParcelableArrayListExtra("sub_filters", (ArrayList<? extends Parcelable>) subFilterList);
        intent.putExtra(FILTER_NAME, filterName);
        startActivityForResult(intent, requestCode);
    }

    private void setParentFilter() {

        ParentFilter parentFilter = new ParentFilter();
        int iterator = 0;


        if (appliedFilter.containsKey(iterator)) {

            parentFilter.setId(iterator);
            parentFilter.setParentFilterName("Price");
            parentFilter.setFilterSelected(appliedFilter.get(iterator));
            parentFilterList.add(parentFilter);
        } else {

            parentFilter.setId(iterator);
            parentFilter.setParentFilterName("Price");
            parentFilter.setFilterSelected("Any");
            parentFilterList.add(parentFilter);
        }

        iterator++;

        if (categoryFilterList.size() > 0) {

            parentFilter = new ParentFilter();

            if (appliedFilter.containsKey(iterator)) {

                parentFilter.setId(iterator);
                parentFilter.setParentFilterName("Category");
                parentFilter.setFilterSelected(appliedFilter.get(iterator));
                parentFilterList.add(parentFilter);

            } else {
                parentFilter.setId(iterator);
                parentFilter.setParentFilterName("Category");
                parentFilter.setFilterSelected("All");
                parentFilterList.add(parentFilter);
            }

            iterator++;
        }

        if (brandList.size() > 0) {

            parentFilter = new ParentFilter();

            if (appliedFilter.containsKey(iterator)) {

                parentFilter.setId(iterator);
                parentFilter.setParentFilterName("Brands");
                parentFilter.setFilterSelected(appliedFilter.get(iterator));
                parentFilterList.add(parentFilter);

            } else {
                parentFilter.setId(iterator);
                parentFilter.setParentFilterName("Brands");
                parentFilter.setFilterSelected("Any");
                parentFilterList.add(parentFilter);
            }

            iterator++;
        }

        int filterId = -1;
        for (Filter filter : filterList) {

            if (filterId != filter.getId()) {

                parentFilter = new ParentFilter();

                parentFilter.setId(filter.getId());
                parentFilter.setParentFilterName(filter.getFilterName());

                if (appliedFilter.containsKey(iterator))
                    parentFilter.setFilterSelected(appliedFilter.get(iterator));
                else
                    parentFilter.setFilterSelected("Any");

                parentFilterList.add(parentFilter);
                iterator++;
            }
            filterId = filter.getId();
        }

    }

    private Integer getParentFilterId(String parentFilterName) {

        for (int i = 0; i < parentFilterList.size(); i++) {
            if (parentFilterName.equals(parentFilterList.get(i).getParentFilterName()))
                return i;
        }

        return null;
    }

    private Integer getCategoryFilterId(String catName) {

        for (int i = 0; i < categoryFilterList.size(); i++) {
            if (categoryFilterList.get(i).getName().equals(catName))
                return categoryFilterList.get(i).getId();
        }

        return null;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        isFiltersReset = false;
        switch (requestCode) {
            case 200: // pricing

                if (data.getStringExtra(FILTER_NAME) != null) {

                    priceFilter = data.getStringExtra(PRICE_FILTER);
                    priceRange = data.getStringExtra(PRICE_RANGE);

                    if (!priceRange.equals("Any")) {
                        parentFilterList.get(0).setFilterSelected("PKR " + priceRange);
                        appliedFilter.put(0, priceRange);
                    }
                    parentFilterAdapter.notifyDataSetChanged();
                }

                resultantIntent.putExtra(PRICE_FILTER, priceFilter);
                data.putExtra(SELECTED_FILTER_NAME, filterName);

                resultantIntent.putExtra(SELECTED_FILTER_NAME, filterName);
                setResult(RESULT_OK, resultantIntent);

                break;

            case 201: // category


                if (data.getStringExtra(FILTER_NAME) != null) {
                    categoryFilterName = data.getStringExtra(FILTER_NAME);
                    parentFilterList.get(1).setFilterSelected(categoryFilterName);
                    appliedFilter.put(1, categoryFilterName);

                    parentFilterAdapter.notifyDataSetChanged();
                }

                resultantIntent.putExtra(SELECTED_FILTER_NAME, "Category");
                Integer categoryFilterId = getCategoryFilterId(categoryFilterName);

                if (categoryFilterId != null)
                    resultantIntent.putExtra(CATEGORY_FILTER, String.valueOf(categoryFilterId));

/*                data.putExtra(SELECTED_FILTER_ID, String.valueOf(getCategoryFilterId(categoryFilterName)));
                data.putExtra(SELECTED_FILTER_NAME, filterName);*/

                setResult(RESULT_OK, resultantIntent);

                break;

            case 202: // brands

                if (data.getStringExtra(SELECTED_FILTER_NAME) != null) {
                    brandFilterName = data.getStringExtra(SELECTED_FILTER_NAME);

                    if (categoryFilterList.size() > 0) {
                        parentFilterList.get(2).setFilterSelected(brandFilterName);
                        appliedFilter.put(2, brandFilterName);
                    } else {
                        parentFilterList.get(1).setFilterSelected(brandFilterName);
                        appliedFilter.put(1, brandFilterName);
                    }

                    parentFilterAdapter.notifyDataSetChanged();
                }

                //   getSelectedBrandIds(brandFilterName);
                resultantIntent.putExtra(SELECTED_FILTER_NAME, "Brands");
                resultantIntent.putExtra(BRAND_FILTER, data.getStringExtra(BRAND_FILTER));
                setResult(RESULT_OK, resultantIntent);

                break;

            case 203:

                if (data.getStringExtra(SELECTED_FILTER_NAME) != null) {

                    filterName = data.getStringExtra(FILTER_NAME);
                    pvFilters = data.getStringExtra(SELECTED_FILTER_NAME);

                    pvFilterIds.append(data.getStringExtra(PV_FILTER_));
                    pvFilterIds.append(",");

                    Integer index = getParentFilterId(filterName);
                    
                    if (index != null) {
                        parentFilterList.get(index).setFilterSelected(pvFilters);
                        appliedFilter.put(index, pvFilters);

                        parentFilterAdapter.notifyDataSetChanged();
                    }
                }

                resultantIntent.putExtra(SELECTED_FILTER_NAME, filterName);
                resultantIntent.putExtra(PV_FILTER_, pvFilterIds.toString());
                setResult(RESULT_OK, resultantIntent);
                break;
        }

        if (resultCode == RESULT_OK) {

            if (!pvFilterIds.toString().isEmpty()) {
                String pvFilters = AppUtils.trimLastComa(pvFilterIds.toString());
                resultantIntent.putExtra(PV_FILTER_, pvFilters);
            }
            finish();
        }

        setSubFiltersSelection();
    }

    private void setSubFiltersSelection() {

    }

    void setResultantIntent() {

        resultantIntent.putExtra(SELECTED_FILTER_NAME, "Multiple_Filters");

        if (!pvFilterIds.toString().isEmpty()) {
            String pvFilters = AppUtils.trimLastComa(pvFilterIds.toString());
            resultantIntent.putExtra(PV_FILTER_, pvFilters);
        }

        setResult(RESULT_OK, resultantIntent);
        finish();
    }

    @Override
    public void onBackPressed() {

        if (isFiltersReset) {
            resultantIntent = new Intent();
            resultantIntent.putExtra("reset_filters", "true");
        }

        setResult(RESULT_CANCELED, resultantIntent);
        resultantIntent = new Intent();
        finish();
    }
}
