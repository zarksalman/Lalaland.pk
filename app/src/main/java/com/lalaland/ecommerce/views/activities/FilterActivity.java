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
                    categoryFilterList.addAll(filterDataContainer.getData().getCategoryFilters());
                    brandList.addAll(filterDataContainer.getData().getBrands());
                    filterList.addAll(filterDataContainer.getData().getFilters());

                    setParentFilter();
                    setParentAdapter();
                }
            }
        });
    }

    private void setParentAdapter() {


        parentFilterAdapter = new ParentFilterAdapter(this, filter -> {

            String filterName;
            filterName = filter.getParentFilterName();
            intent.putExtra(FILTER_NAME, filterName);

            switch (filterName) {

                case "Price":
                    startActivityForResult(intent, 200);
                    break;

                case "Category":
                    startActivityForResult(intent, 201);
                    break;

                case "Brands":
                    startActivityForResult(intent, 202);
                    break;

                default:
                    getsubFilters(filterName);
            }
        });

        activityFilterBinding.rvFilters.setLayoutManager(new LinearLayoutManager(this));
        activityFilterBinding.rvFilters.setHasFixedSize(true);
        activityFilterBinding.rvFilters.setAdapter(parentFilterAdapter);
        parentFilterAdapter.setData(parentFilterList);


    }

    private void getsubFilters(String filterName) {

        List<Filter> subFilterList = new ArrayList<>();

        for (Filter filter : filterList) {

            if (filterName.equals(filter.getFilterName())) {
                subFilterList.add(filter);
            }
        }

        intent.putParcelableArrayListExtra("sub_filters", (ArrayList<? extends Parcelable>) subFilterList);
        startActivityForResult(intent, 203);
    }

    private void setParentFilter() {

        ParentFilter parentFilter = new ParentFilter();
        int iterator = 0;

        parentFilter.setId(0);
        parentFilter.setParentFilterName("Price");
        parentFilter.setFilterSelected("Any");
        parentFilterList.add(parentFilter);
        iterator++;

        if (categoryFilterList.size() > 0) {

            parentFilter = new ParentFilter();

            parentFilter.setId(1);
            parentFilter.setParentFilterName("Category");
            parentFilter.setFilterSelected("All");
            parentFilterList.add(parentFilter);
            iterator++;
        }

        if (brandList.size() > 0) {

            parentFilter = new ParentFilter();

            parentFilter.setId(2);
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
                case 200:

                    priceFilter = data.getStringExtra(PRICE_FILTER);
                    priceRange = data.getStringExtra(PRICE_RANGE);
                    parentFilterList.get(0).setFilterSelected(priceRange);
                    parentFilterAdapter.notifyDataSetChanged();
                    setResult(RESULT_OK, data);
                    finish();

                    break;

                case 201:
                    break;

                case 202:
                    break;

                case 203:
                    break;
            }
        }
    }
}
