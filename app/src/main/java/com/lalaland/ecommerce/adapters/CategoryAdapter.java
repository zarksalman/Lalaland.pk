package com.lalaland.ecommerce.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.data.models.categories.InnerCategory;
import com.lalaland.ecommerce.data.models.categories.SubCategory;
import com.lalaland.ecommerce.databinding.SubCategoryItemBinding;

import java.util.ArrayList;
import java.util.List;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.SubCategoryViewHolder> {

    private Context mContext;
    private List<SubCategory> mSubCategories = new ArrayList<>();
    private SubCategoryItemBinding mSubCategoryItemBinding;
    private LayoutInflater inflater;
    private InnerCategoryListener mInnerCategoryListener;

    public CategoryAdapter(Context context, InnerCategoryListener innerCategoryListener) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        mInnerCategoryListener = innerCategoryListener;
    }

    @NonNull
    @Override
    public SubCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        mSubCategoryItemBinding = DataBindingUtil.inflate(inflater, R.layout.sub_category_item, parent, false);
        return new SubCategoryViewHolder(mSubCategoryItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoryViewHolder holder, int position) {
        SubCategory subCategory = mSubCategories.get(position);
        holder.bindHolder(subCategory);
    }

    @Override
    public int getItemCount() {
        return mSubCategories.size();
    }

    public void setData(List<SubCategory> subCategories) {

        mSubCategories = subCategories;
        notifyDataSetChanged();
    }

    public void onInnerCategoryClicked(InnerCategory innerCategory) {
        mInnerCategoryListener.onInnerCategoryClicked(innerCategory);
    }

/*
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
*/

    class SubCategoryViewHolder extends RecyclerView.ViewHolder {

        SubCategoryItemBinding subCategoryItemBinding;

        SubCategoryViewHolder(@NonNull SubCategoryItemBinding subCategoryItemBinding) {
            super(subCategoryItemBinding.getRoot());

            this.subCategoryItemBinding = subCategoryItemBinding;
        }

        void bindHolder(SubCategory subCategory) {
            this.subCategoryItemBinding.setSubCategory(subCategory);
            this.subCategoryItemBinding.setAdapter(CategoryAdapter.this);
            this.subCategoryItemBinding.executePendingBindings();
        }
    }

    public interface InnerCategoryListener {
        void onInnerCategoryClicked(InnerCategory innerCategory);
    }
}
