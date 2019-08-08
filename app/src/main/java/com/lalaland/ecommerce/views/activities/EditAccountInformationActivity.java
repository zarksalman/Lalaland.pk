package com.lalaland.ecommerce.views.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.databinding.ActivityEditAccountInformationBinding;
import com.lalaland.ecommerce.helpers.AppPreference;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.lalaland.ecommerce.helpers.AppConstants.CONFIRM_TYPE;
import static com.lalaland.ecommerce.helpers.AppConstants.DATE_OF_BIRTH;
import static com.lalaland.ecommerce.helpers.AppConstants.FIRST_NAME;
import static com.lalaland.ecommerce.helpers.AppConstants.GENDER;
import static com.lalaland.ecommerce.helpers.AppConstants.LAST_NAME;
import static com.lalaland.ecommerce.helpers.AppConstants.NEW_PASSWORD;
import static com.lalaland.ecommerce.helpers.AppConstants.OLD_PASSWORD;
import static com.lalaland.ecommerce.helpers.AppConstants.PASSWORD;
import static com.lalaland.ecommerce.helpers.AppConstants.PHONE_NUMBER;
import static com.lalaland.ecommerce.helpers.AppConstants.TYPE;

public class EditAccountInformationActivity extends AppCompatActivity {

    private ActivityEditAccountInformationBinding activityEditAccountInformationBinding;
    private Intent intent;
    private int requestCode;

    private String oldPassword = "";
    private String password = "";
    private String confirmPassword = "";
    private String first_name = "";
    private String last_name = "";
    private String phoneNumber = "";
    private String gender = "male";
    private String dob = "dob";

