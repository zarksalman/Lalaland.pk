package com.lalaland.ecommerce.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.data.models.productDetails.ProductMultimedium;
import com.lalaland.ecommerce.databinding.ProductImageItemBinding;
import com.lalaland.ecommerce.helpers.AppConstants;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductImageAdapter extends PagerAdapter {


    private ProductImageItemBinding productImageItemBinding;
    private List<ProductMultimedium> productMultimedia;
    private String mediaDescription;


    Context context;
    LayoutInflater layoutInflater;

    public ProductImageAdapter(Context context, List<ProductMultimedium> mProductMultimedia, String mMediaDescription) {
        this.context = context;
        productMultimedia = mProductMultimedia;
        mediaDescription = mMediaDescription;
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

        productImageItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.product_image_item, container, false);

        String imgUrl = AppConstants.PRODUCT_STORAGE_BASE_URL.concat(productMultimedia.get(position).getSource());

        Picasso
                .get()
                .load(imgUrl)
                .placeholder(R.drawable.placeholder_products)
                .into(productImageItemBinding.ivProduct);

        productImageItemBinding.tvImageDetail.setText(mediaDescription);
        if (mediaDescription == null || mediaDescription.isEmpty())
            productImageItemBinding.tvImageDetail.setVisibility(View.GONE);
        else
            productImageItemBinding.tvImageDetail.setVisibility(View.VISIBLE);

        container.addView(productImageItemBinding.getRoot());
        return productImageItemBinding.getRoot();

    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }
}
