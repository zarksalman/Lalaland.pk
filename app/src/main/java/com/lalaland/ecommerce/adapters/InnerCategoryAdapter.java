package com.lalaland.ecommerce.adapters;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;

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
    float width = 0;
    public InnerCategoryAdapter(Context context, InnerCategoryListener innerCategoryListener) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        mInnerCategoryListener = innerCategoryListener;
        width = getScreenWidth();
    }

    @NonNull
    @Override
    public InnerCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        mInnerCategoryItemBinding = DataBindingUtil.inflate(inflater, R.layout.inner_category_item, parent, false);

        mInnerCategoryItemBinding.innerCatParent.getLayoutParams().width = (int) (width / 4.8);
//        mInnerCategoryItemBinding.ivInnerCat.getLayoutParams().height = (int) (getScreenWidth());
        return new InnerCategoryViewHolder(mInnerCategoryItemBinding);
    }

    public int getScreenWidth() {

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size.x;
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
