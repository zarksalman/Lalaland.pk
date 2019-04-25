package com.lalaland.ecommerce.views.fragments.registrationFragments;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.databinding.FragmentSignupBinding;
import com.lalaland.ecommerce.helpers.AppPreference;
import com.lalaland.ecommerce.helpers.AppUtils;
import com.lalaland.ecommerce.viewModels.RegistrationViewModel;
import com.lalaland.ecommerce.views.activities.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.lalaland.ecommerce.helpers.AppConstants.CONFIRM_TYPE;
import static com.lalaland.ecommerce.helpers.AppConstants.FAIL_CODE;
import static com.lalaland.ecommerce.helpers.AppConstants.NO_NETWORK;
import static com.lalaland.ecommerce.helpers.AppConstants.PASSWORD;
import static com.lalaland.ecommerce.helpers.AppConstants.PASSWORD_PATTERN;
import static com.lalaland.ecommerce.helpers.AppConstants.SIGNIN_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;
import static com.lalaland.ecommerce.helpers.AppConstants.TYPE;
import static com.lalaland.ecommerce.helpers.AppUtils.isNetworkAvailable;


public class SignupFragment extends Fragment {

    private FragmentSignupBinding fragmentSignupBinding;
    private RegistrationViewModel registrationViewModel;
    final Calendar dobCalender = Calendar.getInstance();
    private Map<String, String> parameter = new HashMap<>();

    private String email = "";
    private String confirmEmail = "";
    private String password = "";
    private String confirmPassword = "";
    private String first_name = "";
    private String last_name = "";
    private String phoneNumber = "";

    public SignupFragment() {
        // Required empty public constructor
    }


