package com.lalaland.ecommerce.views.activities;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.databinding.ActivityWebViewBinding;

import static com.lalaland.ecommerce.helpers.AppConstants.BLOGS;
import static com.lalaland.ecommerce.helpers.AppConstants.BLOG_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.FAQ_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.PRODUCT_TYPE;
import static com.lalaland.ecommerce.helpers.AppConstants.RETURN_POLICY_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.TERMS_AND_CONDITIONS_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.URL_TYPE;

public class WebViewActivity extends AppCompatActivity {

    ActivityWebViewBinding activityWebViewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityWebViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_web_view);

        loadWeb();

        activityWebViewBinding.ivBackArrow.setOnClickListener(v -> {
            finish();
        });
    }

    private void loadWeb() {

        switch (URL_TYPE) {

            // blog
            case 1:
                loadWebView(BLOGS);
                break;

            // refund
            case 2:
                loadWebView(RETURN_POLICY_URL);
                break;

            // terms and conditions
            case 3:
                loadWebView(TERMS_AND_CONDITIONS_URL);
                break;

            // FAQs
            case 4:
                loadWebView(FAQ_URL);
                break;

            default:
                String url;

                if (getIntent().getExtras() != null) {
                    url = getIntent().getStringExtra(BLOG_URL);
                    loadWebView(url);
                }


        }
    }

    private void loadWebView(String url) {

        activityWebViewBinding.wvWebsite.getSettings().setJavaScriptEnabled(true); // enable javascript


        activityWebViewBinding.wvWebsite.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(WebViewActivity.this, description, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                // Redirect to deprecated method, so you can use it in all SDK versions
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
            }
        });

        activityWebViewBinding.wvWebsite.loadUrl(url);

        activityWebViewBinding.wvWebsite.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                activityWebViewBinding.pbLoading.setVisibility(View.GONE);
            }
        });
    }
}
