package com.lalaland.ecommerce.customBinding;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lalaland.ecommerce.R;

import static com.lalaland.ecommerce.helpers.AppConstants.ACTION_STORAGE_BASE_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.BANNER_STORAGE_BASE_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.PRODUCT_STORAGE_BASE_URL;

public class CustomBinding {

    @BindingAdapter("setProductImage")
    public static void setImageFromServer(ImageView imageView, String imageName) {

        String imageSrc = PRODUCT_STORAGE_BASE_URL.concat(imageName);
        Glide
                .with(imageView.getContext())
                .load(imageSrc)
                .placeholder(R.drawable.place_holder)
                .fitCenter()
                .apply(RequestOptions.centerInsideTransform())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(imageView);
    }

    @BindingAdapter("setActionImage")
    public static void setActionImageFromServer(ImageView imageView, String imageName) {

        String imageSrc = ACTION_STORAGE_BASE_URL.concat(imageName);
        Glide
                .with(imageView.getContext())
                .load(imageSrc)
                .placeholder(R.drawable.btn_bg_round_corner_accent)
                .fitCenter()
                .into(imageView);
    }

    @BindingAdapter("setWeekProductImage")
    public static void setWeekProductImageFromServer(ImageView imageView, String imageName) {

        String imageSrc = PRODUCT_STORAGE_BASE_URL.concat(imageName);
        Glide
                .with(imageView.getContext())
                .load(imageSrc)
                .placeholder(R.drawable.btn_bg_round_corner_accent)
                .fitCenter()
                .into(imageView);
    }

}