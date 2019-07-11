package com.lalaland.ecommerce.views.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.databinding.ActivityAccountInformationNewBinding;
import com.lalaland.ecommerce.helpers.AppPreference;
import com.lalaland.ecommerce.helpers.AppUtils;
import com.lalaland.ecommerce.viewModels.user.UserViewModel;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.lalaland.ecommerce.helpers.AppConstants.DATE_OF_BIRTH;
import static com.lalaland.ecommerce.helpers.AppConstants.EMAIL;
import static com.lalaland.ecommerce.helpers.AppConstants.FIRST_NAME;
import static com.lalaland.ecommerce.helpers.AppConstants.GENDER;
import static com.lalaland.ecommerce.helpers.AppConstants.GENERAL_ERROR;
import static com.lalaland.ecommerce.helpers.AppConstants.LAST_NAME;
import static com.lalaland.ecommerce.helpers.AppConstants.NEW_PASSWORD;
import static com.lalaland.ecommerce.helpers.AppConstants.OLD_PASSWORD;
import static com.lalaland.ecommerce.helpers.AppConstants.PHONE_NUMBER;
import static com.lalaland.ecommerce.helpers.AppConstants.SIGNIN_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;
import static com.lalaland.ecommerce.helpers.AppConstants.USER_AVATAR;
import static com.lalaland.ecommerce.helpers.AppConstants.USER_NAME;
import static com.lalaland.ecommerce.helpers.AppConstants.USER_STORAGE_BASE_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.VALIDATION_FAIL_CODE;

public class AccountInformationActivity extends AppCompatActivity {

    private ActivityAccountInformationNewBinding activityAccountInformationBinding;
    private UserViewModel userViewModel;
    private Map<String, String> parameter = new HashMap<>();
    private AppPreference appPreference;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityAccountInformationBinding = DataBindingUtil.setContentView(this, R.layout.activity_account_information_new);
        activityAccountInformationBinding.setClickListener(this);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        appPreference = AppPreference.getInstance(this);
        token = appPreference.getString(SIGNIN_TOKEN);

        //   isAllowedToUploadImage();

        activityAccountInformationBinding.ivCloseCheckoutScreen.setOnClickListener(v -> {
            setResultForActivity(2);
        });

