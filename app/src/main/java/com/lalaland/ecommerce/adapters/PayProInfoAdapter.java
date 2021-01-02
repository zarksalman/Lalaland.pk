package com.lalaland.ecommerce.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.lalaland.ecommerce.R;

public class PayProInfoAdapter extends PagerAdapter {

    int[] image_resource = {R.drawable.paypro_1,
            R.drawable.paypro_2,
            R.drawable.paypro_3,
            R.drawable.paypro_4,
            R.drawable.paypro_5,
            R.drawable.paypro_6,
    };

    String[] introductionContent = {

            "Receive PayPro/ConnectPay ID\nvia sms and email.",
            "Login to your Internet Banking/\nMobile App.",
            "Select PayPro/ConnectPay from\nthe bill payment option.",
            "Enter PayPro/ConnectPay\nID.",
            "Make payment after \nconfirmation.",
            "Receive Payments Notification via \nSMS and Email instantly."
    };

    Context context;
    LayoutInflater layoutInflater;

    public PayProInfoAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return image_resource.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {

        return view == (ConstraintLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.paypro_item, container, false);
        ImageView imageView = view.findViewById(R.id.iv_paypro);
        TextView textView = view.findViewById(R.id.tv_info);

        Glide.with(context)
                .load(image_resource[position])
                .placeholder(R.drawable.placeholder_products)
                .into(imageView);

        textView.setText(introductionContent[position]);

        container.addView(view);
        return view;

    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

}
