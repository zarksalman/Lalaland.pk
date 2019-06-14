package com.lalaland.ecommerce.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.data.models.filters.Filter;
import com.lalaland.ecommerce.databinding.ActivitySubFiltersBinding;

import java.util.ArrayList;
import java.util.List;

import static com.lalaland.ecommerce.helpers.AppConstants.FILTER_NAME;
import static com.lalaland.ecommerce.helpers.AppConstants.PRICE_FILTER;
import static com.lalaland.ecommerce.helpers.AppConstants.PRICE_RANGE;

public class SubFiltersActivity extends AppCompatActivity {

    private ActivitySubFiltersBinding activitySubFiltersBinding;
    private String parentFilterName;
    private List<Filter> subFilterList = new ArrayList<>();
    private String subFilterName;
    private String priceParamsStart = "{\"price_range\":[";
    private String priceParamsMid;
    private String priceParamsEnd = "]}";
    private StringBuilder priceParams = new StringBuilder();
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySubFiltersBinding = DataBindingUtil.setContentView(this, R.layout.activity_sub_filters);

        subFilterName = getIntent().getStringExtra(FILTER_NAME);

        if (subFilterName.equals("Price")) {
            activitySubFiltersBinding.priceContainer.setVisibility(View.VISIBLE);

        } else if (subFilterName.equals("Brands")) {

        } else if (subFilterName.equals("Category")) {

        } else {
            subFilterList = getIntent().getParcelableArrayListExtra("sub_filters");
            setSubFilterAdapter();
        }

        activitySubFiltersBinding.tvApplySubFilter.setOnClickListener(v -> {

            if (subFilterName.equals("Price")) {
                setPriceParams();
            }

        });

    }

    private void setPriceParams() {

        StringBuilder lowPrice = new StringBuilder();
        StringBuilder highPrice = new StringBuilder();

        lowPrice.append(activitySubFiltersBinding.etLow.getText().toString().trim());
        highPrice.append(activitySubFiltersBinding.etHigh.getText().toString().trim());

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

        setResults();
    }

    private void setSubFilterAdapter() {

    }

    private void setResults() {
        intent = new Intent();
        intent.putExtra(PRICE_FILTER, priceParams.toString().trim());
        intent.putExtra(PRICE_RANGE, priceParamsMid.trim());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
    }
}
