package com.lalaland.ecommerce.customBinding;

import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.helpers.AppConstants;

import static com.lalaland.ecommerce.helpers.AppConstants.ACTION_STORAGE_BASE_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.BRAND_FOCUS_STORAGE_BASE_URL;
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

        String imageSrc = PRODUCT_STORAGE_BASE_URL.concat(imageName);
        Glide
                .with(imageView.getContext())
                .load(imageSrc)
                .placeholder(R.drawable.placeholder_products)
                .fitCenter()
                .into(imageView);
    }


    @BindingAdapter("setBrandFocusImage")
    public static void setBrandFocusImageFromServer(ImageView imageView, String imageName) {

        String imageSrc = BRAND_FOCUS_STORAGE_BASE_URL.concat(imageName);
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

    @BindingAdapter("setFitAndSize")
    public static void setFitAndSize(TextView tvFitAndSize, String html) {


        /*tvFitAndSize.setMovementMethod(LinkMovementMethod.getInstance());
        tvFitAndSize.setText(AppUtils.stripHtml(html));
*/
     /*   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvFitAndSize.setText(AppUtils.stripHtml(html));
        } else {
            tvFitAndSize.setText(Html.fromHtml(html));
        }*/
    }
}
