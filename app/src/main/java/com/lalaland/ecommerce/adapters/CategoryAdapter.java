package com.lalaland.ecommerce.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
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
    private CategoryListener mCategoryListener;

    public CategoryAdapter(Context context, CategoryListener categoryListener) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        mCategoryListener = categoryListener;
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

 /*   public void onInnerCategoryClicked(SubCategory subCategory) {
        mCategoryListener.onCategoryClicked(subCategory);
    }
*/
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

    class SubCategoryViewHolder extends RecyclerView.ViewHolder implements InnerCategoryAdapter.InnerCategoryListener {

        SubCategoryItemBinding subCategoryItemBinding;
        InnerCategoryAdapter innerCategoryAdapter;

        SubCategoryViewHolder(@NonNull SubCategoryItemBinding subCategoryItemBinding) {
            super(subCategoryItemBinding.getRoot());
            this.subCategoryItemBinding = subCategoryItemBinding;

            innerCategoryAdapter = new InnerCategoryAdapter(mContext, this);
        }

        void bindHolder(SubCategory subCategory) {

            if (subCategory.getInnerCategories().size() > 3)
                innerCategoryAdapter.setData(subCategory.getInnerCategories().subList(0, 3));
            else if (subCategory.getInnerCategories().size() > 0)
                innerCategoryAdapter.setData(subCategory.getInnerCategories());

            if (subCategory.getInnerCategories().size() > 0) {

                this.subCategoryItemBinding.rvInnerCategory.setAdapter(innerCategoryAdapter);
                this.subCategoryItemBinding.rvInnerCategory.setLayoutManager(new GridLayoutManager(mContext, 3));


                this.subCategoryItemBinding.setSubCategory(subCategory);
                this.subCategoryItemBinding.setAdapter(CategoryAdapter.this);
                this.subCategoryItemBinding.executePendingBindings();
            }
        }

        @Override
        public void onInnerCategoryClicked(InnerCategory innerCategory) {

        }
    }

    public interface CategoryListener {
        void onCategoryClicked(SubCategory subCategory);
    }
}
