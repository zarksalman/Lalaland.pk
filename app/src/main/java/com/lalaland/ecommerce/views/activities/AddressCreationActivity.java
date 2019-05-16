package com.lalaland.ecommerce.views.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.data.models.userAddressBook.UserAddresses;
import com.lalaland.ecommerce.databinding.ActivityAddressCreationBinding;
import com.lalaland.ecommerce.helpers.AppConstants;
import com.lalaland.ecommerce.helpers.AppPreference;
import com.lalaland.ecommerce.viewModels.user.UserViewModel;

import java.util.HashMap;
import java.util.Map;

import static com.lalaland.ecommerce.helpers.AppConstants.CONFIRM_TYPE;
import static com.lalaland.ecommerce.helpers.AppConstants.GENERAL_ERROR;
import static com.lalaland.ecommerce.helpers.AppConstants.SIGNIN_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;
import static com.lalaland.ecommerce.helpers.AppConstants.TYPE;
import static com.lalaland.ecommerce.helpers.AppConstants.VALIDATION_FAIL_CODE;

public class AddressCreationActivity extends AppCompatActivity {

    private ActivityAddressCreationBinding activityAddressCreationBinding;
    private UserAddresses userAddresses;
    private UserViewModel userViewModel;
    private AppPreference appPreference;
    private String token;
    private String first_name;
    private String last_name;
    private String postal_address;
    private String postal_code;
    private String city;
    private boolean is_primary = false;
    private Map<String, String> parameter = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityAddressCreationBinding = DataBindingUtil.setContentView(this, R.layout.activity_address_creation);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        appPreference = AppPreference.getInstance(this);
        token = appPreference.getString(SIGNIN_TOKEN);

        userAddresses = new UserAddresses();
        activityAddressCreationBinding.setClickListener(this);
    }

    public void cancel(View view) {

        onBackPressed();
    }

    public void save(View view) {


        activityAddressCreationBinding.pbLoading.setVisibility(View.VISIBLE);

        if (
                validateNames(TYPE)
                        && validateNames(CONFIRM_TYPE)
                        && validateAddress(1)
                        && validateAddress(2)
                        && validateAddress(3)) {

            is_primary = activityAddressCreationBinding.cbDefault.isChecked();

            parameter.put("first_name", first_name);
            parameter.put("last_name", last_name);
            parameter.put("physical_address", postal_address);
            parameter.put("shipping_address", postal_address);
            parameter.put("city_id", city);
            parameter.put("postal_code", postal_code);


            if (is_primary) {
                parameter.put("is_primary", "1");
                userAddresses.setIsPrimary(1);
            } else {
                parameter.put("is_primary", "0");
                userAddresses.setIsPrimary(0);
            }

            userViewModel.addNewAddress(token, parameter).observe(this, addressDataContainer -> {

                if (addressDataContainer != null) {

                    if (addressDataContainer.getCode().equals(SUCCESS_CODE)) {

                        AppConstants.userAddresses = addressDataContainer.getData().getUserAddress().get(0);
                        Log.d(AppConstants.TAG, addressDataContainer.getMsg());
                        Toast.makeText(this, addressDataContainer.getMsg(), Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(this, CheckoutScreen.class));
                        finish();
                    } else if (addressDataContainer.getCode().equals(VALIDATION_FAIL_CODE)) {

                        Log.d(AppConstants.TAG, addressDataContainer.getMsg());
                        Toast.makeText(this, addressDataContainer.getMsg(), Toast.LENGTH_SHORT).show();
                    } else {

                        Log.d(AppConstants.TAG, GENERAL_ERROR);
                        Toast.makeText(this, GENERAL_ERROR, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d(AppConstants.TAG, GENERAL_ERROR);
                    Toast.makeText(this, GENERAL_ERROR, Toast.LENGTH_SHORT).show();
                }
            });
        }

        activityAddressCreationBinding.pbLoading.setVisibility(View.GONE);
    }

    private boolean validateNames(int type) {

        first_name = activityAddressCreationBinding.etFirstName.getText().toString().trim();
        last_name = activityAddressCreationBinding.etLastName.getText().toString().trim();

        if (type == TYPE) {

            if (first_name.isEmpty()) {
                activityAddressCreationBinding.tiFirstName.setError("First name could not be empty");
            } else {
                activityAddressCreationBinding.tiFirstName.setError(null);
                return true;
            }

        } else if (type == CONFIRM_TYPE) {

            if (last_name.isEmpty()) {
                activityAddressCreationBinding.tiLastName.setError("Last name could not be empty");
            } else {
                activityAddressCreationBinding.tiLastName.setError(null);
                return true;
            }
        }
        return false;  // default return false
    }

    private boolean validateAddress(int type) {

        if (type == 1) {

            postal_address = activityAddressCreationBinding.etAddress.getText().toString().trim();

            if (postal_address.isEmpty()) {
                activityAddressCreationBinding.tiAddress.setError("Address could not be empty");
                return false;
            } else
                activityAddressCreationBinding.tiAddress.setError(null);
        } else if (type == 2) {
            postal_code = activityAddressCreationBinding.etPostalCode.getText().toString().trim();

            if (postal_address.isEmpty()) {
                activityAddressCreationBinding.tiPostalCode.setError("Postal code could not be empty");
                return false;
            } else
                activityAddressCreationBinding.tiPostalCode.setError(null);
        } else if (type == 3) {

            city = activityAddressCreationBinding.etCity.getText().toString().trim();
            if (postal_address.isEmpty()) {
                activityAddressCreationBinding.tiCity.setError("City could not be empty");
                return false;
            } else
                activityAddressCreationBinding.tiCity.setError(null);
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
