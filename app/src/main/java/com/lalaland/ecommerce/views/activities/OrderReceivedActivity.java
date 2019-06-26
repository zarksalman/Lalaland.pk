package com.lalaland.ecommerce.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.adapters.RecommendedProductsAdapter;
import com.lalaland.ecommerce.data.models.products.Product;
import com.lalaland.ecommerce.databinding.ActivityOrderReceivedBinding;
import com.lalaland.ecommerce.helpers.AppConstants;
import com.lalaland.ecommerce.helpers.AppUtils;

import java.util.ArrayList;
import java.util.List;

import static com.lalaland.ecommerce.helpers.AppConstants.ORDER_TOTAL;
import static com.lalaland.ecommerce.helpers.AppConstants.PRODUCT_ID;

public class OrderReceivedActivity extends AppCompatActivity implements RecommendedProductsAdapter.ProductListener {

    private ActivityOrderReceivedBinding activityOrderReceivedBinding;
    private List<Product> recommendedProductList = new ArrayList<>();
    private String totalBill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityOrderReceivedBinding = DataBindingUtil.setContentView(this, R.layout.activity_order_received);

        totalBill = getIntent().getStringExtra(ORDER_TOTAL);
        recommendedProductList = getIntent().getParcelableArrayListExtra("recommended_products");

        setInitValues();

        activityOrderReceivedBinding.btnBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void setInitValues() {

        activityOrderReceivedBinding.tvTotalAmount.setText(AppUtils.formatPriceString(totalBill));
        activityOrderReceivedBinding.setListener(this);

        setAdapter();
    }

    public void trackOrder(View view) {

        AppConstants.LOAD_HOME_FRAGMENT_INDEX = 4;
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void continueShopping(View view) {

        AppConstants.LOAD_HOME_FRAGMENT_INDEX = 0;
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }


    private void setAdapter() {

        RecommendedProductsAdapter productAdapter = new RecommendedProductsAdapter(this, this);

        activityOrderReceivedBinding.rvRecommendedProducts.setHasFixedSize(true);
        activityOrderReceivedBinding.rvRecommendedProducts.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        activityOrderReceivedBinding.rvRecommendedProducts.setAdapter(productAdapter);
        productAdapter.setData(recommendedProductList);
    }

    @Override
    public void onProductProductClicked(Product product) {
        AppConstants.LOAD_HOME_FRAGMENT_INDEX = 0;
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(PRODUCT_ID, product.getId());
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {

        AppConstants.LOAD_HOME_FRAGMENT_INDEX = 0;
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
