package com.lalaland.ecommerce.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.data.models.categories.InnerCategory;
import com.lalaland.ecommerce.databinding.InnerCategoryItemBinding;

import java.util.ArrayList;
import java.util.List;


public class InnerCategoryAdapter extends RecyclerView.Adapter<InnerCategoryAdapter.InnerCategoryViewHolder> {

    private Context mContext;
    private List<InnerCategory> mInnerCategories = new ArrayList<>();
    private InnerCategoryItemBinding mInnerCategoryItemBinding;
    private LayoutInflater inflater;
    private InnerCategoryListener mInnerCategoryListener;

    public InnerCategoryAdapter(Context context, InnerCategoryListener innerCategoryListener) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        mInnerCategoryListener = innerCategoryListener;
    }

    @NonNull
    @Override
    public InnerCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        mInnerCategoryItemBinding = DataBindingUtil.inflate(inflater, R.layout.inner_category_item, parent, false);
        return new InnerCategoryViewHolder(mInnerCategoryItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerCategoryViewHolder holder, int position) {
        InnerCategory innerCategory = mInnerCategories.get(position);
        holder.bindHolder(innerCategory);
    }

    @Override
    public int getItemCount() {
        return mInnerCategories.size();
    }

    public void setData(List<InnerCategory> innerCategories) {

        mInnerCategories = innerCategories;
        notifyDataSetChanged();
    }

    public void onInnerCategoryClicked(InnerCategory innerCategory) {
        mInnerCategoryListener.onInnerCategoryClicked(innerCategory);
    }


    class InnerCategoryViewHolder extends RecyclerView.ViewHolder {

        InnerCategoryItemBinding innerCategoryItemBinding;

        InnerCategoryViewHolder(@NonNull InnerCategoryItemBinding innerCategoryItemBinding) {
            super(innerCategoryItemBinding.getRoot());

            this.innerCategoryItemBinding = innerCategoryItemBinding;
        }

        void bindHolder(InnerCategory innerCategory) {
            this.innerCategoryItemBinding.setInnerCategory(innerCategory);
            this.innerCategoryItemBinding.setAdapter(InnerCategoryAdapter.this);
            this.innerCategoryItemBinding.executePendingBindings();
        }
    }

    public interface InnerCategoryListener {
        void onInnerCategoryClicked(InnerCategory innerCategory);
    }
}
