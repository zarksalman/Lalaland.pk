package com.lalaland.ecommerce.views.activities;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.view.View;
import android.widget.Toast;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.data.models.actionProducs.ActionProducts;
import com.lalaland.ecommerce.data.models.cart.CartProduct;
import com.lalaland.ecommerce.data.models.productDetails.ProductDetailDataContainer;
import com.lalaland.ecommerce.data.models.productDetails.ProductVariation;
import com.lalaland.ecommerce.databinding.ActivityProductDetailBinding;
import com.lalaland.ecommerce.helpers.AppConstants;
import com.lalaland.ecommerce.viewModels.products.ProductViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lalaland.ecommerce.helpers.AppConstants.ADD_TO_CART;
import static com.lalaland.ecommerce.helpers.AppConstants.ADD_TO_WISH_LIST;
import static com.lalaland.ecommerce.helpers.AppConstants.GENERAL_ERROR;
import static com.lalaland.ecommerce.helpers.AppConstants.IS_WISH_LIST;
import static com.lalaland.ecommerce.helpers.AppConstants.PRODUCT_ID;
import static com.lalaland.ecommerce.helpers.AppConstants.PRODUCT_STORAGE_BASE_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.PRODUCT_VARIATION_ID;
import static com.lalaland.ecommerce.helpers.AppConstants.QUANTITY;
import static com.lalaland.ecommerce.helpers.AppConstants.REMOVE_FROM_WISH_LIST;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;

public class ProductDetailActivity extends AppCompatActivity {

    private ActivityProductDetailBinding activityProductDetailBinding;
    private ProductDetailDataContainer mProductDetailDataContainer;
    private List<ProductVariation> mProductVariation;
    private ProductViewModel productViewModel;
    private Map<String, String> parameter = new HashMap<>();
    private int product_id, variation_id, quantity;
    private String productImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityProductDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_product_detail);
        activityProductDetailBinding.setListener(this);

        product_id = getIntent().getIntExtra(PRODUCT_ID, 0);
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        getProductDetail();

        activityProductDetailBinding.setListener(this);
    }

    void loadProductDetail() {
        productImage = PRODUCT_STORAGE_BASE_URL + mProductDetailDataContainer.getData().getProductDetails().getPrimaryImage();
        variation_id = mProductVariation.get(0).getId();
        quantity = 2;

        Glide
                .with(this)
                .load(productImage)
                .into(activityProductDetailBinding.ivProduct);
    }

    void getProductDetail() {

        productViewModel.getProductDetail(product_id).observe(this, productDetailDataContainer -> {

            if (productDetailDataContainer != null) {

                if (productDetailDataContainer.getCode().equals(SUCCESS_CODE)) {
                    mProductDetailDataContainer = productDetailDataContainer;
                    mProductVariation = productDetailDataContainer.getData().getProductVariations();

                    loadProductDetail();
                }
            }
        });
    }

    public void AddToCart(View view) {

        parameter.clear();

        parameter.put(PRODUCT_VARIATION_ID, String.valueOf(variation_id));
        parameter.put(PRODUCT_ID, String.valueOf(product_id));
        parameter.put(QUANTITY, String.valueOf(2));

        productViewModel.addToCart(parameter).observe(this, basicResponse -> {

            if (basicResponse.getCode().equals(SUCCESS_CODE)) {
                Toast.makeText(this, ADD_TO_CART, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, GENERAL_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addRemoveToWishList(View view, int isAddOrRemove) {

        parameter.clear();

        parameter.put(PRODUCT_ID, String.valueOf(product_id));
        parameter.put(IS_WISH_LIST, String.valueOf(isAddOrRemove));


        // is_wish_list = true to add to list and is_wish_list = false to remove from list
        productViewModel.addRemoveToWishList(parameter).observe(this, basicResponse -> {

            if (basicResponse.getCode().equals(SUCCESS_CODE)) {
                if (isAddOrRemove == 0)
                    Toast.makeText(this, ADD_TO_WISH_LIST, Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, REMOVE_FROM_WISH_LIST, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, GENERAL_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }

    void initParameter() {
    }
}
