package com.lalaland.ecommerce.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.databinding.ActivityAccountInformationBinding;
import com.lalaland.ecommerce.helpers.AppPreference;
import com.lalaland.ecommerce.viewModels.user.UserViewModel;

import java.util.HashMap;
import java.util.Map;

import static com.lalaland.ecommerce.helpers.AppConstants.DATE_OF_BIRTH;
import static com.lalaland.ecommerce.helpers.AppConstants.FIRST_NAME;
import static com.lalaland.ecommerce.helpers.AppConstants.GENDER;
import static com.lalaland.ecommerce.helpers.AppConstants.GENERAL_ERROR;
import static com.lalaland.ecommerce.helpers.AppConstants.LAST_NAME;
import static com.lalaland.ecommerce.helpers.AppConstants.NEW_PASSWORD;
import static com.lalaland.ecommerce.helpers.AppConstants.OLD_PASSWORD;
import static com.lalaland.ecommerce.helpers.AppConstants.PHONE_NUMBER;
import static com.lalaland.ecommerce.helpers.AppConstants.SIGNIN_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;
import static com.lalaland.ecommerce.helpers.AppConstants.VALIDATION_FAIL_CODE;
import static com.lalaland.ecommerce.helpers.AppConstants.userAddresses;

public class AccountInformationActivity extends AppCompatActivity {

    private ActivityAccountInformationBinding activityAccountInformationBinding;
    private UserViewModel userViewModel;
    private Intent intent;
    private Map<String, String> parameter = new HashMap<>();
    private AppPreference appPreference;

    private int requestCode;
    private String token;

    private String oldPassword = "";
    private String password = "";
    private String confirmPassword = "";
    private String first_name = "";
    private String last_name = "";
    private String phoneNumber = "";
    private String gender = "male";
    private String dob = "dob";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityAccountInformationBinding = DataBindingUtil.setContentView(this, R.layout.activity_account_information);
        activityAccountInformationBinding.setClickListener(this);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        appPreference = AppPreference.getInstance(this);
        token = appPreference.getString(SIGNIN_TOKEN);

        initUI();
    }

    private void initUI() {

        activityAccountInformationBinding.tvUserName.setText(userAddresses.getUserName());
        activityAccountInformationBinding.tvUserPhone.setText(userAddresses.getPhone());
        activityAccountInformationBinding.tvUserEmail.setText(userAddresses.getEmail());
        activityAccountInformationBinding.tvUserDob.setText(appPreference.getString(DATE_OF_BIRTH));
        activityAccountInformationBinding.tvUserGender.setText(appPreference.getString(GENDER));
    }

    public void changeAttributes(int type) {

        intent = new Intent(this, EditAccountInformationActivity.class);
        requestCode = type;
        intent.putExtra("request_code", type);
        startActivityForResult(intent, type);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    parameter.clear();
                    first_name = data.getStringExtra(FIRST_NAME);
                    last_name = data.getStringExtra(LAST_NAME);
                    break;

                case 2:
                    phoneNumber = data.getStringExtra(PHONE_NUMBER);

                    parameter.clear();
                    parameter.put(PHONE_NUMBER, phoneNumber);
                    updateUserDetails();
                    break;
                case 3:

                    oldPassword = data.getStringExtra(OLD_PASSWORD);
                    password = data.getStringExtra(NEW_PASSWORD);

                    parameter.clear();
                    parameter.put(OLD_PASSWORD, oldPassword);
                    parameter.put(NEW_PASSWORD, password);

                    changePassword();
                    break;

                case 4:

                    gender = data.getStringExtra(GENDER);

                    parameter.clear();
                    parameter.put(GENDER, gender);
                    updateUserDetails();

                    break;

                case 5:

                    dob = data.getStringExtra(DATE_OF_BIRTH);

                    parameter.clear();
                    parameter.put(DATE_OF_BIRTH, dob);
                    updateUserDetails();
                    break;
            }
        }
    }

    private void changePassword() {

        userViewModel.changePassword(token, parameter).observe(this, basicResponse -> {

            if (basicResponse != null) {

                if (basicResponse.getCode().equals(SUCCESS_CODE)) {
                    Log.d("registerUser", basicResponse.getMsg());
                    Log.d("registerUser", AppPreference.getInstance(this).getString(SIGNIN_TOKEN));
                    Toast.makeText(this, basicResponse.getMsg(), Toast.LENGTH_SHORT).show();

                } else if (basicResponse.getCode().equals(VALIDATION_FAIL_CODE)) {
                    Toast.makeText(this, basicResponse.getMsg(), Toast.LENGTH_SHORT).show();
                }
            } else
                Toast.makeText(this, GENERAL_ERROR, Toast.LENGTH_SHORT).show();
        });
    }

    private void updateUserDetails() {
        userViewModel.updateUserDetails(token, parameter).observe(this, updateUserDataContainer -> {

            if (updateUserDataContainer != null) {

                if (updateUserDataContainer.getCode().equals(SUCCESS_CODE)) {

                    userAddresses.setUserName(updateUserDataContainer.getData().getUser().getName());
                    userAddresses.setPhone(updateUserDataContainer.getData().getUser().getPhone());
                    appPreference.setString(DATE_OF_BIRTH, updateUserDataContainer.getData().getUser().getDateOfBirth());
                    appPreference.setString(GENDER, updateUserDataContainer.getData().getUser().getGender());

                    initUI();

                    Toast.makeText(this, updateUserDataContainer.getMsg(), Toast.LENGTH_SHORT).show();
                } else if (updateUserDataContainer.getCode().equals(VALIDATION_FAIL_CODE)) {
                    Toast.makeText(this, updateUserDataContainer.getMsg(), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(this, GENERAL_ERROR, Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, GENERAL_ERROR, Toast.LENGTH_SHORT).show();
        });
    }
}