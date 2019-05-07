package com.lalaland.ecommerce.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.data.models.category.Category;
import com.lalaland.ecommerce.data.models.home.Actions;
import com.lalaland.ecommerce.databinding.ActionLayoutBinding;
import com.lalaland.ecommerce.databinding.CategoryItemBinding;

import java.util.List;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context mContext;
    private List<Category> mCategory;
    private CategoryItemBinding categoryItemBinding;
    private LayoutInflater inflater;
    private CategoryClickListener mCategoryClickListener;

    public CategoryAdapter(Context context, CategoryClickListener categoryClickListener) {
        mContext = context;
        mCategoryClickListener = categoryClickListener;
        inflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        categoryItemBinding = DataBindingUtil.inflate(inflater, R.layout.category_item, parent, false);
        return new CategoryViewHolder(categoryItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = mCategory.get(position);
        holder.bindHolder(category);
    }

    @Override
    public int getItemCount() {
        return mCategory.size();
    }

    public void categoryClicked(View view, Category category) {
        mCategoryClickListener.onCategoryClicked(category);
    }

    public void setData(List<Category> categoryList) {

        mCategory = categoryList;
        notifyDataSetChanged();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        CategoryItemBinding mCategoryItemBinding;

        CategoryViewHolder(@NonNull CategoryItemBinding categoryItemBinding) {
            super(categoryItemBinding.getRoot());
            mCategoryItemBinding = categoryItemBinding;
        }

        void bindHolder(Category category) {
            mCategoryItemBinding.setCategory(category);
            mCategoryItemBinding.setAdapter(CategoryAdapter.this);
            mCategoryItemBinding.executePendingBindings();
        }
    }

    public interface CategoryClickListener {
        void onCategoryClicked(Category category);
    }
}
