package com.lalaland.ecommerce.views.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.databinding.ActivityChangePaymentBinding;
import com.lalaland.ecommerce.helpers.AppConstants;

public class ChangePaymentActivity extends AppCompatActivity {


    ActivityChangePaymentBinding activityChangePaymentBinding;
    String totalBill;
    Double totalAmount = 0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityChangePaymentBinding = DataBindingUtil.setContentView(this, R.layout.activity_change_payment);

        totalBill = getIntent().getStringExtra("total_bill");
        totalAmount = Double.parseDouble(totalBill);

        activityChangePaymentBinding.setListener(this);
    }


    public void cashOnDeliverTransfer(View v) {

        if (totalAmount > AppConstants.PAYMENT_LOWEST_LIMIT) {
            showDialoge();
        } else {
            AppConstants.CASH_TRANSFER_TYPE = 1;
            setResult(RESULT_OK);
            finish();
        }

    }

    private void showDialoge() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.cash_on_delivery_dialogue);
        dialog.show();

        dialog.findViewById(R.id.tv_ok).setOnClickListener(v -> {
            dialog.dismiss();
        });
    }

    public void bankTransfer(View v) {

        AppConstants.CASH_TRANSFER_TYPE = 2;
        setResult(RESULT_OK);
        finish();
    }

    public void onlineTransfer(View v) {

        AppConstants.CASH_TRANSFER_TYPE = 3;
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
