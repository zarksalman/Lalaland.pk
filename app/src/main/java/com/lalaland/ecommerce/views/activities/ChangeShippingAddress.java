package com.lalaland.ecommerce.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChangeShippingAddress extends AppCompatActivity {

    ActivityChangeShippingAddressBinding activityChangeShippingAddressBinding;
    AddressAdapter addressAdapter;
    UserViewModel userViewModel;
    String token;
    List<UserAddresses> addressesList = new ArrayList<>();
    Map<String, String> parameter = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityChangeShippingAddressBinding = DataBindingUtil.setContentView(this, R.layout.activity_change_shipping_address);
        activityChangeShippingAddressBinding.setListener(this);

        token = AppPreference.getInstance(this).getString(AppConstants.SIGNIN_TOKEN);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        getAddresses();

        activityChangeShippingAddressBinding.ivBackArrow.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void getAddresses() {

        userViewModel.getAddresses(token).observe(this, addressDataContainer -> {

            if (addressDataContainer != null) {

                if (addressDataContainer.getData().getUserAddress().size() > 0) {
                    addressesList = addressDataContainer.getData().getUserAddress();
                    setAdapter();
                    activityChangeShippingAddressBinding.rvAddress.setVisibility(View.VISIBLE);
                    activityChangeShippingAddressBinding.pbLoading.setVisibility(View.GONE);
                } else {
                    activityChangeShippingAddressBinding.ivEmptyState.setVisibility(View.VISIBLE);
                }

            }
        });
    }


    private void setAdapter() {


        addressAdapter = new AddressAdapter(this, new AddressAdapter.AddressListener() {

            @Override
            public void onAddressClicked(UserAddresses userAddresses) {

                if (userAddresses.getIsPrimary() == 1) {
                    setResult(RESULT_CANCELED);
                    finish();
                }

                activityChangeShippingAddressBinding.pbLoading.setVisibility(View.VISIBLE);
                String[] fullName;

                fullName = userAddresses.getUserNameAddress().split(" ");

                parameter.clear();

                parameter.put("address_id", String.valueOf(userAddresses.getAddressId()));
                parameter.put("physical_address", String.valueOf(userAddresses.getShippingAddress()));
                parameter.put("shipping_address", String.valueOf(userAddresses.getShippingAddress()));
                parameter.put("city_id", String.valueOf(userAddresses.getCityId()));
                parameter.put("postal_code", String.valueOf(userAddresses.getPostalCode()));
                parameter.put("is_primary", String.valueOf(1));
                parameter.put("first_name", fullName[0]);
                parameter.put("last_name", fullName[1]);

                userViewModel.editAddress(token, parameter).observe(ChangeShippingAddress.this, addressDataContainer -> {

                    if (addressDataContainer != null) {

                        for (int i = 0; i < addressDataContainer.getData().getUserAddress().size(); i++) {

                            if (addressDataContainer.getData().getUserAddress().get(i).getIsPrimary() == 1) {
                                AppConstants.userAddresses = addressDataContainer.getData().getUserAddress().get(i);

                                setResult(RESULT_OK);
                                finish();

                                break;
                            }
                        }
                    }
                });

            }

            @Override
            public void onEditAddressClicked(UserAddresses userAddresses) {

                Intent intent = new Intent(ChangeShippingAddress.this, AddressCreationActivity.class);
                intent.putExtra("user_address", userAddresses);
                intent.putExtra("is_edit_address", true);
                startActivityForResult(intent, 2);
            }
        });

        activityChangeShippingAddressBinding.rvAddress.setAdapter(addressAdapter);
        activityChangeShippingAddressBinding.rvAddress.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        activityChangeShippingAddressBinding.rvAddress.setHasFixedSize(true);
        addressAdapter.setData(addressesList);

    }

    public void addNewAddress(View view) {
        startActivityForResult(new Intent(this, AddressCreationActivity.class), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 1 || requestCode == 2) {

                activityChangeShippingAddressBinding.rvAddress.setVisibility(View.GONE);
                activityChangeShippingAddressBinding.pbLoading.setVisibility(View.VISIBLE);
                getAddresses();
            }
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
