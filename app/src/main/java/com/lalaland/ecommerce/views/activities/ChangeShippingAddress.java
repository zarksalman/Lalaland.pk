package com.lalaland.ecommerce.views.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.adapters.AddressAdapter;
import com.lalaland.ecommerce.data.models.userAddressBook.UserAddresses;
import com.lalaland.ecommerce.databinding.ActivityChangeShippingAddressBinding;
import com.lalaland.ecommerce.helpers.AppConstants;
import com.lalaland.ecommerce.helpers.AppPreference;
import com.lalaland.ecommerce.viewModels.user.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class ChangeShippingAddress extends AppCompatActivity {

    ActivityChangeShippingAddressBinding activityChangeShippingAddressBinding;
    AddressAdapter addressAdapter;
    UserViewModel userViewModel;
    String token;
    List<UserAddresses> addressesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityChangeShippingAddressBinding = DataBindingUtil.setContentView(this, R.layout.activity_change_shipping_address);
        activityChangeShippingAddressBinding.setListener(this);

        token = AppPreference.getInstance(this).getString(AppConstants.SIGNIN_TOKEN);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        getAddresses();
    }

    private void getAddresses() {

        userViewModel.getAddresses(token).observe(this, addressDataContainer -> {

            if (addressDataContainer != null) {
                addressesList = addressDataContainer.getData().getUserAddress();
                setAdapter();
                activityChangeShippingAddressBinding.pbLoading.setVisibility(View.GONE);
            }
        });
    }


    private void setAdapter() {

        addressAdapter = new AddressAdapter(this, userAddresses -> {
        });

        activityChangeShippingAddressBinding.rvAddress.setAdapter(addressAdapter);
        activityChangeShippingAddressBinding.rvAddress.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        activityChangeShippingAddressBinding.rvAddress.setHasFixedSize(true);
        addressAdapter.setData(addressesList);

    }


    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
