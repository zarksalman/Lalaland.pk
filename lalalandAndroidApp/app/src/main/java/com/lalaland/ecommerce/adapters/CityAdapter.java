package com.lalaland.ecommerce.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.data.models.category.City;
import com.lalaland.ecommerce.databinding.CityItemBinding;

import java.util.ArrayList;
import java.util.List;


public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {

    private Context mContext;
    private List<City> mCtyList, filteredCityList = new ArrayList<>();
    private CityItemBinding cityItemBinding;
    private LayoutInflater inflater;
    private CityListener mCityListener;

    public CityAdapter(Context context, CityListener cityListener) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        mCityListener = cityListener;
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        cityItemBinding = DataBindingUtil.inflate(inflater, R.layout.city_item, parent, false);
        return new CityViewHolder(cityItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        City city = filteredCityList.get(position);
        holder.bindHolder(city);
    }

    @Override
    public int getItemCount() {
        return filteredCityList.size();
    }

    public void setData(List<City> cityList) {

        mCtyList = cityList;
        filteredCityList.addAll(cityList);

        notifyDataSetChanged();
    }

    public void onCityClicked(View view, City city) {
        mCityListener.onCityClicked(city);
    }

    public void filter(String text) {
        filteredCityList.clear();

        if (text.isEmpty()) {
            filteredCityList.addAll(mCtyList);
        } else {
            text = text.toLowerCase();
            for (City city : mCtyList) {
                if (city.getCityName().toLowerCase().contains(text)) {
                    filteredCityList.add(city);
                }
            }
        }
        notifyDataSetChanged();
    }

    class CityViewHolder extends RecyclerView.ViewHolder {

        CityItemBinding mCityItemBinding;

        CityViewHolder(@NonNull CityItemBinding cityItemBinding) {
            super(cityItemBinding.getRoot());

            mCityItemBinding = cityItemBinding;
        }

        void bindHolder(City city) {
            mCityItemBinding.setCity(city);
            mCityItemBinding.setAdapter(CityAdapter.this);
            mCityItemBinding.executePendingBindings();
        }
    }

    public interface CityListener {
        void onCityClicked(City city);
    }
}
