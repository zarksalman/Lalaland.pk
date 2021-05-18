package com.lalaland.ecommerce.views.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.adapters.IntroductionAdapter;
import com.lalaland.ecommerce.databinding.ActivityIntroductionScreenBinding;
import com.lalaland.ecommerce.helpers.AppPreference;

import java.util.ArrayList;
import java.util.List;

import static com.lalaland.ecommerce.helpers.AppConstants.IS_FIRST_TIME;


public class IntroductionScreenActivity extends AppCompatActivity {

    private ActivityIntroductionScreenBinding activityIntroductionScreenBinding;
    private IntroductionAdapter introductionAdapter;
    private List<ImageView> dots = new ArrayList<>();
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityIntroductionScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_introduction_screen);

        introductionAdapter = new IntroductionAdapter(this);
        activityIntroductionScreenBinding.vpIntroduction.setAdapter(introductionAdapter);

        addDots();

        activityIntroductionScreenBinding.tvSkip.setOnClickListener(v -> {
            startNewActivity();
        });

        activityIntroductionScreenBinding.tvNext.setOnClickListener(v -> {

            index = getItem(1);

            if (index >= 5) {
                startNewActivity();
            }

            activityIntroductionScreenBinding.vpIntroduction.setCurrentItem(index, true);
        });
    }

    private int getItem(int i) {
        return activityIntroductionScreenBinding.vpIntroduction.getCurrentItem() + i;
    }

    public void addDots() {


        for (int i = 0; i < 5; i++) {
            ImageView dot = new ImageView(this);
            dot.setImageDrawable(getResources().getDrawable(R.drawable.empty_intro_circle));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            activityIntroductionScreenBinding.dots.addView(dot, params);

            dots.add(dot);
        }

        selectDot(0);

        activityIntroductionScreenBinding.vpIntroduction.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                if (position == activityIntroductionScreenBinding.vpIntroduction.getAdapter().getCount() - 1)
                    activityIntroductionScreenBinding.tvNext.setText("Done");
                else
                    activityIntroductionScreenBinding.tvNext.setText("Next");

                selectDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public void selectDot(int idx) {
        Resources res = getResources();
        for (int i = 0; i < 5; i++) {
            int drawableId = (i == idx) ? (R.drawable.filled_intro_circle) : (R.drawable.empty_intro_circle);
            Drawable drawable = res.getDrawable(drawableId);
            dots.get(i).setImageDrawable(drawable);
        }
    }

    void startNewActivity() {
        AppPreference.getInstance(this).setBoolean(IS_FIRST_TIME, true);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
