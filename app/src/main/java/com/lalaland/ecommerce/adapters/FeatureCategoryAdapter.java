package com.lalaland.ecommerce.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.data.models.home.FeaturedCategory;
import com.lalaland.ecommerce.databinding.FeatureCategoryItemBinding;

import java.util.List;


public class FeatureCategoryAdapter extends RecyclerView.Adapter<FeatureCategoryAdapter.ProductViewHolder> {

    private Context mContext;
    private List<FeaturedCategory> mProductList;
    private FeatureCategoryItemBinding productItemBinding;
    private LayoutInflater inflater;
    private FeatureCategoryListener mFeatureCategory;

    public FeatureCategoryAdapter(Context context, FeatureCategoryListener featureCategoryListener) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        mFeatureCategory = featureCategoryListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        productItemBinding = DataBindingUtil.inflate(inflater, R.layout.feature_category_item, parent, false);
        return new ProductViewHolder(productItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        FeaturedCategory product = mProductList.get(position);
        holder.bindHolder(product);
    }

    @Override
    public int getItemCount() {

        if (mProductList != null)
            return mProductList.size();

        return 0;
    }

    public void setData(List<FeaturedCategory> productList) {

        mProductList = productList;
        notifyDataSetChanged();
    }

    public void onFeatureCategoryClicked(View view, FeaturedCategory featuredCategory) {
        mFeatureCategory.onFeatureCategoryClicked(featuredCategory);
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        FeatureCategoryItemBinding mFeatureCategoryItemBinding;

        ProductViewHolder(@NonNull FeatureCategoryItemBinding featureCategoryItemBinding) {
            super(productItemBinding.getRoot());

            mFeatureCategoryItemBinding = featureCategoryItemBinding;
        }

        void bindHolder(FeaturedCategory featuredCategory) {

            mFeatureCategoryItemBinding.setFeatureCategory(featuredCategory);
            mFeatureCategoryItemBinding.setAdapter(FeatureCategoryAdapter.this);
            mFeatureCategoryItemBinding.executePendingBindings();
        }
    }

    public interface FeatureCategoryListener {
        void onFeatureCategoryClicked(FeaturedCategory featuredCategory);
    }
}
