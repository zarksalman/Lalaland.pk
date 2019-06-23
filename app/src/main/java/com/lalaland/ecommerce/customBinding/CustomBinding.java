package com.lalaland.ecommerce.customBinding;

import android.graphics.Color;
import android.graphics.Paint;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.helpers.AppConstants;

import static com.lalaland.ecommerce.helpers.AppConstants.ACTION_STORAGE_BASE_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.BRAND_FOCUS_STORAGE_BASE_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.CATEGORY_BRAND_STORAGE_BASE_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.PRODUCT_STORAGE_BASE_URL;

public class CustomBinding {

    @BindingAdapter("setProductImage")
    public static void setImageFromServer(ImageView imageView, String imageName) {

        String imageSrc = PRODUCT_STORAGE_BASE_URL.concat(imageName);
        Glide
                .with(AppConstants.mContext)
                .load(imageSrc)
                .placeholder(R.drawable.placeholder_products)
                .into(imageView);
    }

    @BindingAdapter("setMediumImageFromServer")
    public static void setMediumImageFromServer(ImageView imageView, String imageName) {

        String imageSrc = PRODUCT_STORAGE_BASE_URL.concat(imageName);
        Glide
                .with(AppConstants.mContext)
                .load(imageSrc)
                .placeholder(R.drawable.placeholder_products)
                .into(imageView);
    }

    @BindingAdapter("setActionImage")
    public static void setActionImageFromServer(ImageView imageView, String imageName) {

        String imageSrc = ACTION_STORAGE_BASE_URL.concat(imageName);
        Glide
                .with(imageView.getContext())
                .load(imageSrc)
                .placeholder(R.drawable.placeholder_products)
                .fitCenter()
                .into(imageView);
    }

    @BindingAdapter("setWeekProductImage")
    public static void setWeekProductImageFromServer(ImageView imageView, String imageName) {

        String imageSrc = PRODUCT_STORAGE_BASE_URL.concat("medium/").concat(imageName);
        Glide
                .with(imageView.getContext())
                .load(imageSrc)
                .fitCenter()
                .placeholder(R.drawable.placeholder_products)
                .into(imageView);
    }


    @BindingAdapter("setBrandFocusImage")
    public static void setBrandFocusImageFromServer(ImageView imageView, String imageName) {

        String imageSrc = BRAND_FOCUS_STORAGE_BASE_URL.concat(imageName);
        Glide
                .with(imageView.getContext())
                .load(imageSrc)
                .centerCrop()
                .placeholder(R.drawable.placeholder_products)
                .into(imageView);
    }

    @BindingAdapter("setCategoryBrandImage")
    public static void setCategoryBrandImage(ImageView imageView, String logoUrl) {

        String imageSrc = CATEGORY_BRAND_STORAGE_BASE_URL.concat(logoUrl);
        Glide
                .with(imageView.getContext())
                .load(imageSrc)
                .placeholder(R.drawable.placeholder_products)
                .fitCenter()
                .into(imageView);
    }

    @BindingAdapter("setCartItemStatus")
    public static void setCartItemsStatus(CheckBox checkBox, int status) {

        /*
         * 1 =  added to cart
         * 2 =  ordered
         * 3 =  ready for checkout
         * */

        if (status == 1)
            checkBox.setChecked(false);
        else if (status == 3)
            checkBox.setChecked(true);

    }

    @BindingAdapter("setFilterColor")
    public static void setFilterColor(ImageView imageView, String filterValue) {

        Float percentFactor = 2.55f;

        if (filterValue.toLowerCase().contains("multi")) {
            imageView.setImageDrawable(AppConstants.mContext.getResources().getDrawable(R.drawable.muli_color_icon));
        } else if (filterValue.toLowerCase().contains("#")) {
            imageView.setBackgroundColor(Color.parseColor(filterValue));
        } else if (filterValue.toLowerCase().contains("rgb")) {


            filterValue = filterValue.replace("rgb(", "");
            filterValue = filterValue.replace(")", "");

            if (filterValue.contains("%")) {

                filterValue = filterValue.replace("%", "");
                percentFactor = 2.55f;
            }

            String[] colorRgb = filterValue.split(",");
            Integer[] rgbColor = new Integer[3];

            rgbColor[0] = Math.round(Float.parseFloat(colorRgb[0]) * percentFactor);
            rgbColor[1] = Math.round(Float.parseFloat(colorRgb[1]) * percentFactor);
            rgbColor[2] = Math.round(Float.parseFloat(colorRgb[2]) * percentFactor);

            imageView.setBackgroundColor(Color.rgb(rgbColor[0], rgbColor[1], rgbColor[2]));
        } else {
            imageView.setBackgroundColor(Color.rgb(255, 255, 255));
        }
    }

    @BindingAdapter("setStrike")
    public static void setStrike(TextView text, String actualPrice) {
        text.setPaintFlags(text.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }
}
