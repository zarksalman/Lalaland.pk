package com.lalaland.ecommerce.views.fragments.registrationFragments;


import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.databinding.FragmentSignupBinding;
import com.lalaland.ecommerce.helpers.AppConstants;
import com.lalaland.ecommerce.helpers.AppPreference;
import com.lalaland.ecommerce.viewModels.RegistrationViewModel;

import java.util.HashMap;
import java.util.Map;

import static com.lalaland.ecommerce.helpers.AppConstants.FAIL_CODE;
import static com.lalaland.ecommerce.helpers.AppConstants.SIGNIN_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.SIGNUP_COUNT;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;


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

        fragmentSignupBinding.btnRegister.setOnClickListener(v -> registerUser());

        fragmentSignupBinding.btnChangePassword.setOnClickListener(v -> changePassword());

        return fragmentSignupBinding.getRoot();
    }

    void registerUser() {

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
