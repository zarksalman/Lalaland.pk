package com.lalaland.ecommerce.views.fragments.registrationFragments;


import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.databinding.FragmentSignupBinding;
import com.lalaland.ecommerce.helpers.AppPreference;
import com.lalaland.ecommerce.viewModels.RegistrationViewModel;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static com.lalaland.ecommerce.helpers.AppConstants.CONFIRM_TYPE;
import static com.lalaland.ecommerce.helpers.AppConstants.FAIL_CODE;
import static com.lalaland.ecommerce.helpers.AppConstants.PASSWORD_PATTERN;
import static com.lalaland.ecommerce.helpers.AppConstants.SIGNIN_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;
import static com.lalaland.ecommerce.helpers.AppConstants.TYPE;


public class SignupFragment extends Fragment {

    private FragmentSignupBinding fragmentSignupBinding;
    private RegistrationViewModel registrationViewModel;

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


        return fragmentSignupBinding.getRoot();
    }

    private boolean validateEmail(int type) {

        String email = fragmentSignupBinding.etEmail.getText().toString().trim();
        String confirmEmail = fragmentSignupBinding.etConfirmEmail.getText().toString().trim();

        if (!email.equals(confirmEmail)) {
            fragmentSignupBinding.tiEmail.setError("Emails are not same");
            fragmentSignupBinding.tiConfirmEmail.setError("Emails are not same");
            return false;
        }

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

        return false; // default return state
    }

    private boolean validatePassword(int type) {

        String password = fragmentSignupBinding.etPassword.getText().toString().trim();
        String confirmPassword = fragmentSignupBinding.etConfirmPassword.getText().toString().trim();

        if (!password.equals(confirmPassword)) {
            fragmentSignupBinding.tiPassword.setError("Emails are not same");
            fragmentSignupBinding.tiConfirmPassword.setError("Emails are not same");
            return false;
        }

        if (type == TYPE) {

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
            } else if (!Patterns.EMAIL_ADDRESS.matcher(confirmPassword).matches()) {
                fragmentSignupBinding.tiConfirmPassword.setError("Please enter a valid Password (At least 1 digit,At least 1 Alphabet,At least 6 characters)");
            } else {
                fragmentSignupBinding.tiConfirmPassword.setError(null);
                return true;
            }
        }

        return false; // default return state

    }

    public void registerUser() {

        validateEmail(TYPE);
        validateEmail(CONFIRM_TYPE);

        validatePassword(TYPE);
        validatePassword(CONFIRM_TYPE);

        return;
        /*
        StringBuilder email = new StringBuilder("salman_hameed_");
        email.append(AppPreference.getInstance(AppConstants.mContext).getInt(SIGNUP_COUNT));
        email.append("@gmail.com");

        Map<String, String> parameter = new HashMap<>();
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
}
