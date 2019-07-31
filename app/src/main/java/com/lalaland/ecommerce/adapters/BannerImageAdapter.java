package com.lalaland.ecommerce.adapters;

import android.content.Context;
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
import com.lalaland.ecommerce.databinding.BannerImageItemBinding;

import java.util.ArrayList;
import java.util.List;

import static com.lalaland.ecommerce.helpers.AppConstants.BANNER_STORAGE_BASE_URL;

public class BannerImageAdapter extends PagerAdapter {


    private BannerImageItemBinding bannerImageItemBinding;
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

        bannerImageItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.banner_image_item, container, false);

        String imgUrl = BANNER_STORAGE_BASE_URL.concat(productMultimedia.get(position).getBannerImage());

        Glide
                .with(context)
                .load(imgUrl)
                .fitCenter()
                .placeholder(R.drawable.placeholder_products)
                .into(bannerImageItemBinding.ivProduct);

/*
        Picasso
                .get()
                .load(imgUrl)
                .placeholder(R.drawable.placeholder_products)
                .fit()
                .centerCrop()
                .into(bannerImageItemBinding.ivProduct);
*/

        container.addView(bannerImageItemBinding.getRoot());
        return bannerImageItemBinding.getRoot();

    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

}
