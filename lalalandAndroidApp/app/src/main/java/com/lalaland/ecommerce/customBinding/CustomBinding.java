package com.lalaland.ecommerce.customBinding;

import android.graphics.Color;
import android.graphics.Paint;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.helpers.AppConstants;
import com.lalaland.ecommerce.helpers.AppPreference;

import static com.lalaland.ecommerce.helpers.AppConstants.ADVERTISEMENT_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.ADVERTISEMENT_URL_KEY;
import static com.lalaland.ecommerce.helpers.AppConstants.BRAND_FOCUS_STORAGE_BASE_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.BRAND_FOCUS_STORAGE_BASE_URL_KEY;
import static com.lalaland.ecommerce.helpers.AppConstants.BRAND_STORAGE_BASE_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.BRAND_STORAGE_BASE_URL_KEY;
import static com.lalaland.ecommerce.helpers.AppConstants.CATEGORY_FOCUS_STORAGE_BASE_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.CATEGORY_FOCUS_STORAGE_BASE_URL_KEY;
import static com.lalaland.ecommerce.helpers.AppConstants.CUSTOM_PRODUCT_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.CUSTOM_PRODUCT_URL_KEY;
import static com.lalaland.ecommerce.helpers.AppConstants.MEDIUM_PRODUCT_STORAGE_BASE_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.MEDIUM_PRODUCT_STORAGE_BASE_URL_KEY;
import static com.lalaland.ecommerce.helpers.AppConstants.PRODUCT_STORAGE_BASE_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.PRODUCT_STORAGE_BASE_URL_KEY;
import static com.lalaland.ecommerce.helpers.AppConstants.USER_STORAGE_BASE_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.USER_STORAGE_BASE_URL_KEY;

public class CustomBinding {

    @BindingAdapter("setProductImage")
    public static void setImageFromServer(ImageView imageView, String imageName) {

        PRODUCT_STORAGE_BASE_URL = AppPreference.getInstance(imageView.getContext()).getString(PRODUCT_STORAGE_BASE_URL_KEY);

        String imageSrc = PRODUCT_STORAGE_BASE_URL.concat(imageName);
        Glide
                .with(AppConstants.mContext)
                .load(imageSrc)
                .fitCenter()
                .error(R.drawable.placeholder_products)
                .placeholder(R.drawable.placeholder_products)
                .into(imageView);
    }

    @BindingAdapter(value = {"url", "colorPatch"}, requireAll = false)
    public static void setLinkedProductImage(ImageView imageView, String imageName, String colorPatch) {

        PRODUCT_STORAGE_BASE_URL = AppPreference.getInstance(imageView.getContext()).getString(PRODUCT_STORAGE_BASE_URL_KEY);

        String imageSrc;

        if (colorPatch == null || colorPatch.isEmpty())
            imageSrc = PRODUCT_STORAGE_BASE_URL.concat(imageName);
        else
            imageSrc = AppConstants.COLOR_PATCH_URL.concat(colorPatch);

        Glide
                .with(AppConstants.mContext)
                .load(imageSrc)
                .fitCenter()
                .error(R.drawable.placeholder_products)
                .placeholder(R.drawable.placeholder_products)
                .into(imageView);
    }

