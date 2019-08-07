package com.lalaland.ecommerce.adapters;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.data.models.home.FeaturedBrand;
import com.lalaland.ecommerce.databinding.OldBrandsFocusItemBinding;

import java.util.List;


public class BrandsFocusAdapter extends RecyclerView.Adapter<BrandsFocusAdapter.BrandsFocusViewHolder> {

    private Context mContext;
    private List<FeaturedBrand> mFeaturedBrandList;
    private OldBrandsFocusItemBinding brandsFocusItemBinding;
    private LayoutInflater inflater;
    private FeatureBrandClickListener mFeatureBrandClickListener;
    float width = 0;

    public BrandsFocusAdapter(Context context, FeatureBrandClickListener featureBrandClickListener) {
        mContext = context;
        mFeatureBrandClickListener = featureBrandClickListener;
        inflater = LayoutInflater.from(context);
        width = getScreenWidth();
    }

    @NonNull
    @Override
    public BrandsFocusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        brandsFocusItemBinding = DataBindingUtil.inflate(inflater, R.layout.old_brands_focus_item, parent, false);

        brandsFocusItemBinding.oldBrandParent.getLayoutParams().width = (int) (width / 1.5);
//        mInnerCategoryItemBinding.ivInnerCat.getLayoutParams().height = (int) (getScreenWidth());
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

    public int getScreenWidth() {

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size.x;
    }

    class BrandsFocusViewHolder extends RecyclerView.ViewHolder {

        OldBrandsFocusItemBinding mBrandsFocusItemBinding;

        BrandsFocusViewHolder(@NonNull OldBrandsFocusItemBinding brandsFocusItemBinding) {
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
