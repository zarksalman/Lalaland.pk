package com.lalaland.ecommerce.views.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.adapters.RegistrationTabsAdapter;
import com.lalaland.ecommerce.databinding.ActivityRegistrationBinding;
import com.lalaland.ecommerce.views.fragments.registrationFragments.SigninFragment;
import com.lalaland.ecommerce.views.fragments.registrationFragments.SignupFragment;

import static com.lalaland.ecommerce.helpers.AppConstants.REGISTER;
import static com.lalaland.ecommerce.helpers.AppConstants.SIGN_IN;

public class RegistrationActivity extends AppCompatActivity {

    private ActivityRegistrationBinding registrationBinding;
    private RegistrationTabsAdapter registrationTabsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        registrationBinding = DataBindingUtil.setContentView(this, R.layout.activity_registration);
        registrationBinding.setListener(this);

        registrationTabsAdapter = new RegistrationTabsAdapter(getSupportFragmentManager());
        registrationBinding.vpRegistration.setAdapter(registrationTabsAdapter);
        registrationTabsAdapter.addFragment(SigninFragment.newInstance(), SIGN_IN);
        registrationTabsAdapter.addFragment(SignupFragment.newInstance(), REGISTER);
        registrationTabsAdapter.notifyDataSetChanged();
        registrationBinding.tlScreenRegistration.setupWithViewPager(registrationBinding.vpRegistration);
    }

    @Override
    public void onBackPressed() {
        closeActivity();
    }

    public void closeActivity() {

        setResult(RESULT_CANCELED);
        finish();
    }
}