    @BindingAdapter("setMediumImageFromServer")
    public static void setMediumImageFromServer(ImageView imageView, String imageName) {

        PRODUCT_STORAGE_BASE_URL = AppPreference.getInstance(imageView.getContext()).getString(PRODUCT_STORAGE_BASE_URL_KEY);

        Log.d("product_url", PRODUCT_STORAGE_BASE_URL);
        Log.d("recomended_images", imageName);

        try {

            String imageSrc = PRODUCT_STORAGE_BASE_URL.concat(imageName);
            Glide
                    .with(AppConstants.mContext)
                    .load(imageSrc)
                    .fitCenter()
                    .error(R.drawable.placeholder_products)
                    .placeholder(R.drawable.placeholder_products)
                    .into(imageView);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @BindingAdapter("setActionImage")
    public static void setActionImageFromServer(ImageView imageView, String imageName) {

        Glide
                .with(imageView.getContext())
                .load(imageName)
                .placeholder(R.drawable.placeholder_products_pick_of_week)
                .error(R.drawable.placeholder_products_pick_of_week)
                .centerInside()
                .into(imageView);
    }

    @BindingAdapter("setDrawableImage")
    public static void setDrawableImage(ImageView imageView, int drawable) {
        Glide
                .with(imageView.getContext())
                .load(drawable)
                .centerInside()
                .into(imageView);
    }

    @BindingAdapter("setWeekProductImage")
    public static void setWeekProductImageFromServer(ImageView imageView, String imageName) {

        MEDIUM_PRODUCT_STORAGE_BASE_URL = AppPreference.getInstance(imageView.getContext()).getString(MEDIUM_PRODUCT_STORAGE_BASE_URL_KEY);

        try {

            String imageSrc = MEDIUM_PRODUCT_STORAGE_BASE_URL.concat(imageName);
            Glide
                    .with(imageView.getContext())
                    .load(imageSrc)
                    .fitCenter()
                    .placeholder(R.drawable.placeholder_products_pick_of_week)
                    .error(R.drawable.placeholder_products_pick_of_week)
                    .into(imageView);
        } catch (OutOfMemoryError outOfMemoryError) {
            outOfMemoryError.printStackTrace();
        }
    }


    @BindingAdapter("setBrandFocusImage")
    public static void setBrandFocusImageFromServer(ImageView imageView, String imageName) {

        BRAND_FOCUS_STORAGE_BASE_URL = AppPreference.getInstance(imageView.getContext()).getString(BRAND_FOCUS_STORAGE_BASE_URL_KEY);

        String imageSrc = BRAND_FOCUS_STORAGE_BASE_URL.concat(imageName);
        Glide
                .with(imageView.getContext())
                .load(imageSrc)
                .fitCenter()
                .error(R.drawable.placeholder_products)
                .placeholder(R.drawable.placeholder_products)
                .into(imageView);
    }

    @BindingAdapter("setGetTheLookImage")
    public static void setGetTheLookImage(ImageView imageView, String imageName) {

        CUSTOM_PRODUCT_URL = AppPreference.getInstance(imageView.getContext()).getString(CUSTOM_PRODUCT_URL_KEY);

        String imageSrc = CUSTOM_PRODUCT_URL.concat(imageName);
        Glide
                .with(imageView.getContext())
                .load(imageSrc)
                .fitCenter()
                .error(R.drawable.placeholder_products)
                .placeholder(R.drawable.placeholder_products)
                .into(imageView);
    }

    @BindingAdapter("setCategoryBrandImage")
    public static void setCategoryBrandImage(ImageView imageView, String logoUrl) {

        BRAND_STORAGE_BASE_URL = AppPreference.getInstance(imageView.getContext()).getString(BRAND_STORAGE_BASE_URL_KEY);

        String imageSrc = BRAND_STORAGE_BASE_URL.concat(logoUrl);
        Glide
                .with(imageView.getContext())
                .load(imageSrc)
                .placeholder(R.drawable.placeholder_products_bigger_images)
                .error(R.drawable.placeholder_products_bigger_images)
                .fitCenter()
                .into(imageView);
    }

    @BindingAdapter("setFeatureCategoryImage")
    public static void setFeatureCategoryImage(ImageView imageView, String imageName) {

        CATEGORY_FOCUS_STORAGE_BASE_URL = AppPreference.getInstance(imageView.getContext()).getString(CATEGORY_FOCUS_STORAGE_BASE_URL_KEY);

        String imageSrc = CATEGORY_FOCUS_STORAGE_BASE_URL.concat(imageName);
        Glide
                .with(AppConstants.mContext)
                .load(imageSrc)
                .fitCenter()
                .error(R.drawable.placeholder_products)
                .placeholder(R.drawable.placeholder_products)
                .into(imageView);
    }

    @BindingAdapter("setBlogsImage")
    public static void setBlogsImage(ImageView imageView, String imageName) {

        Glide
                .with(imageView.getContext())
                .load(imageName)
                .placeholder(R.drawable.placeholder_products)
                .error(R.drawable.placeholder_products)
                .fitCenter()
                .into(imageView);
    }

    @BindingAdapter("setAdvertisementImage")
    public static void setAdvertisementImage(ImageView imageView, String imageName) {

        ADVERTISEMENT_URL = AppPreference.getInstance(imageView.getContext()).getString(ADVERTISEMENT_URL_KEY);

        String imageSrc = ADVERTISEMENT_URL.concat(imageName);
        Glide
                .with(imageView.getContext())
                .load(imageSrc)
                .placeholder(R.drawable.placeholder_products)
                .error(R.drawable.placeholder_products)
                .centerInside()
                .into(imageView);
    }

    @BindingAdapter("setUserImage")
    public static void setUserImage(ImageView imageView, String imageName) {

        USER_STORAGE_BASE_URL = AppPreference.getInstance(imageView.getContext()).getString(USER_STORAGE_BASE_URL_KEY);
        String imageSrc = USER_STORAGE_BASE_URL.concat(imageName);
        Glide
                .with(imageView.getContext())
                .load(imageSrc)
                .placeholder(R.drawable.placeholder_products)
                .error(R.drawable.placeholder_products)
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

    @BindingAdapter("setUnderline")
    public static void setUnderline(TextView textView, String text) {
        if (text == null) return;

        String moreSizes = "(More Size: ";
        String productString = moreSizes + text + ")";
        SpannableString content = new SpannableString(productString);
        content.setSpan(new UnderlineSpan(), moreSizes.length() - 1, content.length() - 1, 0);
        content.setSpan(new ForegroundColorSpan(Color.BLUE), 0, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(content);
    }
}
