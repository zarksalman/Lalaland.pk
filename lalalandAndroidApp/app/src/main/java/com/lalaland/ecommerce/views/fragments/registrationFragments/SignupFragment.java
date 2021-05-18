package com.lalaland.ecommerce.views.fragments.registrationFragments;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.databinding.FragmentSignupBinding;
import com.lalaland.ecommerce.helpers.AnalyticsManager;
import com.lalaland.ecommerce.helpers.AppConstants;
import com.lalaland.ecommerce.helpers.AppPreference;
import com.lalaland.ecommerce.helpers.AppUtils;
import com.lalaland.ecommerce.interfaces.LoadingLogin;
import com.lalaland.ecommerce.viewModels.user.RegistrationViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.lalaland.ecommerce.helpers.AppConstants.AUTHORIZATION_FAIL_CODE;
import static com.lalaland.ecommerce.helpers.AppConstants.CART_SESSION_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.DATE_OF_BIRTH;
import static com.lalaland.ecommerce.helpers.AppConstants.FORM_SIGN_UP;
import static com.lalaland.ecommerce.helpers.AppConstants.GENDER;
import static com.lalaland.ecommerce.helpers.AppConstants.NO_NETWORK;
import static com.lalaland.ecommerce.helpers.AppConstants.PASSWORD;
import static com.lalaland.ecommerce.helpers.AppConstants.PHONE_NUMBER;
import static com.lalaland.ecommerce.helpers.AppConstants.SIGNIN_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;
import static com.lalaland.ecommerce.helpers.AppConstants.TYPE;
import static com.lalaland.ecommerce.helpers.AppConstants.USER_AVATAR;
import static com.lalaland.ecommerce.helpers.AppConstants.USER_NAME;
import static com.lalaland.ecommerce.helpers.AppConstants.VALIDATION_FAIL_CODE;
import static com.lalaland.ecommerce.helpers.AppConstants.mContext;
import static com.lalaland.ecommerce.helpers.AppUtils.isNetworkAvailable;


public class SignupFragment extends BaseRegistrationFragment implements LoadingLogin {

    final Calendar dobCalender = Calendar.getInstance();
    private FragmentSignupBinding fragmentSignupBinding;
    private RegistrationViewModel registrationViewModel;
    private Map<String, String> parameter = new HashMap<>();

    private String email = "";
    private String confirmEmail = "";
    private String password = "";
    private String confirmPassword = "";
    private String first_name = "";
    private String last_name = "";
    private String phoneNumber = "";
    private String gender = "";
    private String dob = "";

