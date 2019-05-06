package com.lalaland.ecommerce.views.activities;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.view.View;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.databinding.ActivityProductDetailBinding;

public class ProductDetailActivity extends AppCompatActivity {

    ActivityProductDetailBinding activityProductDetailBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityProductDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_product_detail);

        activityProductDetailBinding.setListener(this);
    }

    public void AddToCart(View view) {

    }

    public void AddToWishList(View view) {
    }
}
