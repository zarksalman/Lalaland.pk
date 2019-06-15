package com.lalaland.ecommerce.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.adapters.FiltersAdapter;
import com.lalaland.ecommerce.data.models.filters.Filter;
import com.lalaland.ecommerce.databinding.ActivitySubFiltersBinding;

import java.util.ArrayList;
import java.util.List;

import static com.lalaland.ecommerce.helpers.AppConstants.FILTER_NAME;
import static com.lalaland.ecommerce.helpers.AppConstants.PRICE_FILTER;
import static com.lalaland.ecommerce.helpers.AppConstants.PRICE_RANGE;
import static com.lalaland.ecommerce.helpers.AppConstants.SELECTED_FILTER_ID;
import static com.lalaland.ecommerce.helpers.AppConstants.SELECTED_FILTER_NAME;

public class SubFiltersActivity extends AppCompatActivity {

    private ActivitySubFiltersBinding activitySubFiltersBinding;
    private String parentFilterName;
    private List<Filter> subFilterList = new ArrayList<>();
    private List<Filter> selectedSubFilterList = new ArrayList<>();
    List<Filter> selectedFilters = new ArrayList<>();
    private String subFilterName;

    // price filter parameter ==> Base64("{"price_range":[90,100,110]}")
    private String priceParamsStart = "{\"price_range\":[";
    private String priceParamsMid;
    private String priceParamsEnd = "]}";
    private StringBuilder priceParams = new StringBuilder();

    // brand filter parameter ==> Base64("[90,100,110]")
    private String brandParamsStart = "[";
    private String brandParamsMid = "";
    private String brandParamsEnd = "]";
    private StringBuilder brandParams = new StringBuilder();

    private Intent intent;
    String filterName;
    FiltersAdapter filtersAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySubFiltersBinding = DataBindingUtil.setContentView(this, R.layout.activity_sub_filters);

        subFilterName = getIntent().getStringExtra(FILTER_NAME);

        if (subFilterName.equals("Price")) {
            activitySubFiltersBinding.priceContainer.setVisibility(View.VISIBLE);
        } else {
            subFilterList = getIntent().getParcelableArrayListExtra("sub_filters");
            filterName = getIntent().getStringExtra(FILTER_NAME);
            setSubFilterAdapter();
        }

        activitySubFiltersBinding.tvApplySubFilter.setOnClickListener(v -> {

            if (subFilterName.equals("Price")) {
                setPriceParams();
            } else if (subFilterName.equals("Category")) {
                setCategoryFilterIntent();
            } else {

                setOtherFiltersIntent();
            }
        });

    }

    private String setBrandParams(String sFilterNames) {

        StringBuffer sb;

        brandParams.append(brandParamsStart);

        for (int i = 0; i < selectedFilters.size(); i++) {
            brandParamsMid = brandParamsMid.concat(String.valueOf(selectedFilters.get(i).getId()));
            brandParamsMid = brandParamsMid.concat(",");
            sFilterNames = sFilterNames.concat(selectedFilters.get(i).getDisplayName()).concat(",");
        }

/*
        int lastComa = brandParamsMid.lastIndexOf(",");
        brandParamsMid = brandParamsMid.replace(",$", "");

        brandParams.append(brandParamsMid);
        brandParams.append(brandParamsEnd);
*/


        sb = new StringBuffer(brandParamsMid);
        sb.replace(brandParamsMid.lastIndexOf(","), brandParamsMid.lastIndexOf(",") + 1, "");

        brandParams.append(sb);
        brandParams.append(brandParamsEnd);

        sb = new StringBuffer(sFilterNames);
        sb.replace(sFilterNames.lastIndexOf(","), sFilterNames.lastIndexOf(",") + 1, "");

        return sb.toString();

    }

    private void setPriceParams() {

        StringBuilder lowPrice = new StringBuilder();
        StringBuilder highPrice = new StringBuilder();

        lowPrice.append(activitySubFiltersBinding.etLow.getText().toString().trim());
        highPrice.append(activitySubFiltersBinding.etHigh.getText().toString().trim());

        if (Integer.parseInt(lowPrice.toString()) > Integer.parseInt(highPrice.toString())) {

            Toast.makeText(this, "Low price should be less than high price !!!", Toast.LENGTH_SHORT).show();
            lowPrice = new StringBuilder();
            highPrice = new StringBuilder();
            return;
        }

        if (!lowPrice.toString().isEmpty() && !highPrice.toString().isEmpty()) {
            // do nothing just send low value [10,100]
            lowPrice.append(",");
            lowPrice.append(highPrice.toString());
        } else if (lowPrice.toString().isEmpty()) {

            // do nothing just send low value [0,100]
            lowPrice.append(0);
            lowPrice.append(",");
            lowPrice.append(highPrice.toString());
        } else if (highPrice.toString().isEmpty()) {
            // do nothing just send low value [100]
            lowPrice.append(highPrice.toString());
        } else {

            priceParamsStart = "";
            priceParamsMid = "";
            priceParamsEnd = "";
        }

        priceParamsMid = lowPrice.toString();

        priceParams = new StringBuilder();
        priceParams.append(priceParamsStart);
        priceParams.append(priceParamsMid);
        priceParams.append(priceParamsEnd);

        setPriceFilterIntent();
    }

    private void setSubFilterAdapter() {


        filtersAdapter = new FiltersAdapter(this, filterName);

        activitySubFiltersBinding.rvColor.setHasFixedSize(true);
        activitySubFiltersBinding.rvColor.setAdapter(filtersAdapter);
        activitySubFiltersBinding.rvColor.setLayoutManager(new LinearLayoutManager(this));
        filtersAdapter.setData(subFilterList);

        activitySubFiltersBinding.rvColor.setVisibility(View.VISIBLE);
    }

    private void setPriceFilterIntent() {
        intent = new Intent();
        intent.putExtra(PRICE_FILTER, priceParams.toString().trim());
        intent.putExtra(PRICE_RANGE, priceParamsMid.trim());
        setResults();
    }

    private void setCategoryFilterIntent() {

        intent = new Intent();
        String catFilter;

        if (filtersAdapter.getSelectedCategory() != null) {
            catFilter = filtersAdapter.getSelectedCategory().getDisplayName();
            intent.putExtra(FILTER_NAME, catFilter);
        }

        setResults();
    }

    private void setOtherFiltersIntent() {

        intent = new Intent();
        String sFilterNames = "";

        if (filtersAdapter.getSelectedFilters() != null) {
            selectedFilters = filtersAdapter.getSelectedFilters();
            sFilterNames = setBrandParams(sFilterNames);

            intent.putExtra(SELECTED_FILTER_NAME, sFilterNames);
            intent.putExtra(SELECTED_FILTER_ID, brandParams.toString());
        }
        setResults();
    }

    private void setResults() {
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
