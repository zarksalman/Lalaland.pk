package com.lalaland.ecommerce.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

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
import com.lalaland.ecommerce.viewModels.filter.FilterViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lalaland.ecommerce.helpers.AppConstants.ACTION_ID;
import static com.lalaland.ecommerce.helpers.AppConstants.FILTER_ID;
import static com.lalaland.ecommerce.helpers.AppConstants.FILTER_KEY;
import static com.lalaland.ecommerce.helpers.AppConstants.FILTER_NAME;
import static com.lalaland.ecommerce.helpers.AppConstants.PRICE_FILTER;
import static com.lalaland.ecommerce.helpers.AppConstants.PRICE_RANGE;
import static com.lalaland.ecommerce.helpers.AppConstants.SELECTED_FILTER_ID;
import static com.lalaland.ecommerce.helpers.AppConstants.SELECTED_FILTER_NAME;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;

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

    String priceFilter;
    String priceRange;

    String filterName;
    String categoryFilterName;
    String brandFilterName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityFilterBinding = DataBindingUtil.setContentView(this, R.layout.activity_filter);

        actionId = getIntent().getStringExtra(ACTION_ID);
        key = getIntent().getStringExtra(FILTER_KEY);

        parameter.put(FILTER_ID, actionId);
        parameter.put(FILTER_KEY, key);

        intent = new Intent(this, SubFiltersActivity.class);
        filterViewModel = ViewModelProviders.of(this).get(FilterViewModel.class);
        getFilters();
    }

    private void getFilters() {

        filterViewModel.getCategories(parameter).observe(this, filterDataContainer -> {

            if (filterDataContainer != null) {

                if (filterDataContainer.getCode().equals(SUCCESS_CODE)) {

                    if (filterDataContainer.getData().getCategoryFilters() != null)
                        categoryFilterList.addAll(filterDataContainer.getData().getCategoryFilters());

                    if (filterDataContainer.getData().getBrands() != null)
                        brandList.addAll(filterDataContainer.getData().getBrands());

                    if (filterDataContainer.getData().getCategoryFilters() != null)
                        filterList.addAll(filterDataContainer.getData().getFilters());

                    setParentFilter();
                    setParentAdapter();
                }
            }
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

        List<Filter> subFilterList = new ArrayList<>();
        Filter mSubFlter = new Filter();

        if (filterName.equals("Category")) {

            for (CategoryFilter category : categoryFilterList) {

                mSubFlter = new Filter();
                mSubFlter.setDisplayName(category.getName());
                mSubFlter.setId(category.getId());
                subFilterList.add(mSubFlter);
            }
        } else if (filterName.equals("Brands")) {

            for (Brand brand : brandList) {

                mSubFlter = new Filter();
                mSubFlter.setDisplayName(brand.getName());
                mSubFlter.setId(brand.getId());
                subFilterList.add(mSubFlter);
            }
        } else {
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

        parentFilter.setId(iterator);
        parentFilter.setParentFilterName("Price");
        parentFilter.setFilterSelected("Any");
        parentFilterList.add(parentFilter);
        iterator++;

        if (categoryFilterList.size() > 0) {

            parentFilter = new ParentFilter();

            parentFilter.setId(iterator);
            parentFilter.setParentFilterName("Category");
            parentFilter.setFilterSelected("All");
            parentFilterList.add(parentFilter);
            iterator++;
        }

        if (brandList.size() > 0) {

            parentFilter = new ParentFilter();

            parentFilter.setId(iterator);
            parentFilter.setParentFilterName("Brands");
            parentFilter.setFilterSelected("Any");
            parentFilterList.add(parentFilter);
            iterator++;
        }

        int filterId = -1;
        for (Filter filter : filterList) {

            if (filterId != filter.getId()) {

                parentFilter = new ParentFilter();

                parentFilter.setId(filter.getId());
                parentFilter.setParentFilterName(filter.getFilterName());
                parentFilter.setFilterSelected("Any");
                parentFilterList.add(parentFilter);
                iterator++;
            }
            filterId = filter.getId();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case 200: // pricing

                    priceFilter = data.getStringExtra(PRICE_FILTER);
                    priceRange = data.getStringExtra(PRICE_RANGE);
                    parentFilterList.get(0).setFilterSelected(priceRange);
                    parentFilterAdapter.notifyDataSetChanged();

                    data.putExtra(SELECTED_FILTER_NAME, filterName);
                    setResult(RESULT_OK, data);
                    finish();

                    break;

                case 201: // category

                    categoryFilterName = data.getStringExtra(FILTER_NAME);
                    parentFilterList.get(1).setFilterSelected(categoryFilterName);
                    parentFilterAdapter.notifyDataSetChanged();

                    data.putExtra(SELECTED_FILTER_ID, String.valueOf(getCategoryFilterId(categoryFilterName)));
                    data.putExtra(SELECTED_FILTER_NAME, filterName);
                    setResult(RESULT_OK, data);
                    finish();

                    break;

                case 202: // brands

                    brandFilterName = data.getStringExtra(SELECTED_FILTER_NAME);
                    parentFilterList.get(2).setFilterSelected(brandFilterName);
                    parentFilterAdapter.notifyDataSetChanged();
                    data.putExtra(SELECTED_FILTER_NAME, "Brands");
                    setResult(RESULT_OK, data);
                    finish();
                    break;

                case 203:
                    break;
            }
        }
    }

    private Integer getCategoryFilterId(String catName) {

        for (int i = 0; i < categoryFilterList.size(); i++) {
            if (categoryFilterList.get(i).getName().equals(catName))
                return categoryFilterList.get(i).getId();
        }

        return null;
    }
}
