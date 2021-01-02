package com.lalaland.ecommerce.views.activities;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.adapters.BankAdapter;
import com.lalaland.ecommerce.adapters.PayProInfoAdapter;
import com.lalaland.ecommerce.data.models.category.PayProData;
import com.lalaland.ecommerce.databinding.ActivityPayProBinding;
import com.lalaland.ecommerce.helpers.AppConstants;

import java.util.ArrayList;
import java.util.List;

public class PayProActivity extends AppCompatActivity {

    private ActivityPayProBinding activityPayProBinding;
    private List<String> bankingTypeList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    private BankAdapter bankAdapter;
    private List<PayProData> payProDataList = new ArrayList<>();

    private PayProInfoAdapter payProInfoAdapter;
    private List<ImageView> dots = new ArrayList<>();
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPayProBinding = DataBindingUtil.setContentView(this, R.layout.activity_pay_pro);
        startTimer();
        initBankAdapter();
        initBankPartnersSpinner();

        payProInfoAdapter = new PayProInfoAdapter(this);
        activityPayProBinding.vpPayproInfo.setAdapter(payProInfoAdapter);

        addDots();
    }

    private void initBankPartnersSpinner() {
        bankingTypeList.add(getString(R.string.select_internet_banking));

        for (PayProData payProData : payProDataList) {
            bankingTypeList.add(payProData.getName());
        }

        activityPayProBinding.spBankPartner.setPrompt(getString(R.string.select_internet_banking));
        adapter = new ArrayAdapter<String>(this, R.layout.spiner_item, bankingTypeList) {

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View mView = super.getDropDownView(position, convertView, parent);
                TextView mTextView = (TextView) mView;
                if (position == 0) {
                    mTextView.setTextColor(Color.GRAY);
                } else {
                    mTextView.setTextColor(Color.BLACK);
                }

                return mView;
            }
        };

        activityPayProBinding.spBankPartner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0)
                    bankAdapter.setData(new ArrayList<>());
                else
                    bankAdapter.setData(payProDataList.get(position - 1).getBankList());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activityPayProBinding.spBankPartner.setAdapter(adapter);
    }

    private void initBankAdapter() {

        setBankList();
        bankAdapter = new BankAdapter(this);
        activityPayProBinding.rvBanks.setLayoutManager(new GridLayoutManager(this, 5));
        bankAdapter.setData(payProDataList.get(0).getBankList());
        activityPayProBinding.rvBanks.setAdapter(bankAdapter);
    }

    private void setBankList() {
        payProDataList = AppConstants.staticBankList;
    }

    private void startTimer() {

        int time = 1;
        int totalTimeCountInMilliseconds = time * 60 * 1000;

        CountDownTimer countDownTimer = new CountDownTimer(totalTimeCountInMilliseconds, 1000) {
            // 100 means, onTick function will be called at every 100
            // milliseconds

            @Override
            public void onTick(long leftTimeInMilliseconds) {
                long seconds = leftTimeInMilliseconds / 1000;
                int progressValue = (int) seconds / 60;
                int finalValue = (progressValue + 1) * 5;
                //progressValue *= progressValue;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    activityPayProBinding.pbTimer.setProgress((progressValue + 1) * 5, true);
                } else {
                    activityPayProBinding.pbTimer.setProgress((progressValue + 1) * 5);
                }

                Log.d("progress_left_time", "" + leftTimeInMilliseconds);
                Log.d("progress_value", "" + progressValue);
                Log.d("progress_value_5", "" + (progressValue + 1) * 5);
                Log.d("progress_min", "" + (seconds / 60));
                Log.d("progress_sec", "" + (seconds % 60));

                activityPayProBinding.tvTimer.setText(String.format("%02d", seconds / 60)
                        + ":" + String.format("%02d", seconds % 60));
            }

            @Override
            public void onFinish() {
                activityPayProBinding.pbTimer.setProgress(0);
            }

        }.start();
    }

    private int getItem(int i) {
        return activityPayProBinding.vpPayproInfo.getCurrentItem() + i;
    }

    public void addDots() {


        for (int i = 0; i < 5; i++) {
            ImageView dot = new ImageView(this);
            dot.setImageDrawable(getResources().getDrawable(R.drawable.empty_intro_circle));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            activityPayProBinding.dots.addView(dot, params);

            dots.add(dot);
        }

        selectDot(0);

        activityPayProBinding.vpPayproInfo.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
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
            int drawableId = (i == idx) ? (R.drawable.filled_circle) : (R.drawable.empty_circle);
            Drawable drawable = res.getDrawable(drawableId);
            dots.get(i).setImageDrawable(drawable);
        }
    }
}