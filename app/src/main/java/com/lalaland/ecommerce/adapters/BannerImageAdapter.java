package com.lalaland.ecommerce.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.data.models.home.HomeBanner;
import com.lalaland.ecommerce.databinding.SliderItemBinding;
import com.lalaland.ecommerce.helpers.AppConstants;

import java.util.ArrayList;
import java.util.List;

import static com.lalaland.ecommerce.helpers.AppConstants.TAG;

public class BannerImageAdapter extends PagerAdapter {


    private SliderItemBinding productImageItemBinding;
    List<HomeBanner> productMultimedia = new ArrayList<>();


    Context context;
    LayoutInflater layoutInflater;

    public BannerImageAdapter(Context context, List<HomeBanner> mProductMultimedia) {
        this.context = context;
        productMultimedia = mProductMultimedia;
    }

    @Override
    public int getCount() {
        return productMultimedia.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {

        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View view = layoutInflater.inflate(R.layout.product_image_item, container, false);

        productImageItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.slider_item, container, false);

        String imgUrl = AppConstants.BANNER_STORAGE_BASE_URL.concat(productMultimedia.get(position).getBannerImage());
        Log.d("imgUrl", imgUrl);
/*
        Glide.with(context)
                .load(imgUrl)
                .placeholder(R.drawable.placeholder_products)
                .into(productImageItemBinding.ivProduct);
*/

        Glide
                .with(context)
                .load(imgUrl)
                .placeholder(R.drawable.placeholder_products)
                .error(R.drawable.placeholder_products)
                .into(productImageItemBinding.ivSlider);

        container.addView(productImageItemBinding.getRoot());
        return productImageItemBinding.getRoot();

    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

}
