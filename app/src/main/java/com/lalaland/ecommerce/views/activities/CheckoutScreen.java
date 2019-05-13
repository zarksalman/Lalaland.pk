package com.lalaland.ecommerce.views.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.data.models.userDetails.UserAddresses;
import com.lalaland.ecommerce.databinding.ActivityCheckoutScreenBinding;
import com.lalaland.ecommerce.helpers.AppConstants;

import static com.lalaland.ecommerce.helpers.AppConstants.CASH_TRANSFER_TYPE;

public class CheckoutScreen extends AppCompatActivity {

    private ActivityCheckoutScreenBinding activityCheckoutScreenBinding;
    private UserAddresses userAddresses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCheckoutScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_checkout_screen);

        userAddresses = AppConstants.userAddresses;
//        userAddresses = (UserAddresses) getIntent().getSerializableExtra("address");


        if (userAddresses == null) {
            activityCheckoutScreenBinding.addUserAddress.setVisibility(View.VISIBLE);
            activityCheckoutScreenBinding.userDetail.setVisibility(View.GONE);
        } else {

            activityCheckoutScreenBinding.tvUserName.setText(userAddresses.getUserName());
            activityCheckoutScreenBinding.tvUserAddress.setText(userAddresses.getShippingAddress());
            activityCheckoutScreenBinding.tvUserCityPostalCode.setText(String.valueOf(userAddresses.getPostalCode()));
            activityCheckoutScreenBinding.tvUserMobile.setText(userAddresses.getPhone());

            activityCheckoutScreenBinding.addUserAddress.setVisibility(View.GONE);
            activityCheckoutScreenBinding.userDetail.setVisibility(View.VISIBLE);
        }

        setListeners();
    }

    void setListeners() {

        activityCheckoutScreenBinding.rgPaymentType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (R.id.rb_cash_on_delivery == checkedId)
                    CASH_TRANSFER_TYPE = 1;
                else
                    CASH_TRANSFER_TYPE = 2;
            }
        });
    }

    public void addNewAddress(View view) {

    }

}
