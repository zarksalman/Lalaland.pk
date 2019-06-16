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
import com.lalaland.ecommerce.data.models.filters.PvFilterCustomModel;
import com.lalaland.ecommerce.databinding.ActivitySubFiltersBinding;

import java.util.ArrayList;
import java.util.List;

import static com.lalaland.ecommerce.helpers.AppConstants.FILTER_NAME;
import static com.lalaland.ecommerce.helpers.AppConstants.PRICE_FILTER;
import static com.lalaland.ecommerce.helpers.AppConstants.PRICE_RANGE;
import static com.lalaland.ecommerce.helpers.AppConstants.PV_FILTER_;
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

    // pvfilter parameter ==> Base64(" "{"2":[90,100,110], "5":[40,400,310]}" ")
    private String pvFilterParamsStart = "\"{\"";
    private String pvParentFilterId = "";
    private String pvFilterParamsIdsStart = "\":[";
    private StringBuilder pvFilterParamsIdsMid = new StringBuilder();
    private String pvFilterParamsIdsEnd = "],";
    private String pvFilterParamsEnd = "}\"";
    private StringBuilder pvFilterParams = new StringBuilder();

    private Intent intent;
    String filterName;
    FiltersAdapter filtersAdapter;

    StringBuffer sFilterNames = new StringBuffer();
    StringBuffer sFilterIds = new StringBuffer();



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
            } else if (subFilterName.equals("Brands")) {
                setBrandsFiltersIntent();
            } else {
                setOthersFilterIntent();
            }

            setResult(RESULT_OK, intent);
            finish();
        });

        activitySubFiltersBinding.ivBtnBack.setOnClickListener(v -> onBackPressed());

    }

    private String setBrandParams(String sFilterNames) {

        StringBuffer sb;

        brandParams.append(brandParamsStart);

        for (int i = 0; i < selectedFilters.size(); i++) {
            brandParamsMid = brandParamsMid.concat(String.valueOf(selectedFilters.get(i).getId()));
            brandParamsMid = brandParamsMid.concat(",");
            sFilterNames = sFilterNames.concat(selectedFilters.get(i).getDisplayName()).concat(",");
        }

        brandParams.append(trimLastComa(brandParamsMid));
        brandParams.append(brandParamsEnd);

        return trimLastComa(sFilterNames);
    }

    private void setPriceParams() {

        StringBuilder lowPrice = new StringBuilder();
        StringBuilder highPrice = new StringBuilder();

        lowPrice.append(activitySubFiltersBinding.etLow.getText().toString().trim());
        highPrice.append(activitySubFiltersBinding.etHigh.getText().toString().trim());

        if (Integer.parseInt(lowPrice.toString()) >= Integer.parseInt(highPrice.toString())) {

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
    }

    void setPbFiltersParams() {

        pvFilterParams.append(pvFilterParamsStart);
        pvParentFilterId = String.valueOf(selectedFilters.get(0).getId());
        pvFilterParams.append(pvParentFilterId);
        pvFilterParams.append(pvFilterParamsIdsStart);

        for (int i = 0; i < selectedFilters.size(); i++) {

            pvFilterParamsIdsMid.append(selectedFilters.get(i).getProductVariationValueId());
            pvFilterParamsIdsMid.append(",");
        }

        pvFilterParamsIdsMid = new StringBuilder(trimLastComa(pvFilterParamsIdsMid.toString()));
        pvFilterParams.append(pvFilterParamsIdsMid);
        pvFilterParams.append(pvFilterParamsIdsEnd);
        pvFilterParams = new StringBuilder(trimLastComa(pvFilterParams.toString()));
        pvFilterParams.append(pvFilterParamsEnd);

    }

    private void setCategoryFilterIntent() {

        intent = new Intent();
        String catFilter;

        if (filtersAdapter.getSelectedCategory() != null) {
            catFilter = filtersAdapter.getSelectedCategory().getDisplayName();
            intent.putExtra(FILTER_NAME, catFilter);
        } else
            Toast.makeText(this, "Select atleast one filter", Toast.LENGTH_SHORT).show();
    }

    private void setBrandsFiltersIntent() {

        intent = new Intent();
        String sFilterNames = "";

        if (filtersAdapter.getSelectedFilters() != null) {
            selectedFilters = filtersAdapter.getSelectedFilters();
            sFilterNames = setBrandParams(sFilterNames);

            intent.putExtra(SELECTED_FILTER_NAME, sFilterNames);
            intent.putExtra(SELECTED_FILTER_ID, brandParams.toString());
        } else
            Toast.makeText(this, "Select atleast one filter", Toast.LENGTH_SHORT).show();
    }

    private void setOthersFilterIntent() {
        intent = new Intent();


        if (filtersAdapter.getSelectedFilters() != null) {
            selectedFilters = filtersAdapter.getSelectedFilters();

            setPbFiltersParams();

            intent.putExtra(PV_FILTER_, pvFilterParams.toString());
            intent.putExtra(SELECTED_FILTER_NAME, selectedFilters.get(0).getFilterName());
            intent.putExtra(FILTER_NAME, selectedFilters.get(0).getFilterName());

            //intent.putParcelableArrayListExtra("selected_filters", (ArrayList<? extends Parcelable>) selectedFilters);

            // for multiple filters
            /*  setOthersParams();

            intent.putExtra(FILTER_NAME, selectedFilters.get(0).getFilterName());
            intent.putExtra(SELECTED_FILTER_NAME, sFilterNames.toString());
            intent.putExtra(SELECTED_FILTER_ID, sFilterIds.toString());
            */

        } else
            Toast.makeText(this, "Select atleast one filter", Toast.LENGTH_SHORT).show();
    }

    private void setOthersParams() {

        List<PvFilterCustomModel> pvFilterCustomModelList = new ArrayList<>();
        List<Filter> subPvFilter = new ArrayList<>();
        PvFilterCustomModel pvFilterCustomModel = new PvFilterCustomModel();

        for (int i = 0; i < selectedFilters.size(); i++) {
            sFilterNames.append(selectedFilters.get(i).getDisplayName());
            sFilterNames.append(",");

            sFilterIds.append(selectedFilters.get(i).getProductVariationValueId());
            sFilterIds.append(",");

            //     filterName = selectedFilters.get(i).getFilterName();
        }

        sFilterNames = new StringBuffer(trimLastComa(sFilterNames.toString()));
        sFilterIds = new StringBuffer(trimLastComa(sFilterIds.toString()));

    }

    private String trimLastComa(String trimString) {

        StringBuffer sb = new StringBuffer(trimString);
        sb.replace(trimString.lastIndexOf(","), trimString.lastIndexOf(",") + 1, "");

        return sb.toString();
    }

    @Override
    public void onBackPressed() {

        // for multiple filters

      /*  switch (subFilterName) {
            case "Price":
                intent = new Intent();
                intent.putExtra(FILTER_NAME, "not null");
                intent.putExtra(PRICE_RANGE, "Any");
                break;
            case "Category":
                setCategoryFilterIntent();
                break;
            case "Brands":
                setBrandsFiltersIntent();
                break;
            default:
                setOthersFilterIntent();
                break;
        }
        setResult(RESULT_CANCELED, intent);
        finish();*/
    }
}
