package com.lalaland.ecommerce.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.data.models.category.CategoryBrand;
import com.lalaland.ecommerce.databinding.CategoryBrandItemBinding;

import java.util.ArrayList;
import java.util.List;


public class CategorBrandAdapter extends RecyclerView.Adapter<CategorBrandAdapter.CategoryBrandViewHolder> {

    private Context mContext;
    private List<CategoryBrand> mCategoryBrands = new ArrayList<>();
    private CategoryBrandItemBinding categoryBrandItemBinding;
    private LayoutInflater inflater;
    private CategoryBrandListener mCategoryBrandListener;

    public CategorBrandAdapter(Context context, CategoryBrandListener categoryBrandListener) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        mCategoryBrandListener = categoryBrandListener;
    }

    @NonNull
    @Override
    public CategoryBrandViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        categoryBrandItemBinding = DataBindingUtil.inflate(inflater, R.layout.category_brand_item, parent, false);
        return new CategoryBrandViewHolder(categoryBrandItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryBrandViewHolder holder, int position) {
        CategoryBrand categoryBrand = mCategoryBrands.get(position);
        holder.bindHolder(categoryBrand);
    }

    @Override
    public int getItemCount() {
        return mCategoryBrands.size();
    }

    public void setData(List<CategoryBrand> categoryBrands) {

        mCategoryBrands = categoryBrands;
        notifyDataSetChanged();
    }

    public void onCategoryBrandClicked(View view, CategoryBrand brand) {
        mCategoryBrandListener.onCategoryBrandClicked(brand);
    }

    class CategoryBrandViewHolder extends RecyclerView.ViewHolder {

        CategoryBrandItemBinding mCategoryBrandItemBinding;

        CategoryBrandViewHolder(@NonNull CategoryBrandItemBinding categoryBrandItemBinding) {
            super(categoryBrandItemBinding.getRoot());

            mCategoryBrandItemBinding = categoryBrandItemBinding;
        }

        void bindHolder(CategoryBrand categoryBrand) {
            mCategoryBrandItemBinding.setBrand(categoryBrand);
            mCategoryBrandItemBinding.setAdapter(CategorBrandAdapter.this);
            mCategoryBrandItemBinding.executePendingBindings();
        }
    }

    public interface CategoryBrandListener {
        void onCategoryBrandClicked(CategoryBrand categoryBrand);
    }
}
