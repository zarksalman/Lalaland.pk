package com.lalaland.ecommerce.views.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.adapters.CityAdapter;
import com.lalaland.ecommerce.data.models.category.City;
import com.lalaland.ecommerce.databinding.ActivitySelectCityBinding;
import com.lalaland.ecommerce.helpers.AppConstants;
import com.lalaland.ecommerce.helpers.AppUtils;

import java.util.ArrayList;
import java.util.List;

public class SelectCityActivity extends AppCompatActivity implements CityAdapter.CityListener {

    private ActivitySelectCityBinding activitySelectCityBinding;
    private List<City> cityList = new ArrayList<>();
    private CityAdapter cityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySelectCityBinding = DataBindingUtil.setContentView(this, R.layout.activity_select_city);
        activitySelectCityBinding.setClickListener(this);

        setAdapter();

        activitySelectCityBinding.ivBackArrow.setOnClickListener(v -> {
            onBackPressed();
        });

        activitySelectCityBinding.svCity.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                cityAdapter.filter(query);

                AppUtils.hideKeyboard(SelectCityActivity.this);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                cityAdapter.filter(newText);
                return true;
            }
        });
    }

    void setAdapter() {


        cityList = AppConstants.staticCitiesList;

        cityAdapter = new CityAdapter(this, this);
        activitySelectCityBinding.rvCity.setHasFixedSize(true);
        activitySelectCityBinding.rvCity.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        activitySelectCityBinding.rvCity.setAdapter(cityAdapter);
        cityAdapter.setData(cityList);
    }

    @Override
    public void onCityClicked(City city) {

        Intent intent = new Intent();

        intent.putExtra("city_id", String.valueOf(city.getCityId()));
        intent.putExtra("city_name", city.getCityName());

        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
