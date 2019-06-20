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
import com.lalaland.ecommerce.data.models.productDetails.ProductMultimedium;
import com.lalaland.ecommerce.databinding.ProductImageItemBinding;
import com.lalaland.ecommerce.helpers.AppConstants;

import java.util.ArrayList;
import java.util.List;

public class ProductImageAdapter extends PagerAdapter {


    private ProductImageItemBinding productImageItemBinding;
    List<ProductMultimedium> productMultimedia = new ArrayList<>();


    Context context;
    LayoutInflater layoutInflater;

    public ProductImageAdapter(Context context, List<ProductMultimedium> mProductMultimedia) {
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

        productImageItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.product_image_item, container, false);

        String imgUrl = AppConstants.PRODUCT_STORAGE_BASE_URL.concat(productMultimedia.get(position).getSource());

        Glide.with(context)
                .load(imgUrl)
                .placeholder(R.drawable.placeholder_products)
                .into(productImageItemBinding.ivProduct);
        
        container.addView(productImageItemBinding.getRoot());
        return productImageItemBinding.getRoot();

    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

}
