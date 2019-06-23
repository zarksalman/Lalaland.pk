package com.lalaland.ecommerce.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
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

    public void showAll(SubCategory subCategory) {
        mCategoryListener.onCategoryClicked(subCategory);
    }

    class SubCategoryViewHolder extends RecyclerView.ViewHolder implements InnerCategoryAdapter.InnerCategoryListener {

        SubCategoryItemBinding subCategoryItemBinding;
        InnerCategoryAdapter innerCategoryAdapter;

        SubCategoryViewHolder(@NonNull SubCategoryItemBinding subCategoryItemBinding) {
            super(subCategoryItemBinding.getRoot());
            this.subCategoryItemBinding = subCategoryItemBinding;

            this.innerCategoryAdapter = new InnerCategoryAdapter(mContext, this);
            this.subCategoryItemBinding.rvInnerCategory.setAdapter(innerCategoryAdapter);
            this.subCategoryItemBinding.rvInnerCategory.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));

        }

        void bindHolder(SubCategory subCategory) {

          /*  if (subCategory.getInnerCategories().size() >= 3)
                this.innerCategoryAdapter.setData(subCategory.getInnerCategories().subList(0, 3));
            else if (subCategory.getInnerCategories().size() > 0)
          */

            this.innerCategoryAdapter.setData(subCategory.getInnerCategories());

            this.subCategoryItemBinding.setSubCategory(subCategory);
            this.subCategoryItemBinding.setAdapter(CategoryAdapter.this);
            this.subCategoryItemBinding.executePendingBindings();

        }

        @Override
        public void onInnerCategoryClicked(InnerCategory innerCategory) {
            mCategoryListener.onInnerCategoryClicked(innerCategory);
        }
    }

    public interface CategoryListener {
        void onCategoryClicked(SubCategory subCategory);

        void onInnerCategoryClicked(InnerCategory innerCategory);
    }
}
