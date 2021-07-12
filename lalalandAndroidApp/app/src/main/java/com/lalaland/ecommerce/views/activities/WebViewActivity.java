package com.lalaland.ecommerce.views.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.data.models.products.Product;
import com.lalaland.ecommerce.databinding.ActivityWebViewBinding;
import com.lalaland.ecommerce.helpers.AppConstants;

import java.util.ArrayList;
import java.util.List;

import static com.lalaland.ecommerce.helpers.AppConstants.BLOGS;
import static com.lalaland.ecommerce.helpers.AppConstants.BLOG_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.CLICK_2_PAY_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.FAQ_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.ORDER_TOTAL;
import static com.lalaland.ecommerce.helpers.AppConstants.RETURN_POLICY_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.TERMS_AND_CONDITIONS_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.URL_TYPE;

public class WebViewActivity extends AppCompatActivity {

    ActivityWebViewBinding activityWebViewBinding;
    String url;
    String redirectUrl;
    private List<Product> recommendedProductList = new ArrayList<>();
    private String totalBill = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityWebViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_web_view);

        totalBill = getIntent().getStringExtra(ORDER_TOTAL);
        recommendedProductList = getIntent().getParcelableArrayListExtra("recommended_products");


        loadWeb();

        activityWebViewBinding.ivBackArrow.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    @Override
    public void onBackPressed() {
        if (redirectUrl == null) {
            AppConstants.LOAD_HOME_FRAGMENT_INDEX = 0;
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        finish();
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

            // FAQs
            case 5:

                if (getIntent().getExtras() != null) {
                    url = getIntent().getStringExtra(CLICK_2_PAY_URL);
                    redirectUrl = getIntent().getStringExtra(CLICK_2_PAY_URL);
                    loadWebView(url);
                }
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

        activityWebViewBinding.wvWebsite.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                activityWebViewBinding.pbLoading.setVisibility(View.GONE);

                // do your handling codes here, which url is the requested url
                // probably you need to open that url rather than redirect:
                view.loadUrl(url);
                Log.d("redirect_pay_url", url);

                if (redirectUrl != null) {
                    String redirectUrlHost = Uri.parse(Uri.parse(redirectUrl).getQueryParameter("callback_url")).getHost();
                    String urlHost = Uri.parse(url).getHost();

                    if (redirectUrlHost.equals(urlHost)) {

                        AppConstants.LOAD_HOME_FRAGMENT_INDEX = 0;
                        Intent intent = new Intent(WebViewActivity.this, OrderReceivedActivity.class);
                        intent.putExtra(ORDER_TOTAL, String.valueOf(totalBill));
                        intent.putParcelableArrayListExtra("recommended_products", (ArrayList<? extends Parcelable>) recommendedProductList);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }
                return false;
            }
        });
    }
}