        initUI();
    }

    private void isAllowedToUploadImage() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    100);

        }
    }

    private void initUI() {

        String userName = appPreference.getString(USER_NAME);
        String phoneNumber = appPreference.getString(PHONE_NUMBER);
        String email = appPreference.getString(EMAIL);
        String dateOfBirth = appPreference.getString(DATE_OF_BIRTH);
        String gender = appPreference.getString(GENDER);

        activityAccountInformationBinding.tvUserName.setText(userName);
        activityAccountInformationBinding.tvUserPhone.setText(phoneNumber);
        activityAccountInformationBinding.tvUserEmail.setText(email);
        activityAccountInformationBinding.tvUserDob.setText(dateOfBirth);
        activityAccountInformationBinding.tvUserGender.setText(gender);


        String userAvatar = appPreference.getString(USER_AVATAR);
        String avatarImagePath = USER_STORAGE_BASE_URL.concat(userAvatar);

        Glide.with(this)
                .load(avatarImagePath)
                .placeholder(R.drawable.placeholder_products)
                .into(activityAccountInformationBinding.ivDisplayPicture);
    }

    public void changeAttributes(int type) {

        Intent intent;

        if (type == 0) {

            intent = new Intent(this, ChangeShippingAddress.class);
            intent.putExtra("request_code", type);
            startActivityForResult(intent, type);

        } else {
            intent = new Intent(this, EditAccountInformationActivity.class);
            intent.putExtra("request_code", type);
            startActivityForResult(intent, type);
        }

    }

    public void selectImage() {

        try {
            if (ActivityCompat.checkSelfPermission(AccountInformationActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(AccountInformationActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            } else {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 200);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

     /*   Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), 200);*/
    }

    void uploadImage(Uri uri) {

        activityAccountInformationBinding.pbLoading.setVisibility(View.VISIBLE);

        File imageFile = AppUtils.getFile(this, uri);

        RequestBody filePart = RequestBody.create(
                MediaType.parse(getContentResolver().getType(uri)),
                imageFile);

        MultipartBody.Part file = MultipartBody.Part.createFormData("photo", imageFile.getName(), filePart);

       /* RetrofitClient.getInstance().createClient().uploadProfileImage(token, file).enqueue(new Callback<UploadProfileImageContainer>() {
            @Override
            public void onResponse(Call<UploadProfileImageContainer> call, Response<UploadProfileImageContainer> response) {

                if (response.isSuccessful()) {

                    if (response.body() != null) {

                        if (response.body().getCode().equals(SUCCESS_CODE)) {

                            appPreference.setString(USER_AVATAR, response.body().getData().getAvatar());
                            Toast.makeText(AccountInformationActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                        } else {

                            Toast.makeText(AccountInformationActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                activityAccountInformationBinding.pbLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<UploadProfileImageContainer> call, Throwable t) {
                Log.d(AppConstants.TAG, t.getMessage());
            }
        });*/

        userViewModel.uploadProfileImage(token, file).observe(this, uploadProfileImageContainer -> {

            if (uploadProfileImageContainer != null) {

                if (uploadProfileImageContainer.getCode().equals(SUCCESS_CODE)) {

                    appPreference.setString(USER_AVATAR, uploadProfileImageContainer.getData().getAvatar());

                    String userAvatar = uploadProfileImageContainer.getData().getAvatar();
                    String avatarImagePath = USER_STORAGE_BASE_URL.concat(userAvatar);

                    Glide.with(this)
                            .load(avatarImagePath)
                            .placeholder(R.drawable.placeholder_products)
                            .into(activityAccountInformationBinding.ivDisplayPicture);

                    Toast.makeText(this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(this, uploadProfileImageContainer.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            activityAccountInformationBinding.pbLoading.setVisibility(View.GONE);

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:

                    String first_name = getDataFromIntent(data, FIRST_NAME);
                    String last_name = getDataFromIntent(data, LAST_NAME);

                    parameter.clear();
                    parameter.put(FIRST_NAME, first_name);
                    parameter.put(LAST_NAME, last_name);
                    updateUserDetails();
                    break;

                case 2:

                    String phoneNumber = getDataFromIntent(data, PHONE_NUMBER);

                    parameter.clear();
                    parameter.put(PHONE_NUMBER, phoneNumber);
                    updateUserDetails();
                    break;
                case 3:

                    String oldPassword = getDataFromIntent(data, OLD_PASSWORD);
                    String password = getDataFromIntent(data, NEW_PASSWORD);

                    parameter.clear();
                    parameter.put(OLD_PASSWORD, oldPassword);
                    parameter.put(NEW_PASSWORD, password);

                    changePassword();
                    break;

                case 4:

                    String gender = getDataFromIntent(data, GENDER);

                    parameter.clear();
                    parameter.put(GENDER, gender);
                    updateUserDetails();

                    break;

                case 5:

                    String dob = getDataFromIntent(data, DATE_OF_BIRTH);

                    parameter.clear();
                    parameter.put(DATE_OF_BIRTH, dob);
                    updateUserDetails();
                    break;

                case 200:

                    if (data != null && data.getData() != null) {
                        Uri uri = data.getData();
                        uploadImage(uri);
                    }
                    break;
            }
        }
    }

    String getDataFromIntent(Intent intent, String key) {

        if (intent != null)
            return intent.getStringExtra(key);
        else
            return "";
    }

    private void changePassword() {

        userViewModel.changePassword(token, parameter).observe(this, basicResponse -> {

            if (basicResponse != null) {

                if (basicResponse.getCode().equals(SUCCESS_CODE)) {

                    Log.d("registerUser", basicResponse.getMsg());
                    Toast.makeText(this, basicResponse.getMsg(), Toast.LENGTH_SHORT).show();
                    setResultForActivity(1);

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

                    appPreference.setString(USER_NAME, updateUserDataContainer.getData().getUser().getName());
                    appPreference.setString(PHONE_NUMBER, updateUserDataContainer.getData().getUser().getPhone());
                    appPreference.setString(DATE_OF_BIRTH, updateUserDataContainer.getData().getUser().getDateOfBirth());
                    appPreference.setString(GENDER, updateUserDataContainer.getData().getUser().getGender());

//                    AppConstants.userAddresses.setPhone(updateUserDataContainer.getData().getUser().getPhone());
                    initUI();
                    Toast.makeText(this, updateUserDataContainer.getMsg(), Toast.LENGTH_SHORT).show();

                    setResultForActivity(1);

                } else if (updateUserDataContainer.getCode().equals(VALIDATION_FAIL_CODE)) {
                    Toast.makeText(this, updateUserDataContainer.getMsg(), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(this, GENERAL_ERROR, Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, GENERAL_ERROR, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onBackPressed() {
        setResultForActivity(2);
    }

    void setResultForActivity(int resultType) {

        if (resultType == 1)
            setResult(RESULT_OK);
        else
            setResult(RESULT_CANCELED);

        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {

            case 100:

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, 200);
                } else {
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                }
                break;
        }
    }
}