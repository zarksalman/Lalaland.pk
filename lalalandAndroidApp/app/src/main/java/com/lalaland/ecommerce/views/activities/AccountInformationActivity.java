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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.databinding.ActivityAccountInformationNewBinding;
import com.lalaland.ecommerce.helpers.AppPreference;
import com.lalaland.ecommerce.helpers.AppUtils;
import com.lalaland.ecommerce.viewModels.user.UserViewModel;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.lalaland.ecommerce.helpers.AppConstants.DATE_OF_BIRTH;
import static com.lalaland.ecommerce.helpers.AppConstants.EMAIL;
import static com.lalaland.ecommerce.helpers.AppConstants.FIRST_NAME;
import static com.lalaland.ecommerce.helpers.AppConstants.GENDER;
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
    private boolean isUserUpdatedProfile = false;
    String[] fullNames;
    String userName;
    String phoneNumber;
    String email;
    String dateOfBirth;
    String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityAccountInformationBinding = DataBindingUtil.setContentView(this, R.layout.activity_account_information_new);
        activityAccountInformationBinding.setClickListener(this);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        appPreference = AppPreference.getInstance(this);
        token = appPreference.getString(SIGNIN_TOKEN);

        activityAccountInformationBinding.ivCloseCheckoutScreen.setOnClickListener(v -> {
            onBackPressed();
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

        userName = appPreference.getString(USER_NAME);
        phoneNumber = appPreference.getString(PHONE_NUMBER);
        email = appPreference.getString(EMAIL);
        dateOfBirth = appPreference.getString(DATE_OF_BIRTH);
        gender = appPreference.getString(GENDER);

        fullNames = userName.split(" ");
        activityAccountInformationBinding.tvUserName.setText(userName);
        activityAccountInformationBinding.tvUserPhone.setText(phoneNumber);
        activityAccountInformationBinding.tvUserEmail.setText(email);
        activityAccountInformationBinding.tvUserDob.setText(dateOfBirth);
        activityAccountInformationBinding.tvUserGender.setText(gender);


        String userAvatar = appPreference.getString(USER_AVATAR);
        String avatarImagePath = USER_STORAGE_BASE_URL.concat(userAvatar);

        Glide.with(this)
                .load(avatarImagePath)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.drawable.placeholder_products)
                .into(activityAccountInformationBinding.ivDisplayPicture);
    }

    public void changeAttributes(int type) {

        Intent intent;

        if (type == 6) {

            intent = new Intent(this, ChangeShippingAddress.class);
            intent.putExtra("request_code", type);
            startActivityForResult(intent, type);

        } else {
            intent = new Intent(this, EditAccountInformationActivity.class);
            intent.putExtra("request_code", type);

            switch (type) {

                case 1:

                    switch (fullNames.length) {

                        case 2:
                            intent.putExtra(FIRST_NAME, fullNames[0]);
                            intent.putExtra(LAST_NAME, fullNames[1]);
                            break;

                        case 3:
                            intent.putExtra(FIRST_NAME, fullNames[0] + fullNames[1]);
                            intent.putExtra(LAST_NAME, fullNames[2]);
                            break;

                        case 4:
                            intent.putExtra(FIRST_NAME, fullNames[0] + fullNames[1]);
                            intent.putExtra(LAST_NAME, fullNames[2] + fullNames[3]);
                            break;
                    }

                    break;

                case 2:
                    intent.putExtra(PHONE_NUMBER, phoneNumber);
                    break;

                case 4:
                    intent.putExtra(GENDER, gender);
                    break;

                case 5:
                    intent.putExtra(DATE_OF_BIRTH, dateOfBirth);
                    break;
            }

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
    }

    void uploadImage(Uri uri) {

        activityAccountInformationBinding.pbLoading.setVisibility(View.VISIBLE);

        File imageFile = AppUtils.getFile(this, uri);

        imageFile = AppUtils.saveBitmapToFile(imageFile);

        // create RequestBody instance from file
        RequestBody filePart = RequestBody.create(
                MediaType.parse(Objects.requireNonNull(getContentResolver().getType(uri))),
                imageFile);

        MultipartBody.Part file = MultipartBody.Part.createFormData("avatar", imageFile.getName(), filePart);

        userViewModel.uploadProfileImage(token, file).observe(this, uploadProfileImageContainer -> {

            if (uploadProfileImageContainer != null) {

                if (uploadProfileImageContainer.getCode().equals(SUCCESS_CODE)) {

                    appPreference.setString(USER_AVATAR, uploadProfileImageContainer.getData().getAvatar());

                    String userAvatar = uploadProfileImageContainer.getData().getAvatar();
                    String avatarImagePath = USER_STORAGE_BASE_URL.concat(userAvatar);
                    isUserUpdatedProfile = true;

                    activityAccountInformationBinding.ivDisplayPicture.setImageDrawable(null);

                    Glide.with(this)
                            .load(avatarImagePath)
                            .placeholder(R.drawable.placeholder_products)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(activityAccountInformationBinding.ivDisplayPicture);

                    Toast.makeText(this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(this, uploadProfileImageContainer.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            activityAccountInformationBinding.pbLoading.setVisibility(View.GONE);

        });
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
                    isUserUpdatedProfile = true;

                } else if (basicResponse.getCode().equals(VALIDATION_FAIL_CODE)) {
                    Toast.makeText(this, basicResponse.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
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
                    initUI();
                    isUserUpdatedProfile = true;
                    Toast.makeText(this, updateUserDataContainer.getMsg(), Toast.LENGTH_SHORT).show();


                } else if (updateUserDataContainer.getCode().equals(VALIDATION_FAIL_CODE)) {
                    Toast.makeText(this, updateUserDataContainer.getMsg(), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(this, updateUserDataContainer.getMsg(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case 1:

                    if (data != null) {
                        String first_name = getDataFromIntent(data, FIRST_NAME);
                        String last_name = getDataFromIntent(data, LAST_NAME);

                        parameter.clear();
                        parameter.put(FIRST_NAME, first_name);
                        parameter.put(LAST_NAME, last_name);
                        updateUserDetails();
                    }

                    break;

                case 2:

                    if (data != null) {
                        String phoneNumber = getDataFromIntent(data, PHONE_NUMBER);

                        parameter.clear();
                        parameter.put(PHONE_NUMBER, phoneNumber);
                        updateUserDetails();
                    }
                    break;
                case 3:

                    if (data != null) {
                        String oldPassword = getDataFromIntent(data, OLD_PASSWORD);
                        String password = getDataFromIntent(data, NEW_PASSWORD);

                        parameter.clear();
                        parameter.put(OLD_PASSWORD, oldPassword);
                        parameter.put(NEW_PASSWORD, password);

                        changePassword();
                    }

                    break;

                case 4:

                    if (data != null) {
                        String gender = getDataFromIntent(data, GENDER);

                        parameter.clear();
                        parameter.put(GENDER, gender);
                        updateUserDetails();
                    }

                    break;

                case 5:

                    if (data != null) {
                        String dob = getDataFromIntent(data, DATE_OF_BIRTH);

                        parameter.clear();
                        parameter.put(DATE_OF_BIRTH, dob);
                        updateUserDetails();
                    }
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

    @Override
    public void onBackPressed() {
        if (isUserUpdatedProfile)
            setResultForActivity(1);
        else
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

                    showAltersDialogue("You need to give access to upload image");
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                }
                break;
        }
    }

    private void showAltersDialogue(String msg) {

        new AlertDialog.Builder(this)
                .setTitle("Note")
                .setMessage(msg)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName())));
                })
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();

    }
}