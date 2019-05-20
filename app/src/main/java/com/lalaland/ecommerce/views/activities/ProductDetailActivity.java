package com.lalaland.ecommerce.views.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.data.models.productDetails.ProductDetailDataContainer;
import com.lalaland.ecommerce.data.models.productDetails.ProductVariation;
import com.lalaland.ecommerce.databinding.ActivityProductDetailBinding;
import com.lalaland.ecommerce.helpers.AppPreference;
import com.lalaland.ecommerce.viewModels.products.ProductViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lalaland.ecommerce.helpers.AppConstants.ADD_TO_CART;
import static com.lalaland.ecommerce.helpers.AppConstants.ADD_TO_WISH_LIST;
import static com.lalaland.ecommerce.helpers.AppConstants.CART_SESSION_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.GENERAL_ERROR;
import static com.lalaland.ecommerce.helpers.AppConstants.IS_WISH_LIST;
import static com.lalaland.ecommerce.helpers.AppConstants.ITEM_SOLD;
import static com.lalaland.ecommerce.helpers.AppConstants.PRODUCT_ID;
import static com.lalaland.ecommerce.helpers.AppConstants.PRODUCT_STORAGE_BASE_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.PRODUCT_VARIATION_ID;
import static com.lalaland.ecommerce.helpers.AppConstants.QUANTITY;
import static com.lalaland.ecommerce.helpers.AppConstants.REMOVE_FROM_WISH_LIST;
import static com.lalaland.ecommerce.helpers.AppConstants.SERVER_ERROR;
import static com.lalaland.ecommerce.helpers.AppConstants.SIGNIN_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;
import static com.lalaland.ecommerce.helpers.AppConstants.VALIDATION_FAIL_CODE;

public class ProductDetailActivity extends AppCompatActivity {

    private ActivityProductDetailBinding activityProductDetailBinding;
    private ProductDetailDataContainer mProductDetailDataContainer;
    private List<ProductVariation> mProductVariation;
    private ProductViewModel productViewModel;
    private Map<String, String> parameter = new HashMap<>();
    private Map<String, String> parameter1 = new HashMap<>();
    private Map<String, String> headers = new HashMap<>();
    private int product_id, variation_id, quantity;
    private String productImage;
    private String loginToken;
    private String cartSessionToken;
    private AppPreference appPreference;

    private boolean addToCart = false, addToWishList = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityProductDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_product_detail);
        activityProductDetailBinding.setListener(this);

        appPreference = AppPreference.getInstance(this);
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);

        product_id = getIntent().getIntExtra(PRODUCT_ID, 0);
        getProductDetail();

        activityProductDetailBinding.setListener(this);
    }

    void loadProductDetail() {
        productImage = PRODUCT_STORAGE_BASE_URL + mProductDetailDataContainer.getData().getProductDetails().getPrimaryImage();
        variation_id = mProductVariation.get(mProductVariation.size() - 1).getId();
        quantity = 1;

        // setting cart related data
        loginToken = appPreference.getString(SIGNIN_TOKEN);
        cartSessionToken = appPreference.getString(CART_SESSION_TOKEN);

        headers.put(SIGNIN_TOKEN, loginToken);
        headers.put(CART_SESSION_TOKEN, cartSessionToken);

        Glide
                .with(this)
                .load(productImage)
                .placeholder(R.drawable.placeholder_products)
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

        addToCart = true;
        parameter.clear();

       /* product_id = 2494;
        variation_id = 6395;
        quantity = 5;*/

        parameter.put(PRODUCT_VARIATION_ID, String.valueOf(variation_id));
        parameter.put(PRODUCT_ID, String.valueOf(product_id));
        parameter.put(QUANTITY, String.valueOf(quantity));

        productViewModel.addToCart(headers, parameter).observe(this, basicResponse -> {

            if (basicResponse != null) {
                if (basicResponse.getCode().equals(SUCCESS_CODE)) {
                    Toast.makeText(this, ADD_TO_CART, Toast.LENGTH_SHORT).show();
                    Log.d("AddToCart", ADD_TO_CART);
                } else if (basicResponse.getCode().equals(VALIDATION_FAIL_CODE)) {
                    Toast.makeText(this, ITEM_SOLD, Toast.LENGTH_SHORT).show();
                    Log.d("AddToCart", ITEM_SOLD);
                } else {
                    Toast.makeText(this, GENERAL_ERROR, Toast.LENGTH_SHORT).show();
                    Log.d("AddToCart", GENERAL_ERROR);
                }
            } else
                Toast.makeText(this, GENERAL_ERROR, Toast.LENGTH_SHORT).show();

            addToCart = false;

        });
    }

    public void addRemoveToWishList(View view, int isAddOrRemove) {

        addToWishList = true;

        parameter1.clear();

        parameter.put(PRODUCT_ID, String.valueOf(product_id));
        parameter.put(IS_WISH_LIST, String.valueOf(isAddOrRemove));

        productViewModel.addRemoveToWishList(headers, parameter1).observe(this, basicResponse -> {

            if (basicResponse != null) {
                if (basicResponse.getCode().equals(SUCCESS_CODE)) {
                    if (isAddOrRemove == 1)
                        Toast.makeText(this, ADD_TO_WISH_LIST, Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(this, REMOVE_FROM_WISH_LIST, Toast.LENGTH_SHORT).show();
                } else if (basicResponse.getCode().equals(VALIDATION_FAIL_CODE)) {
                    if (isAddOrRemove == 1)
                        Toast.makeText(this, SERVER_ERROR, Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(this, SERVER_ERROR, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, GENERAL_ERROR, Toast.LENGTH_SHORT).show();
                }

            } else
                Toast.makeText(this, GENERAL_ERROR, Toast.LENGTH_SHORT).show();

        });
    }

    void initParameter() {
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