    private String token, cart_session;
    private AppPreference appPreference;
    private Bundle bundle = new Bundle();

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
        registrationViewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);
        appPreference = AppPreference.getInstance(getContext());
        cart_session = appPreference.getString(CART_SESSION_TOKEN);

        gender = getString(R.string.gender_male);
        setClickListeners();

        return fragmentSignupBinding.getRoot();
    }

    private void setClickListeners() {

        fragmentSignupBinding.btnSignUp.setOnClickListener(v -> sinUpWithForm());
        fragmentSignupBinding.btnFbSignUp.setOnClickListener(v -> signInOrSignUpWithFb(fragmentSignupBinding.btFacebookSignup, this));
        fragmentSignupBinding.etDateOfBirth.setOnClickListener(v -> showDatePickerDialogue());

        fragmentSignupBinding.rgGender.setOnCheckedChangeListener((group, checkedId) -> {
                    RadioButton radioButton = group.findViewById(checkedId);
                    gender = radioButton.getText().toString().toLowerCase();
                }
        );

        fragmentSignupBinding.btnGoogleSignUp.setOnClickListener(v -> {
            registerUserWithGoogle();
        });
    }

    private boolean validateEmail() {


        email = fragmentSignupBinding.etEmail.getText().toString().trim();
        /*confirmEmail = fragmentSignupBinding.etConfirmEmail.getText().toString().trim();

        if (!email.equals(confirmEmail)) {
            fragmentSignupBinding.tiEmail.setError("Emails are not same");
            fragmentSignupBinding.tiConfirmEmail.setError("Emails are not same");
            return false;
        }*/

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
    }

    private boolean validatePasswords() {


        password = fragmentSignupBinding.etPassword.getText().toString().trim();
        /*confirmPassword = fragmentSignupBinding.etConfirmPassword.getText().toString().trim();

        if (!password.equals(confirmPassword)) {
            fragmentSignupBinding.tiPassword.setError("Passwords are not same");
            fragmentSignupBinding.tiConfirmPassword.setError("Passwords are not same");
            return false;
        }*/

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

    }

    private boolean validateNames(int type) {

        first_name = fragmentSignupBinding.etName.getText().toString().trim();
        //last_name = fragmentSignupBinding.etLastName.getText().toString().trim();

        if (type == TYPE) {

            if (first_name.isEmpty()) {
                fragmentSignupBinding.tiName.setError("Name could not be empty");
            } else {
                fragmentSignupBinding.tiName.setError(null);
                return true;
            }

        }/* else if (type == CONFIRM_TYPE) {

            if (last_name.isEmpty()) {
                fragmentSignupBinding.tiLastName.setError("Last name could not be empty");
            } else {
                fragmentSignupBinding.tiLastName.setError(null);
                return true;
            }
        }*/
        return false;  // default return false
    }

    private boolean validateDob() {

        dob = fragmentSignupBinding.etDateOfBirth.getText().toString().trim();

        if (dob.isEmpty()) {
            fragmentSignupBinding.tiDateOfBirth.setError("Dob could not be empty");
            return false;  // default return false
        }

        return true;
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

    public void registerUser(int signUpType) {

        if (isNetworkAvailable()) {
            Toast.makeText(getContext(), NO_NETWORK, Toast.LENGTH_SHORT).show();
            return;
        }
    }

    void sinUpWithForm() {

        if (validateNames(TYPE)
                //&& validateNames(CONFIRM_TYPE)
                && validatePhoneNumber()
                && validateEmail()
                && validatePasswords()) {

            AppUtils.hideKeyboard(getActivity());

            fragmentSignupBinding.pbLoading.setVisibility(View.VISIBLE);

            parameter.put("name", first_name);
            parameter.put("phone", phoneNumber);
            parameter.put("email", email);
            parameter.put("password", password);

            // parameter.put("first_name", first_name);
            // parameter.put("last_name", last_name);
            parameter.put("gender", gender);
            // parameter.put("date_of_birth", dob);

            signUpCallToApi(FORM_SIGN_UP);
        }
    }


    public void registerUserWithGoogle() {
        fragmentSignupBinding.pbLoading.setVisibility(View.VISIBLE);
        signInOrSignUpWithGoogle(fragmentSignupBinding.btGoogleSignup, this);
    }

    private void signUpCallToApi(int signUpType) {

        registrationViewModel.registerUser(cart_session, parameter, signUpType).observe(this, registrationContainer -> {

            if (registrationContainer != null) {

                switch (registrationContainer.getCode()) {
                    case SUCCESS_CODE:
                        Log.d("registerUser", registrationContainer.getData().getUser().getName() + ":" + registrationContainer.getData().getUser().getEmail());
                        Log.d("registerUser", AppPreference.getInstance(getContext()).getString(SIGNIN_TOKEN));

                        AppPreference.getInstance(mContext).setString(USER_NAME, registrationContainer.getData().getUser().getName());
                        AppPreference.getInstance(mContext).setString(DATE_OF_BIRTH, registrationContainer.getData().getUser().getDateOfBirth());
                        AppPreference.getInstance(mContext).setString(PHONE_NUMBER, registrationContainer.getData().getUser().getPhone());
                        AppPreference.getInstance(mContext).setString(GENDER, registrationContainer.getData().getUser().getGender());
                        AppPreference.getInstance(mContext).setString(AppConstants.EMAIL, registrationContainer.getData().getUser().getEmail());

                        AnalyticsManager.getInstance().sendAnalytics("sign_up", bundle);
                        AnalyticsManager.getInstance().sendAnalytics("login_signup", bundle);

                        AnalyticsManager.getInstance().sendFacebookAnalytics("Complete Registration", bundle);
                        AnalyticsManager.getInstance().sendFacebookAnalytics("login_signup", bundle);


                        if (registrationContainer.getData().getUser().getAvatar() != null)
                            AppPreference.getInstance(mContext).setString(USER_AVATAR, registrationContainer.getData().getUser().getAvatar().toString());
                        else
                            AppPreference.getInstance(mContext).setString(USER_AVATAR, "");


                        getActivity().setResult(Activity.RESULT_OK);
                        getActivity().finish();

                        break;
                    case VALIDATION_FAIL_CODE:
                    case AUTHORIZATION_FAIL_CODE:
                        hideProgressBar();
                        showToast(registrationContainer.getMsg());
                        break;
                }
            } else {
                hideProgressBar();
            }
        });
    }

    private void showDatePickerDialogue() {

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                dobCalender.set(Calendar.YEAR, year);
                dobCalender.set(Calendar.MONTH, monthOfYear);
                dobCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                setDOB();
            }
        };

        // context might be null
        if (getContext() != null) {

            DatePickerDialog dialog = new DatePickerDialog(getContext(),
                    date,
                    dobCalender.get(Calendar.YEAR),
                    dobCalender.get(Calendar.MONTH),
                    dobCalender.get(Calendar.DAY_OF_MONTH));

            dialog.getDatePicker().setMaxDate(System.currentTimeMillis());

            dialog.getDatePicker().setCalendarViewShown(true);
            dialog.show();


        }
    }

    private void setDOB() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        dob = sdf.format(dobCalender.getTime());
        fragmentSignupBinding.etDateOfBirth.setText(dob);
    }


    void hideProgressBar() {
        fragmentSignupBinding.pbLoading.setVisibility(View.GONE);
    }

    void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }


    @Override
    public void checkLoading(boolean isLoadingStop) {
        if (isLoadingStop) {
            fragmentSignupBinding.pbLoading.setVisibility(View.GONE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {

            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            fragmentSignupBinding.pbLoading.setVisibility(View.VISIBLE);
        }
    }
}