    final Calendar dobCalender = Calendar.getInstance();
    private AppPreference appPreference;
    private Intent data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityEditAccountInformationBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_account_information);

        appPreference = AppPreference.getInstance(this);
        data = getIntent();
        requestCode = data.getIntExtra("request_code", -1);
        setListeners();
        if (requestCode != -1)
            setInfoVisibility(requestCode);
    }

    private void setListeners() {

        activityEditAccountInformationBinding.setClickListener(this);

    /*    activityEditAccountInformationBinding.etDateOfBirth.setOnClickListener(v -> {
            showDatePickerDialogue();
        });*/

        activityEditAccountInformationBinding.rgGender.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_male)
                gender = "male";
            else
                gender = "female";

            appPreference.setString(GENDER, gender);

        });
    }

    void setInfoVisibility(int visibilityCode) {

        switch (visibilityCode) {

            case 1:

                if (data != null) {

                    first_name = data.getStringExtra(FIRST_NAME);
                    last_name = data.getStringExtra(LAST_NAME);

                    activityEditAccountInformationBinding.etFirstName.setText(first_name);
                    activityEditAccountInformationBinding.etLastName.setText(last_name);

                }

                activityEditAccountInformationBinding.userNameContainer.setVisibility(View.VISIBLE);
                break;

            case 2:

                if (data != null) {
                    phoneNumber = data.getStringExtra(PHONE_NUMBER);
                    activityEditAccountInformationBinding.etContactNumber.setText(phoneNumber);
                }

                activityEditAccountInformationBinding.userPhoneContainer.setVisibility(View.VISIBLE);
                break;

            case 3:
                activityEditAccountInformationBinding.userPasswordContainer.setVisibility(View.VISIBLE);
                break;

            case 4:
                if (data != null) {
                    gender = data.getStringExtra(GENDER);
                    activityEditAccountInformationBinding.etContactNumber.setText(phoneNumber);

                    if (gender.equals("male"))
                        activityEditAccountInformationBinding.rbMale.setChecked(true);
                    else
                        activityEditAccountInformationBinding.rbFemale.setChecked(true);
                }

                activityEditAccountInformationBinding.userGenderContainer.setVisibility(View.VISIBLE);
                break;

            case 5:

                if (data != null) {
                    dob = data.getStringExtra(DATE_OF_BIRTH);
                    activityEditAccountInformationBinding.etDateOfBirth.setText(dob);
                }

                activityEditAccountInformationBinding.userDobContainer.setVisibility(View.VISIBLE);
                break;

        }
    }

    public void saveInfo() {

        intent = new Intent();

        switch (requestCode) {

            case 1:

                if (validateNames(TYPE) && validateNames(CONFIRM_TYPE)) {
                    intent.putExtra(FIRST_NAME, first_name);
                    intent.putExtra(LAST_NAME, last_name);
                } else {
                    intent = null;
                    return;
                }
                break;

            case 2:
                if (validatePhoneNumber()) {
                    intent.putExtra(PHONE_NUMBER, phoneNumber);

                } else {
                    intent = null;
                    return;
                }
                break;

            case 3:
                if (validatePasswords()) {
                    intent.putExtra(OLD_PASSWORD, oldPassword);
                    intent.putExtra(NEW_PASSWORD, password);

                } else {
                    intent = null;
                    return;
                }
                break;

            case 4:
                intent.putExtra(GENDER, gender);
                break;

            case 5:
                if (validateDob()) {
                    intent.putExtra(DATE_OF_BIRTH, dob);
                } else {
                    intent = null;
                    return;
                }
                break;

        }

        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK, intent);
        finish();
    }

    public void showDatePickerDialogue() {

        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            dobCalender.set(Calendar.YEAR, year);
            dobCalender.set(Calendar.MONTH, monthOfYear);
            dobCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setDOB();
        };

        DatePickerDialog dialog = new DatePickerDialog(this, date,
                dobCalender.get(Calendar.YEAR),
                dobCalender.get(Calendar.MONTH),
                dobCalender.get(Calendar.DAY_OF_MONTH));

        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dialog.show();
    }

    private void setDOB() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        dob = sdf.format(dobCalender.getTime());
        activityEditAccountInformationBinding.etDateOfBirth.setText(dob);
    }

    private boolean validatePasswords() {


        oldPassword = activityEditAccountInformationBinding.etOldPassword.getText().toString();
        password = activityEditAccountInformationBinding.etPassword.getText().toString().trim();
        confirmPassword = activityEditAccountInformationBinding.etConfirmPassword.getText().toString().trim();

        if (oldPassword.length() < 6) {
            activityEditAccountInformationBinding.tiOldPassword.setError("Password should be at least length of 6");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            activityEditAccountInformationBinding.tiPassword.setError("Passwords are not same");
            activityEditAccountInformationBinding.tiConfirmPassword.setError("Passwords are not same");
            return false;
        }

        // check after this step password or confirm password because both are equal

        if (password.isEmpty()) {
            activityEditAccountInformationBinding.tiPassword.setError("Passwords could not be empty");
            activityEditAccountInformationBinding.tiConfirmPassword.setError("Passwords could not be empty");
            return false;
        }

        if (!PASSWORD.matcher(password).matches()) {
            activityEditAccountInformationBinding.tiPassword.setError("Please enter a valid Password (At least 1 digit,At least 1 Alphabet,At least 6 characters)");
            activityEditAccountInformationBinding.tiConfirmPassword.setError("Please enter a valid Password (At least 1 digit, At least 1 Alphabet, At least 6 characters)");
            return false;
        }


        activityEditAccountInformationBinding.tiPassword.setError(null);
        activityEditAccountInformationBinding.tiConfirmPassword.setError(null);

        return true;

    }

    private boolean validateNames(int type) {

        first_name = activityEditAccountInformationBinding.etFirstName.getText().toString().trim();
        last_name = activityEditAccountInformationBinding.etLastName.getText().toString().trim();

        if (type == TYPE) {

            if (first_name.isEmpty()) {
                activityEditAccountInformationBinding.tiFirstName.setError("First name could not be empty");
            } else {
                activityEditAccountInformationBinding.tiFirstName.setError(null);
                return true;
            }

        } else if (type == CONFIRM_TYPE) {

            if (last_name.isEmpty()) {
                activityEditAccountInformationBinding.tiLastName.setError("Last name could not be empty");
            } else {
                activityEditAccountInformationBinding.tiLastName.setError(null);
                return true;
            }
        }
        return false;  // default return false
    }

    private boolean validateDob() {

        dob = activityEditAccountInformationBinding.etDateOfBirth.getText().toString().trim();

        if (dob.isEmpty()) {
            activityEditAccountInformationBinding.tiDateOfBirth.setError("Dob could not be empty");
            return false;  // default return false
        }

        return true;
    }

    private boolean validatePhoneNumber() {

        phoneNumber = activityEditAccountInformationBinding.etContactNumber.getText().toString().trim();

        if (phoneNumber.length() == 11) {
            activityEditAccountInformationBinding.tiContactNumber.setError(null);
            return true;
        }

        activityEditAccountInformationBinding.tiContactNumber.setError("Phone number should be 11 digit (03**-*******)");
        return false;
    }
}
