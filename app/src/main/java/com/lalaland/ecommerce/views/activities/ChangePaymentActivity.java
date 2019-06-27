package com.lalaland.ecommerce.views.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.databinding.ActivityChangePaymentBinding;
import com.lalaland.ecommerce.helpers.AppConstants;

public class ChangePaymentActivity extends AppCompatActivity {


    ActivityChangePaymentBinding activityChangePaymentBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityChangePaymentBinding = DataBindingUtil.setContentView(this, R.layout.activity_change_payment);

        activityChangePaymentBinding.setListener(this);
    }


    public void cashOnDeliverTransfer(View v) {

        AppConstants.CASH_TRANSFER_TYPE = 1;
        setResult(RESULT_OK);
        finish();
    }
    
    public void bankTransfer(View v) {

        AppConstants.CASH_TRANSFER_TYPE = 2;
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