    public static SignupFragment newInstance() {
        SignupFragment fragment = new SignupFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentSignupBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_signup, container, false);
        registrationViewModel = ViewModelProviders.of(this).get(RegistrationViewModel.class);
        setClickListeners();
        return fragmentSignupBinding.getRoot();
    }

    void setClickListeners() {

        fragmentSignupBinding.btnSignUp.setOnClickListener(v -> registerUser());
        fragmentSignupBinding.etDateOfBirth.setOnClickListener(v -> showDatePickerDialogue());
    }

    private boolean validateEmail(int type) {


        email = fragmentSignupBinding.etEmail.getText().toString().trim();
        confirmEmail = fragmentSignupBinding.etConfirmEmail.getText().toString().trim();

        if (!email.equals(confirmEmail)) {
            fragmentSignupBinding.tiEmail.setError("Emails are not same");
            fragmentSignupBinding.tiConfirmEmail.setError("Emails are not same");
            return false;
        }

        if (email.isEmpty()) {
            fragmentSignupBinding.tiEmail.setError("Email Could Not Be Empty");
            fragmentSignupBinding.tiConfirmEmail.setError("Email Could Not Be Empty");
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            fragmentSignupBinding.tiEmail.setError("Please enter a valid email address");
            fragmentSignupBinding.tiConfirmEmail.setError("Please enter a valid email address");
            return false;
        }

        fragmentSignupBinding.tiEmail.setError(null);
        fragmentSignupBinding.tiConfirmEmail.setError(null);
        return true;
/*
        if (type == TYPE) {

            if (email.isEmpty()) {
                fragmentSignupBinding.tiEmail.setError("Email Could Not Be Empty");
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                fragmentSignupBinding.tiEmail.setError("Please enter a valid email address");
            } else {
                fragmentSignupBinding.tiEmail.setError(null);
                return true;
            }
        } else if (type == CONFIRM_TYPE) {


            if (confirmEmail.isEmpty()) {
                fragmentSignupBinding.tiConfirmEmail.setError("Email Could Not Be Empty");
            } else if (!Patterns.EMAIL_ADDRESS.matcher(confirmEmail).matches()) {
                fragmentSignupBinding.tiConfirmEmail.setError("Please enter a valid email address");
            } else {
                fragmentSignupBinding.tiConfirmEmail.setError(null);
                return true;
            }
        }

        return false; // default return state*/
    }

    private boolean validatePasswords() {


        password = fragmentSignupBinding.etPassword.getText().toString().trim();
        confirmPassword = fragmentSignupBinding.etConfirmPassword.getText().toString().trim();

        if (!password.equals(confirmPassword)) {
            fragmentSignupBinding.tiPassword.setError("Passwords are not same");
            fragmentSignupBinding.tiConfirmPassword.setError("Passwords are not same");
            return false;
        }

        // check after this step password or confirm password because both are equal

        if (password.isEmpty()) {
            fragmentSignupBinding.tiPassword.setError("Passwords could not be empty");
            fragmentSignupBinding.tiConfirmPassword.setError("Passwords could not be empty");
            return false;
        }

//        *************************** make it correct regular expression**************************************
        if (!PASSWORD.matcher(password).matches()) {
            fragmentSignupBinding.tiPassword.setError("Please enter a valid Password (At least 1 digit,At least 1 Alphabet,At least 6 characters)");
            fragmentSignupBinding.tiConfirmPassword.setError("Please enter a valid Password (At least 1 digit, At least 1 Alphabet, At least 6 characters)");
            return false;
        }


        fragmentSignupBinding.tiPassword.setError(null);
        fragmentSignupBinding.tiConfirmPassword.setError(null);

        return true;


      /*  if (type == TYPE) {

            if (password.isEmpty()) {
                fragmentSignupBinding.tiPassword.setError("Password Could Not Be Empty");
            } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
                fragmentSignupBinding.tiPassword.setError("Please enter a valid Password (At least 1 digit,At least 1 Alphabet,At least 6 characters)");
            } else {
                fragmentSignupBinding.tiPassword.setError(null);
                return true;
            }
        } else if (type == CONFIRM_TYPE) {


            if (confirmPassword.isEmpty()) {
                fragmentSignupBinding.tiConfirmPassword.setError("Password Could Not Be Empty");
            } else if (!PASSWORD_PATTERN.matcher(confirmPassword).matches()) {
                fragmentSignupBinding.tiConfirmPassword.setError("Please enter a valid Password (At least 1 digit,At least 1 Alphabet,At least 6 characters)");
            } else {
                fragmentSignupBinding.tiConfirmPassword.setError(null);
                return true;

            }
        }*/

    }

    private boolean validateNames(int type) {

        first_name = fragmentSignupBinding.etFirstName.getText().toString().trim();
        last_name = fragmentSignupBinding.etLastName.getText().toString().trim();

        if (type == TYPE) {

            if (first_name.isEmpty()) {
                fragmentSignupBinding.tiFirstName.setError("First name could not be empty");
            } else {
                fragmentSignupBinding.tiFirstName.setError(null);
                return true;
            }

        } else if (type == CONFIRM_TYPE) {

            if (last_name.isEmpty()) {
                fragmentSignupBinding.tiLastName.setError("Last name could not be empty");
            } else {
                fragmentSignupBinding.tiLastName.setError(null);
                return true;
            }
        }
        return false;  // default return false
    }

    private boolean validatePhoneNumber() {

        phoneNumber = fragmentSignupBinding.etContactNumber.getText().toString().trim();

        if (phoneNumber.length() == 11) {
            fragmentSignupBinding.tiContactNumber.setError(null);
            return true;
        }

        fragmentSignupBinding.tiContactNumber.setError("Phone number should be 11 digit (03**-*******)");
        return false;
    }

    public void registerUser() {

        if (isNetworkAvailable()) {
            Toast.makeText(getContext(), NO_NETWORK, Toast.LENGTH_SHORT).show();
            return;
        }

        fragmentSignupBinding.pbLoading.setVisibility(View.VISIBLE);
        fragmentSignupBinding.btnSignUp.setOnClickListener(null);
        fragmentSignupBinding.btnFbSignUp.setOnClickListener(null);
        fragmentSignupBinding.btnGoogleSignUp.setOnClickListener(null);

        Toast.makeText(getContext(), "correct password validation", Toast.LENGTH_LONG).show();

        if (validateNames(TYPE)
                && validateNames(CONFIRM_TYPE)
                && validatePhoneNumber()
                && validateEmail(TYPE)
                && validateEmail(CONFIRM_TYPE)
                && validatePasswords()) {

            parameter.put("email", email);
            parameter.put("password", password);
            parameter.put("first_name", first_name);
            parameter.put("last_name", last_name);
            parameter.put("phone", phoneNumber);
            Log.d("signup_form", email.concat(password).concat(first_name).concat(last_name).concat(phoneNumber));

            registrationViewModel.registerUser(parameter).observe(this, registrationContainer -> {

                if (registrationContainer != null) {

                    if (registrationContainer.getCode().equals(SUCCESS_CODE)) {
                        Log.d("registerUser", registrationContainer.getData().getName() + ":" + registrationContainer.getData().getEmail());
                        Log.d("registerUser", AppPreference.getInstance(getContext()).getString(SIGNIN_TOKEN));
                        fragmentSignupBinding.pbLoading.setVisibility(View.GONE);
                        getContext().startActivity(new Intent(getContext(), MainActivity.class));
                    } else if (registrationContainer.getCode().equals(FAIL_CODE)) {
                        Toast.makeText(getContext(), registrationContainer.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(getContext(), "Could Not Register at this time", Toast.LENGTH_SHORT).show();
            });
        }

        /*
        StringBuilder email = new StringBuilder("salman_hameed_");
        email.append(AppPreference.getInstance(AppConstants.mContext).getInt(SIGNUP_COUNT));
        email.append("@gmail.com");


        parameter.put("email", email.toString());
        parameter.put("password", "salman123");
        parameter.put("first_name", "salman");
        parameter.put("last_name", "hameed");
        parameter.put("phone", "11111111111");

        registrationViewModel.registerUser(parameter).observe(this, registrationContainer -> {

            if (registrationContainer != null) {

                if (registrationContainer.getCode().equals(SUCCESS_CODE)) {
                    Log.d("registerUser", registrationContainer.getData().getName() + ":" + registrationContainer.getData().getEmail());
                    Log.d("registerUser", AppPreference.getInstance(getContext()).getString(SIGNIN_TOKEN));
                } else if (registrationContainer.getCode().equals(FAIL_CODE)) {
                    Toast.makeText(getContext(), registrationContainer.getMsg(), Toast.LENGTH_SHORT).show();
                }
            } else
                Toast.makeText(getContext(), "Could Not Register at this time", Toast.LENGTH_SHORT).show();
        });
*/
    }

    public void registerUserWithFacebook() {
        Toast.makeText(getContext(), "Login With Facebook", Toast.LENGTH_SHORT).show();
    }

    public void registerUserWithGoogle() {
        Toast.makeText(getContext(), "Login With Google", Toast.LENGTH_SHORT).show();
    }

    private void changePassword() {

        Map<String, String> parameter = new HashMap<>();
        parameter.put("old_password", "salman123");
        parameter.put("new_password", "salman123456789");

        registrationViewModel.changePassword(parameter).observe(this, basicResponse -> {

            if (basicResponse != null) {

                if (basicResponse.getCode().equals(SUCCESS_CODE)) {
                    Log.d("registerUser", basicResponse.getMsg());
                    Log.d("registerUser", AppPreference.getInstance(getContext()).getString(SIGNIN_TOKEN));
                } else if (basicResponse.getCode().equals(FAIL_CODE)) {
                    Toast.makeText(getContext(), basicResponse.getMsg(), Toast.LENGTH_SHORT).show();
                }
            } else
                Toast.makeText(getContext(), "Could Not Change Password at this time", Toast.LENGTH_SHORT).show();
        });

    }

    private void showDatePickerDialogue() {

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                dobCalender.set(Calendar.YEAR, year);
                dobCalender.set(Calendar.MONTH, monthOfYear);
                dobCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                setDOB();
            }
        };

        // context might be null
        if (getContext() != null) {

            DatePickerDialog dialog = new DatePickerDialog(getContext(), date,
                    dobCalender.get(Calendar.YEAR),
                    dobCalender.get(Calendar.MONTH),
                    dobCalender.get(Calendar.DAY_OF_MONTH));

            dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            dialog.show();
        }
    }

    private void setDOB() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        fragmentSignupBinding.etDateOfBirth.setText(sdf.format(dobCalender.getTime()));
    }
}
