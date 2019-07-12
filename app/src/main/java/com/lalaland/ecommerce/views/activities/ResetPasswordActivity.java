package com.lalaland.ecommerce.views.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.databinding.ActivityResetPasswordBinding;
import com.lalaland.ecommerce.helpers.AppConstants;
import com.lalaland.ecommerce.helpers.AppPreference;
import com.lalaland.ecommerce.viewModels.user.LoginViewModel;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.lalaland.ecommerce.helpers.AppConstants.CONFIRM_RESET_PASSWORD;
import static com.lalaland.ecommerce.helpers.AppConstants.EMAIL;
import static com.lalaland.ecommerce.helpers.AppConstants.PASSWORD;
import static com.lalaland.ecommerce.helpers.AppConstants.RECOMMENDED_CAT_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.RESET_PASSWORD;
import static com.lalaland.ecommerce.helpers.AppConstants.RESET_PASSWORD_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;

public class ResetPasswordActivity extends AppCompatActivity {

    ActivityResetPasswordBinding activityResetPasswordBinding;
    LoginViewModel loginViewModel;
    Map<String, String> parameter = new HashMap<>();
    String email, token, recommendedCat;
    AlertDialog resetDialogue;
    View dialogView;
    AlertDialog.Builder dialogBuilder;
    String isResetEmail;
    Intent intent;
    Uri uri;
    String server;
    String path;
    String protocol;
    Set<String> args;
    Object[] keys;
    String password;
    String confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityResetPasswordBinding = DataBindingUtil.setContentView(this, R.layout.activity_reset_password);

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        recommendedCat = AppPreference.getInstance(this).getString(RECOMMENDED_CAT_TOKEN);

        intent = getIntent();
        isResetEmail = intent.getStringExtra("is_reset_password");
        perpareDialogue();

        // not coming from deep linking
        if (isResetEmail != null && isResetEmail.equals("true")) {
            activityResetPasswordBinding.emailContainer.setVisibility(View.VISIBLE);
        } else {


            getParameters(intent);


            activityResetPasswordBinding.tvResetPassword.setText(email);
            activityResetPasswordBinding.passwordContainer.setVisibility(View.VISIBLE);
        }

        activityResetPasswordBinding.btnResetEmail.setOnClickListener(v -> {

            if (validateEmail()) {

                activityResetPasswordBinding.pbLoading.setVisibility(View.VISIBLE);

                parameter.put(AppConstants.EMAIL, email);
                loginViewModel.forgotPassword(parameter).observe(this, basicResponse -> {

                    if (basicResponse != null) {

                        showAltersDialogue(basicResponse.getMsg());
                    }
                });
            }
        });

        activityResetPasswordBinding.btnResetPassword.setOnClickListener(v -> {

            if (validatePasswords()) {

                activityResetPasswordBinding.pbLoading.setVisibility(View.VISIBLE);

                parameter.put(RESET_PASSWORD, password);
                parameter.put(CONFIRM_RESET_PASSWORD, confirmPassword);

                loginViewModel.resetPassword(parameter).observe(this, basicResponse -> {
                    if (basicResponse != null) {

                        if (basicResponse.getCode().equals(SUCCESS_CODE)) {

                            Toast.makeText(this, basicResponse.getMsg(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(this, SplashActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }

                    activityResetPasswordBinding.pbLoading.setVisibility(View.GONE);
                });
            }
        });

        activityResetPasswordBinding.ivBtnBack.setOnClickListener(v -> {
            finish();
        });
    }

    private void getParameters(Intent intent) {

        uri = intent.getData();
        String tempStr = getString(R.string.enter_your_password);

        if (uri != null) {
            /*server = uri.getAuthority();
            path = uri.getPath();
            protocol = uri.getScheme();
            */

            args = uri.getQueryParameterNames();
            keys = args.toArray();

            if (keys != null && keys.length > 0) {

                token = uri.getQueryParameter(keys[0].toString());
                email = uri.getQueryParameter(keys[1].toString());

                parameter.put(RESET_PASSWORD_TOKEN, token);
                parameter.put(EMAIL, email);
                parameter.put(RECOMMENDED_CAT_TOKEN, recommendedCat);

                email = tempStr + " " + email;
            }
        }



    }

    private void perpareDialogue() {

        dialogView = LayoutInflater.from(this).inflate(R.layout.reset_password_dialogue_layout, null);

        dialogBuilder = new AlertDialog.Builder(this);
        resetDialogue = dialogBuilder.create();
        resetDialogue.setCancelable(false);
        resetDialogue.setView(dialogView);

        resetDialogue.setButton(DialogInterface.BUTTON_POSITIVE, getText(R.string.yes), (dialog, which) -> {
            dialog.dismiss();
        });
    }

    private void showAltersDialogue(String msg) {

        new AlertDialog.Builder(this)
                .setTitle("Note")
                .setMessage(msg)

                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                })
                .show();

        activityResetPasswordBinding.pbLoading.setVisibility(View.GONE);
    }

    private boolean validateEmail() {


        email = activityResetPasswordBinding.etResetPassword.getText().toString().trim();


        if (email.isEmpty()) {
            activityResetPasswordBinding.tiEmailResetPassword.setError("Email Could Not Be Empty");
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            activityResetPasswordBinding.tiEmailResetPassword.setError("Please enter a valid email address");
            return false;
        }

        activityResetPasswordBinding.tiEmailResetPassword.setError(null);
        return true;
    }

    private boolean validatePasswords() {


        password = activityResetPasswordBinding.etPassword.getText().toString().trim();
        confirmPassword = activityResetPasswordBinding.etConfirmPassword.getText().toString().trim();

        if (!password.equals(confirmPassword)) {
            activityResetPasswordBinding.tiPassword.setError("Passwords are not same");
            activityResetPasswordBinding.tiConfirmPassword.setError("Passwords are not same");
            return false;
        }

        // check after this step password or confirm password because both are equal

        if (password.isEmpty()) {
            activityResetPasswordBinding.tiPassword.setError("Passwords could not be empty");
            activityResetPasswordBinding.tiConfirmPassword.setError("Passwords could not be empty");
            return false;
        }

        if (!PASSWORD.matcher(password).matches()) {
            activityResetPasswordBinding.tiPassword.setError("Please enter a valid Password (At least 1 digit,At least 1 Alphabet,At least 6 characters)");
            activityResetPasswordBinding.tiConfirmPassword.setError("Please enter a valid Password (At least 1 digit, At least 1 Alphabet, At least 6 characters)");
            return false;
        }


        activityResetPasswordBinding.tiPassword.setError(null);
        activityResetPasswordBinding.tiConfirmPassword.setError(null);

        return true;

    }

}
