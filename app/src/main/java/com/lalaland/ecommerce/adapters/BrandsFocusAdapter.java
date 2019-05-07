package com.lalaland.ecommerce.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.data.models.home.Actions;
import com.lalaland.ecommerce.data.models.home.FeaturedBrand;
import com.lalaland.ecommerce.databinding.ActionLayoutBinding;
import com.lalaland.ecommerce.databinding.BrandsFocusItemBinding;

import java.util.List;


public class BrandsFocusAdapter extends RecyclerView.Adapter<BrandsFocusAdapter.BrandsFocusViewHolder> {

    private Context mContext;
    private List<FeaturedBrand> mFeaturedBrandList;
    private BrandsFocusItemBinding brandsFocusItemBinding;
    private LayoutInflater inflater;
    private FeatureBrandClickListener mFeatureBrandClickListener;

    public BrandsFocusAdapter(Context context, FeatureBrandClickListener featureBrandClickListener) {
        mContext = context;
        mFeatureBrandClickListener = featureBrandClickListener;
        inflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public BrandsFocusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        brandsFocusItemBinding = DataBindingUtil.inflate(inflater, R.layout.brands_focus_item, parent, false);
        return new BrandsFocusViewHolder(brandsFocusItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull BrandsFocusViewHolder holder, int position) {
        FeaturedBrand featuredBrand = mFeaturedBrandList.get(position);
        holder.bindHolder(featuredBrand);
    }

    @Override
    public int getItemCount() {
        return mFeaturedBrandList.size();
    }

    public void brandClicked(View view, FeaturedBrand featuredBrand) {
        mFeatureBrandClickListener.onBrandClicked(featuredBrand);
    }

    public void setData(List<FeaturedBrand> featuredBrandList) {

        mFeaturedBrandList = featuredBrandList;
        notifyDataSetChanged();
    }

    class BrandsFocusViewHolder extends RecyclerView.ViewHolder {

        BrandsFocusItemBinding mBrandsFocusItemBinding;
        BrandsFocusViewHolder(@NonNull BrandsFocusItemBinding brandsFocusItemBinding) {
            super(brandsFocusItemBinding.getRoot());

            mBrandsFocusItemBinding = brandsFocusItemBinding;
        }

        void bindHolder(FeaturedBrand featuredBrand) {
            mBrandsFocusItemBinding.setBrand(featuredBrand);
            mBrandsFocusItemBinding.setAdapter(BrandsFocusAdapter.this);
            mBrandsFocusItemBinding.executePendingBindings();
        }
    }

    public interface FeatureBrandClickListener {
        void onBrandClicked(FeaturedBrand featuredBrand);
    }
}
