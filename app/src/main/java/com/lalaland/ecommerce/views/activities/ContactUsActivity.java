package com.lalaland.ecommerce.views.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.databinding.ActivityContactUsBinding;
import com.lalaland.ecommerce.helpers.AppUtils;

public class ContactUsActivity extends AppCompatActivity {

    ActivityContactUsBinding activityContactUsBinding;
    Intent intent;
    String FACEBOOK_URL = "https://www.facebook.com/lalaland.pk";
    String FACEBOOK_PAGE_ID = "2096149290642572";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityContactUsBinding = DataBindingUtil.setContentView(this, R.layout.activity_contact_us);

        activityContactUsBinding.facebookContainer.setOnClickListener(v -> {
            startFbApplication();
        });

        activityContactUsBinding.instagramContainer.setOnClickListener(v -> {
            startInstaApplication();
        });

        activityContactUsBinding.twitterContainer.setOnClickListener(v -> {
            startTwitterApplication();
        });

        activityContactUsBinding.linkedInContainer.setOnClickListener(v -> {
            startLinkedInApplication();
        });

        activityContactUsBinding.ivBackArrow.setOnClickListener(v -> {
            finish();
        });
    }

    private void startFbApplication() {

        //  intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/2096149290642572"));
        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getFacebookPageURL()));
            startActivity(intent);

    }

    private void startInstaApplication() {
        try {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("instagram://user?username=lalaland.pk"));
            startActivity(intent);
        } catch (Exception e) {
            startActivity(AppUtils.getOpenUrlIntent("https://www.instagram.com/lalaland.pk"));
        }
    }

    private void startTwitterApplication() {
        try {
            //    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=lalaland.pk"));
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=994843236765192192"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            startActivity(AppUtils.getOpenUrlIntent("https://twitter.com/lalalandpk/"));
        }

    }

    private void startLinkedInApplication() {
        try {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("linkedin://company/lalaland-pk/about"));
            startActivity(intent);
        } catch (Exception e) {
            startActivity(AppUtils.getOpenUrlIntent("https://www.linkedin.com/company/lalaland-pk/about/"));
        }
    }

    //method to get the right URL to use in the intent
    public String getFacebookPageURL() {
        PackageManager packageManager = getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }

    public Intent getOpenFacebookIntent() {
        try {
            getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/2096149290642572"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.facebook.com/lalaland.pk"));
        }
    }
}
