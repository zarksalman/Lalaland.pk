package com.lalaland.ecommerce.views.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.adapters.BankAdapter;
import com.lalaland.ecommerce.adapters.PayProInfoAdapter;
import com.lalaland.ecommerce.data.models.category.PayProData;
import com.lalaland.ecommerce.data.models.products.Product;
import com.lalaland.ecommerce.databinding.ActivityPayProBinding;
import com.lalaland.ecommerce.helpers.AppConstants;
import com.lalaland.ecommerce.helpers.AppPreference;
import com.lalaland.ecommerce.helpers.AppUtils;
import com.lalaland.ecommerce.viewModels.order.OrderViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.lalaland.ecommerce.helpers.AppConstants.ORDER_TOTAL;

public class PayProActivity extends AppCompatActivity {

    private List<Product> recommendedProductList = new ArrayList<>();
    private String totalBill = "";
    private String consumerId = "";
    private String orderId = "";

    private ActivityPayProBinding activityPayProBinding;
    private List<String> bankingTypeList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    private BankAdapter bankAdapter;
    private List<PayProData> payProDataList = new ArrayList<>();

    private PayProInfoAdapter payProInfoAdapter;
    private List<ImageView> dots = new ArrayList<>();
    private OrderViewModel orderViewModel;

    private Boolean isApiCalled = false;
    private Boolean shouldCallApi = true;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPayProBinding = DataBindingUtil.setContentView(this, R.layout.activity_pay_pro);

        totalBill = getIntent().getStringExtra(ORDER_TOTAL);
        consumerId = getIntent().getStringExtra(AppConstants.TRANSACTION_ID);
        orderId = getIntent().getStringExtra(AppConstants.ORDER_ID);
        recommendedProductList = getIntent().getParcelableArrayListExtra("recommended_products");

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        setViews();
        initBankAdapter();
        initBankPartnersSpinner();
        startTimer();

        payProInfoAdapter = new PayProInfoAdapter(this);
        activityPayProBinding.vpPayproInfo.setAdapter(payProInfoAdapter);
        addDots();

        new Handler().postDelayed(() -> {
            activityPayProBinding.vpPayproInfo.setVisibility(View.VISIBLE);
            activityPayProBinding.dots.setVisibility(View.VISIBLE);
        }, 1500);

        confirmOrderClick(activityPayProBinding.getRoot(), 0);
    }

    private void setViews() {

        activityPayProBinding.setClickListener(this);
        activityPayProBinding.tvTransactionId.setText(consumerId);
        activityPayProBinding.tvOrderAmount.setText(AppUtils.formatPriceString(String.valueOf(totalBill)));

        activityPayProBinding.tvTitleExpiresIn.requestFocus();
    }

    public void confirmOrderClick(View view, int type) {

        if (isApiCalled)
            return;

        // called from button click then show loading otherwise calling api from bg
        if (type == 1)
            activityPayProBinding.pbLoading.setVisibility(View.VISIBLE);

        isApiCalled = true;

        orderViewModel.checkPayProPaymentStatus(AppPreference.getInstance(this).getString(AppConstants.SIGNIN_TOKEN), orderId).observe(this, basicResponse -> {

            activityPayProBinding.pbLoading.setVisibility(View.GONE);
            isApiCalled = false;

            if (basicResponse != null) {

                if (type == 1)
                    Toast.makeText(this, basicResponse.getMsg(), Toast.LENGTH_SHORT).show();

                if (basicResponse.getCode().equals(AppConstants.SUCCESS_CODE)) {
                    shouldCallApi = false;
                    Intent intent = new Intent(this, OrderReceivedActivity.class);
                    intent.putExtra(ORDER_TOTAL, String.valueOf(totalBill));
                    intent.putParcelableArrayListExtra("recommended_products", (ArrayList<? extends Parcelable>) recommendedProductList);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    shouldCallApi = true;
                }
            }
        });
    }

    public void copyConsumerId(View view) {

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("consumer_id", consumerId);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Consumer Id copied.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        AppConstants.LOAD_HOME_FRAGMENT_INDEX = 0;
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
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

        int time = 20;
        int totalTimeCountInMilliseconds = time * 60 * 1000;

        countDownTimer = new CountDownTimer(totalTimeCountInMilliseconds, 1000) {
            // 100 means, onTick function will be called at every 100
            // milliseconds

            @Override
            public void onTick(long leftTimeInMilliseconds) {
                long seconds = leftTimeInMilliseconds / 1000;
                int progressValue = (int) seconds / 60;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    activityPayProBinding.pbTimer.setProgress((progressValue + 1) * 5, true);
                } else {
                    activityPayProBinding.pbTimer.setProgress((progressValue + 1) * 5);
                }

                if (seconds % 60 == 30 && !isApiCalled && shouldCallApi)
                    confirmOrderClick(activityPayProBinding.getRoot(), 0);

                activityPayProBinding.tvTimer.setText(String.format("%02d", seconds / 60));
            }

            @Override
            public void onFinish() {
                shouldCallApi = false;
                activityPayProBinding.pbTimer.setProgress(0);
            }

        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
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