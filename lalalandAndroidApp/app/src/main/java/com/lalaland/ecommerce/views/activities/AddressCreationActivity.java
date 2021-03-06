package com.lalaland.ecommerce.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.data.models.userAddressBook.UserAddresses;
import com.lalaland.ecommerce.databinding.ActivityAddressCreationBinding;
import com.lalaland.ecommerce.helpers.AppConstants;
import com.lalaland.ecommerce.helpers.AppPreference;
import com.lalaland.ecommerce.viewModels.user.UserViewModel;

import java.util.HashMap;
import java.util.Map;

import static com.lalaland.ecommerce.helpers.AppConstants.SIGNIN_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;
import static com.lalaland.ecommerce.helpers.AppConstants.TYPE;
import static com.lalaland.ecommerce.helpers.AppConstants.USER_NAME;
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
    private String city, cityId;
    private boolean is_primary = false;
    private Map<String, String> parameter = new HashMap<>();
    private boolean isEditAddress = false;
    private UserAddresses editUserAddresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityAddressCreationBinding = DataBindingUtil.setContentView(this, R.layout.activity_address_creation);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        appPreference = AppPreference.getInstance(this);
        token = appPreference.getString(SIGNIN_TOKEN);

        isEditAddress = getIntent().getBooleanExtra("is_edit_address", false);

        if (isEditAddress) {
            editUserAddresses = getIntent().getParcelableExtra("user_address");
            activityAddressCreationBinding.cbDefaultContainer.setVisibility(View.GONE);

            String[] fullName = editUserAddresses.getUserNameAddress().split(" ");
            activityAddressCreationBinding.etFirstName.setText(fullName[0]);
            //  activityAddressCreationBinding.etLastName.setText(fullName[1]);
            activityAddressCreationBinding.etCity.setText(editUserAddresses.getCityName());
            activityAddressCreationBinding.etPostalCode.setText(String.valueOf(editUserAddresses.getPostalCode()));
            activityAddressCreationBinding.etAddress.setText(String.valueOf(editUserAddresses.getShippingAddress()));

            cityId = String.valueOf(editUserAddresses.getCityId());

            if (editUserAddresses.getIsPrimary() == 0)
                activityAddressCreationBinding.cbDefault.setChecked(false);
            else
                activityAddressCreationBinding.cbDefault.setChecked(true);

            activityAddressCreationBinding.tvScreenTitle.setText(getResources().getString(R.string.edit_information));
        } else {

            activityAddressCreationBinding.etFirstName.requestFocus();
            userAddresses = new UserAddresses();


            String mName = appPreference.getString(USER_NAME);
            String[] mNames = mName.split(" ");

            activityAddressCreationBinding.etFirstName.setText(mNames[0]);
/*
            try {
                activityAddressCreationBinding.etLastName.setText(mNames[1]);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }*/

        }

        activityAddressCreationBinding.setClickListener(this);

        activityAddressCreationBinding.etCity.setOnClickListener(v -> {
            Intent intent = new Intent(this, SelectCityActivity.class);
            startActivityForResult(intent, 100);
        });
    }

    public void cancel(View view) {
        onBackPressed();
    }

    public void save(View view) {

        if (
                validateNames(TYPE)
                        && validateAddress(1)
                        && validateAddress(2)
                        && validateAddress(3)
                        && validateCity()) {

            activityAddressCreationBinding.pbLoading.setVisibility(View.VISIBLE);

            is_primary = activityAddressCreationBinding.cbDefault.isChecked();

            parameter.put("first_name", first_name);
            parameter.put("last_name", "");
            parameter.put("physical_address", postal_address);
            parameter.put("shipping_address", postal_address);
            parameter.put("city_id", cityId);
            parameter.put("postal_code", postal_code);


            if (isEditAddress) {

                if (is_primary) {
                    parameter.put("is_primary", "1");
                } else {
                    parameter.put("is_primary", "0");
                }

                parameter.put("address_id", String.valueOf(editUserAddresses.getAddressId()));
                userViewModel.editAddress(token, parameter).observe(this, addressDataContainer -> {

                    if (addressDataContainer != null) {

                        if (addressDataContainer.getCode().equals(SUCCESS_CODE)) {
                            AppConstants.userAddresses = addressDataContainer.getData().getUserAddress().get(0);
                            Toast.makeText(this, addressDataContainer.getMsg(), Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                        }
                    }

                    activityAddressCreationBinding.pbLoading.setVisibility(View.GONE);
                });

            } else {

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
                            setResult(RESULT_OK);
                            finish();
                        } else if (addressDataContainer.getCode().equals(VALIDATION_FAIL_CODE)) {

                            Log.d(AppConstants.TAG, addressDataContainer.getMsg());
                            Toast.makeText(this, addressDataContainer.getMsg(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, addressDataContainer.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    activityAddressCreationBinding.pbLoading.setVisibility(View.GONE);
                });
            }
        }
    }

    private boolean validateNames(int type) {

        first_name = activityAddressCreationBinding.etFirstName.getText().toString().trim();
        //  last_name = activityAddressCreationBinding.etLastName.getText().toString().trim();

        if (type == TYPE) {

            if (first_name.isEmpty()) {
                activityAddressCreationBinding.tiFirstName.setError("First name could not be empty");
            } else {
                activityAddressCreationBinding.tiFirstName.setError(null);
                return true;
            }

        }/* else if (type == CONFIRM_TYPE) {

            if (last_name.isEmpty()) {
                activityAddressCreationBinding.tiLastName.setError("Last name could not be empty");
            } else {
                activityAddressCreationBinding.tiLastName.setError(null);
                return true;
            }
        }*/
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

    private boolean validateCity() {

        city = activityAddressCreationBinding.etCity.getText().toString().trim();

        if (city.isEmpty()) {
            activityAddressCreationBinding.tiCity.setError("Please select city");
            return false;
        }

        activityAddressCreationBinding.tiCity.setError(null);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {

                city = data.getStringExtra("city_name");
                cityId = data.getStringExtra("city_id");

                activityAddressCreationBinding.etCity.setText(city);
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
