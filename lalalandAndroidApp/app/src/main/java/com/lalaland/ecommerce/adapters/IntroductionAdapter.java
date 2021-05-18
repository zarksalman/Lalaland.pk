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

public class IntroductionAdapter extends PagerAdapter {

    int[] image_resource = {R.drawable.intro_1,
            R.drawable.intro_2,
            R.drawable.intro_3,
            R.drawable.intro_4,
            R.drawable.intro_5
    };

    String[] introductionContent = {

            "New Look.",
            "App Exclusive.",
            "Exciting Categories.",
            "Just Choose.",
            "Cool Community."
    };

    String[] headings = {

            "And shop what you want.",
            "Enjoy app exclusive deal.",
            "Cool items added daily.",
            "Easily view and buy your favorites.",
            "Latest gear and best reviews."
    };

    Context context;
    LayoutInflater layoutInflater;

    public IntroductionAdapter(Context context) {
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
        View view = layoutInflater.inflate(R.layout.introduction_item, container, false);
        ImageView imageView = view.findViewById(R.id.iv_introdution);
        TextView textView = view.findViewById(R.id.tv_title);
        TextView headingTextView = view.findViewById(R.id.tv_info);


        headingTextView.setText(headings[position]);

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
