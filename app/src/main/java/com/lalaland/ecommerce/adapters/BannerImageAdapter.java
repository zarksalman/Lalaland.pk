package com.lalaland.ecommerce.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.lalaland.ecommerce.views.activities.ActionProductListingActivity;
import com.lalaland.ecommerce.views.activities.WebViewActivity;

import java.util.ArrayList;
import java.util.List;

import static com.lalaland.ecommerce.helpers.AppConstants.ACTION_ID;
import static com.lalaland.ecommerce.helpers.AppConstants.ACTION_NAME;
import static com.lalaland.ecommerce.helpers.AppConstants.BLOG_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.PRODUCT_TYPE;

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

        productImageItemBinding.ivSlider.setOnClickListener(v -> {

            Intent mIntent;

            if (productMultimedia.get(position).getMobileAppUrl() != null) {

                //this will log the page number that was click
                if (productMultimedia.get(position).getMobileAppUrl().getActionName().toLowerCase().equals("other")) {

                    mIntent = new Intent(context, WebViewActivity.class);
                    // if we are not going to display listing then open webview
                    mIntent.putExtra(BLOG_URL, productMultimedia.get(position).getMobileAppUrl().getTitle());
                    context.startActivity(mIntent);
                }
            } else {


                if (productMultimedia.get(position).getMobileAppUrl() != null) {

                    mIntent = new Intent(context, ActionProductListingActivity.class);
                    mIntent.putExtra(ACTION_NAME, productMultimedia.get(position).getMobileAppUrl().getTitle());
                    mIntent.putExtra(ACTION_ID, String.valueOf(productMultimedia.get(position).getMobileAppUrl().getId()));
                    mIntent.putExtra(PRODUCT_TYPE, productMultimedia.get(position).getMobileAppUrl().getActionName());
                    context.startActivity(mIntent);
                }
            }
        });

        /*
        Glide.with(context)
                .load(imgUrl)
                .placeholder(R.drawable.placeholder_products)
                .into(productImageItemBinding.ivProduct);
*/

        Glide
                .with(context)
                .load(imgUrl)
                .fitCenter()
                .placeholder(R.drawable.placeholder_products_bigger_images)
                .error(R.drawable.placeholder_products_bigger_images)
                .into(productImageItemBinding.ivSlider);


        container.addView(productImageItemBinding.getRoot());
        return productImageItemBinding.getRoot();

    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

}
