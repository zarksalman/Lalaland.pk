package com.lalaland.ecommerce.views.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.adapters.FitAndSizingAdapter;
import com.lalaland.ecommerce.adapters.ProductImageAdapter;
import com.lalaland.ecommerce.adapters.ProductVariationAdapter;
import com.lalaland.ecommerce.data.models.productDetails.FitAndSizing;
import com.lalaland.ecommerce.data.models.productDetails.ProductDetailDataContainer;
import com.lalaland.ecommerce.data.models.productDetails.ProductDetails;
import com.lalaland.ecommerce.data.models.productDetails.ProductMultimedium;
import com.lalaland.ecommerce.data.models.productDetails.ProductVariation;
import com.lalaland.ecommerce.databinding.ActivityProductDetailBinding;
import com.lalaland.ecommerce.databinding.ProuctDetailBottomSheetLayoutBinding;
import com.lalaland.ecommerce.helpers.AnalyticsManager;
import com.lalaland.ecommerce.helpers.AppConstants;
import com.lalaland.ecommerce.helpers.AppPreference;
import com.lalaland.ecommerce.helpers.AppUtils;
import com.lalaland.ecommerce.viewModels.order.OrderViewModel;
import com.lalaland.ecommerce.viewModels.products.ProductViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lalaland.ecommerce.helpers.AppConstants.ADD_TO_CART;
import static com.lalaland.ecommerce.helpers.AppConstants.BASE_URL_PRODUCT_SHARE;
import static com.lalaland.ecommerce.helpers.AppConstants.CART_SESSION_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.GENERAL_ERROR;
import static com.lalaland.ecommerce.helpers.AppConstants.IS_WISH_LIST;
import static com.lalaland.ecommerce.helpers.AppConstants.ITEM_SOLD;
import static com.lalaland.ecommerce.helpers.AppConstants.LOAD_HOME_FRAGMENT_INDEX;
import static com.lalaland.ecommerce.helpers.AppConstants.PRODUCT_ID;
import static com.lalaland.ecommerce.helpers.AppConstants.PRODUCT_STORAGE_BASE_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.PRODUCT_VARIATION_ID;
import static com.lalaland.ecommerce.helpers.AppConstants.QUANTITY;
import static com.lalaland.ecommerce.helpers.AppConstants.SIGNIN_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;
import static com.lalaland.ecommerce.helpers.AppConstants.VALIDATION_FAIL_CODE;

public class ProductDetailActivity extends AppCompatActivity implements ProductVariationAdapter.ProductVariationListener {

    private ActivityProductDetailBinding activityProductDetailBinding;
    private ProductDetailDataContainer mProductDetailDataContainer;
    private List<ProductVariation> mProductVariation = new ArrayList<>();
    private List<ProductMultimedium> mProductMultimedia = new ArrayList<>();
    private List<FitAndSizing> mFitAndSizings = new ArrayList<>();
    private ProductViewModel productViewModel;
    private OrderViewModel orderViewModel;

    private Map<String, String> parameter = new HashMap<>();
    private Map<String, String> deliverOptionparameter = new HashMap<>();
    private Map<String, String> headers = new HashMap<>();
    private int product_id, variation_id, quantity = 1;
    private String productImage;
    private String loginToken;
    private String cartSessionToken;
    private String generalDescription;
    private String materialDescription;
    private AppPreference appPreference;
    private ProductDetails productDetails;
    private ProuctDetailBottomSheetLayoutBinding prouctDetailBottomSheetLayoutBinding;
    private boolean isBuyNow = false;
    private boolean isOutOfStock = false;
    private List<ImageView> dots = new ArrayList<>();
    private int isAddOrRemove;
    private BottomSheetDialog mBottomSheetDialog;
    private ProductVariationAdapter productVariationAdapter;
    String productShareUrl;
    Intent intent;
    StringBuilder price = new StringBuilder();
    StringBuilder aPrice = new StringBuilder();
    Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityProductDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_product_detail);
        activityProductDetailBinding.setListener(this);

        appPreference = AppPreference.getInstance(this);
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        orderViewModel = ViewModelProviders.of(this).get(OrderViewModel.class);

        product_id = getIntent().getIntExtra(PRODUCT_ID, 0);
        Log.d(AppConstants.TAG, "product_id" + product_id);
        activityProductDetailBinding.svProductDetail.setVisibility(View.GONE);
        activityProductDetailBinding.pbLoading.setVisibility(View.VISIBLE);

        intent = new Intent(this, MainActivity.class);

        loginToken = appPreference.getString(SIGNIN_TOKEN);
        cartSessionToken = appPreference.getString(CART_SESSION_TOKEN);

        getProductDetail();

        activityProductDetailBinding.cityContainer.setOnClickListener(v -> {
            startActivityForResult(new Intent(this, SelectCityActivity.class), 200);
        });

        activityProductDetailBinding.cartCounterContainer.setOnClickListener(v -> {

            LOAD_HOME_FRAGMENT_INDEX = 2;
            intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        activityProductDetailBinding.ivCart.setOnClickListener(v -> {

            LOAD_HOME_FRAGMENT_INDEX = 2;
            intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        activityProductDetailBinding.ivShareProduct.setOnClickListener(v -> {
            productShareUrl = BASE_URL_PRODUCT_SHARE;
            productShareUrl = productShareUrl.concat(AppUtils.createProductUrl(mProductDetailDataContainer.getData().getCategoryName().getName()));
            productShareUrl = productShareUrl.concat("/").concat(AppUtils.createProductUrl(productDetails.getBrandName()));
            productShareUrl = productShareUrl.concat("/").concat(AppUtils.createProductUrl(productDetails.getName()));
            productShareUrl = productShareUrl.concat("/").concat(AppUtils.createProductUrl(String.valueOf(productDetails.getId())));

            startActivity(AppUtils.getProductShareIntent(productShareUrl));
        });

        activityProductDetailBinding.btnBack.setOnClickListener(v -> {
            onBackPressed();
        });

    }

    void loadProductDetail() {

        activityProductDetailBinding.tvCounter.setText(String.valueOf(AppConstants.CART_COUNTER));

        //setting viewpagger height because in scrollview wrap/match does not calculate their height correctly
        android.view.Display display = ((android.view.WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        activityProductDetailBinding.vpImages.getLayoutParams().height = ((int) (display.getHeight() * 0.65));
        activityProductDetailBinding.vpImages.getLayoutParams().width = ((int) (display.getWidth() * 1.0));


        ProductImageAdapter productImageAdapter = new ProductImageAdapter(this, mProductMultimedia);
        activityProductDetailBinding.vpImages.setAdapter(productImageAdapter);

        productImage = PRODUCT_STORAGE_BASE_URL + productDetails.getPrimaryImage();

        // setting cart related data
        loginToken = appPreference.getString(SIGNIN_TOKEN);
        cartSessionToken = appPreference.getString(CART_SESSION_TOKEN);
        headers.put(SIGNIN_TOKEN, loginToken);
        headers.put(CART_SESSION_TOKEN, cartSessionToken);

        addDots();

//        Double isWishList = (Double) productDetails.getIsWishListItem();

        if (productDetails.getIsWishListItem() != null) {
            activityProductDetailBinding.btnAddToWish.setImageResource(R.drawable.wish_list_filled_icon);
            isAddOrRemove = 1;  // setting initial showing that it is added to list
        } else {
            isAddOrRemove = 0; // setting initial showing that it is not added to list
        }

        activityProductDetailBinding.tvProductName.setText(productDetails.getName());
        activityProductDetailBinding.tvBrandName.setText(productDetails.getBrandName());
        setPrice();

        generalDescription = productDetails.getGeneralDescription();
        materialDescription = productDetails.getMaterialDescription();

        setProductGeneralDescription(generalDescription, materialDescription);
        setFitAndSizing();

        activityProductDetailBinding.tvProductCode.setText(String.valueOf(productDetails.getProductNum()));
        activityProductDetailBinding.tvSoldByMerchant.setText(productDetails.getMerchantName());

        activityProductDetailBinding.pbLoading.setVisibility(View.GONE);
        activityProductDetailBinding.svProductDetail.setVisibility(View.VISIBLE);

    }

    private void setFitAndSizing() {

        // removing items that have null description
        List<FitAndSizing> tempFitAndSize = new ArrayList<>(mFitAndSizings);
        for (FitAndSizing fitAndSizing : mFitAndSizings) {

            if (fitAndSizing.getDescription() == null) {
                tempFitAndSize.remove(fitAndSizing);
            }
        }

        FitAndSizingAdapter fitAndSizingAdapter = new FitAndSizingAdapter(this);
        activityProductDetailBinding.rvFitAndSize.setHasFixedSize(true);
        activityProductDetailBinding.rvFitAndSize.setAdapter(fitAndSizingAdapter);
        activityProductDetailBinding.rvFitAndSize.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        fitAndSizingAdapter.setData(tempFitAndSize);
    }

    void setPrice() {


        Double maxSalePrice, maxActualPrice, minSalePrice, minActualPrice;

        maxActualPrice = Double.parseDouble(productDetails.getMaxActualPrice());
        maxSalePrice = Double.parseDouble(productDetails.getMaxSalePrice());

        minActualPrice = Double.parseDouble(productDetails.getMinActualPrice());
        minSalePrice = Double.parseDouble(productDetails.getMinSalePrice());

        if (maxSalePrice > minSalePrice) {

            price.append(minSalePrice);
            price.append("-");
            price.append(maxSalePrice);

            activityProductDetailBinding.tvProductPrice.setText(AppUtils.formatPriceString(price.toString()));
        } else {

            price.append(minSalePrice);
            activityProductDetailBinding.tvProductPrice.setText(AppUtils.formatPriceString(price.toString()));
        }

        if (maxActualPrice > minActualPrice) {

            aPrice.append(minActualPrice);
            aPrice.append("-");
            aPrice.append(maxActualPrice);

            activityProductDetailBinding.tvProductActualPrice.setText(AppUtils.formatPriceString(aPrice.toString()));
            AppUtils.showSalePrice(activityProductDetailBinding.tvProductActualPrice);
        } else {

            aPrice.append(minActualPrice);
            activityProductDetailBinding.tvProductActualPrice.setText(AppUtils.formatPriceString(aPrice.toString()));
            AppUtils.showSalePrice(activityProductDetailBinding.tvProductActualPrice);
        }

        if (minActualPrice > minSalePrice) {
            activityProductDetailBinding.tvProductActualPrice.setVisibility(View.VISIBLE);
            AppUtils.showSalePrice(activityProductDetailBinding.tvProductActualPrice);
        } else {
            activityProductDetailBinding.tvProductActualPrice.setVisibility(View.GONE);

        }

    }

    void setProductGeneralDescription(String generalDescription, String materialDescription) {

        activityProductDetailBinding.wvProductGeneralDetail.loadData(generalDescription, "text/html", "UTF-8");
        activityProductDetailBinding.wvProductMaterialDetail.loadData(materialDescription, "text/html", "UTF-8");


        if (generalDescription == null || generalDescription.isEmpty()) {
            activityProductDetailBinding.wvProductGeneralDetail.setVisibility(View.GONE);
            activityProductDetailBinding.tvProductGeneralDetailTitle.setVisibility(View.GONE);
        } else {
            activityProductDetailBinding.wvProductGeneralDetail.setVisibility(View.VISIBLE);
            activityProductDetailBinding.tvProductGeneralDetailTitle.setVisibility(View.VISIBLE);

            activityProductDetailBinding.wvProductGeneralDetail.loadData(generalDescription, "text/html", "UTF-8");

        }

        if (materialDescription == null || materialDescription.isEmpty()) {
            activityProductDetailBinding.wvProductMaterialDetail.setVisibility(View.GONE);
            activityProductDetailBinding.tvProductMaterialDetailTitle.setVisibility(View.GONE);
        } else {
            activityProductDetailBinding.wvProductMaterialDetail.setVisibility(View.VISIBLE);
            activityProductDetailBinding.tvProductMaterialDetailTitle.setVisibility(View.VISIBLE);

            activityProductDetailBinding.wvProductMaterialDetail.loadData(materialDescription, "text/html", "UTF-8");
        }
    }

    void getProductDetail() {

        AppUtils.blockUi(this);

        productViewModel.getProductDetail(product_id).observe(this, productDetailDataContainer -> {

            if (productDetailDataContainer != null) {

                if (productDetailDataContainer.getCode().equals(SUCCESS_CODE)) {
                    mProductDetailDataContainer = productDetailDataContainer;

                    mProductVariation = productDetailDataContainer.getData().getProductVariations();
                    mProductMultimedia.addAll(productDetailDataContainer.getData().getProductMultimedia());
                    mFitAndSizings.addAll(mProductDetailDataContainer.getData().getFitAndSizing());
                    productDetails = mProductDetailDataContainer.getData().getProductDetails();


                    initBottomSheet();
                    loadProductDetail();

                    bundle.putString("id", String.valueOf(product_id));

                    if (getFirstSelectedVariationIndex() != -1) {
                        bundle.putString("variation_id", String.valueOf(mProductVariation.get(getFirstSelectedVariationIndex()).getId()));
                    }

                    bundle.putString("price", productDetails.getMinSalePrice());

                    bundle.putString("brand_name", productDetails.getBrandName());

                    AnalyticsManager.getInstance().sendAnalytics("view_item", bundle);
                    AnalyticsManager.getInstance().sendFacebookAnalytics("Content View", bundle);

                    if (variation_id == -1) {
                        prouctDetailBottomSheetLayoutBinding.btnDone.setText("Sold Out");
                        prouctDetailBottomSheetLayoutBinding.btnDone.setBackground(getResources().getDrawable(R.drawable.bg_round_corner_gray));
                    }

                    activityProductDetailBinding.setListener(this);
                }
            }

            AppUtils.unBlockUi(this);
        });
    }

    private int getFirstSelectedVariationIndex() {

        for (int i = 0; i < mProductVariation.size(); i++) {

            if (AppUtils.toInteger(mProductVariation.get(i).getRemainingQuantity()) > 0) {

                // if user does not select any variation then first will be selected if quantity available
                variation_id = mProductVariation.get(i).getId();
                return i;
            }
        }

        // all variations sold out
        variation_id = -1;
        return variation_id;
    }

    public void AddToCart(View view) {

        if (variation_id == -1)
            return;

        AppUtils.blockUi(this);
        activityProductDetailBinding.pbLoading.setVisibility(View.VISIBLE);

        quantity = Integer.parseInt(prouctDetailBottomSheetLayoutBinding.tvCount.getText().toString());

        parameter.clear();

        parameter.put(PRODUCT_VARIATION_ID, String.valueOf(variation_id));
        parameter.put(PRODUCT_ID, String.valueOf(product_id));
        parameter.put(QUANTITY, String.valueOf(quantity));

        hideBottomSheet();


        productViewModel.addToCart(headers, parameter).observe(this, basicResponse -> {

            if (basicResponse != null) {
                if (basicResponse.getCode().equals(SUCCESS_CODE)) {

                    AnalyticsManager.getInstance().sendAnalytics("add_to_cart", bundle);
                    AnalyticsManager.getInstance().sendFacebookAnalytics("Add to Cart", bundle);

                    if (isBuyNow) {

                        AppConstants.LOAD_HOME_FRAGMENT_INDEX = 2; // setting cart fragment index to load
                        intent = new Intent(this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {

                        activityProductDetailBinding.tvCounter.setText(String.valueOf(AppConstants.CART_COUNTER));
                        Toast.makeText(this, ADD_TO_CART, Toast.LENGTH_SHORT).show();
                        Log.d("AddToCart", ADD_TO_CART);
                    }
                } else if (basicResponse.getCode().equals(VALIDATION_FAIL_CODE)) {
                    Toast.makeText(this, ITEM_SOLD, Toast.LENGTH_SHORT).show();
                    Log.d("AddToCart", ITEM_SOLD);
                } else {
                    Toast.makeText(this, GENERAL_ERROR, Toast.LENGTH_SHORT).show();
                    Log.d("AddToCart", GENERAL_ERROR);
                }
            } else
                Toast.makeText(this, GENERAL_ERROR, Toast.LENGTH_SHORT).show();

            AppUtils.unBlockUi(this);
            activityProductDetailBinding.pbLoading.setVisibility(View.GONE);
        });
    }

    public void addRemoveToWishList(View view) {

        if (loginToken.isEmpty()) {
            Toast.makeText(this, "Please login to add to wishlist", Toast.LENGTH_SHORT).show();
            intent = new Intent(this, RegistrationActivity.class);
            startActivityForResult(intent, 100);
            return;
        } else {

            AppUtils.blockUi(this);
            activityProductDetailBinding.pbLoading.setVisibility(View.VISIBLE);

            if (isAddOrRemove == 1)
                isAddOrRemove = 0;
            else
                isAddOrRemove = 1;

            parameter.clear();

            parameter.put(PRODUCT_ID, String.valueOf(product_id));
            parameter.put(IS_WISH_LIST, String.valueOf(isAddOrRemove));

            productViewModel.addRemoveToWishList(headers, parameter).observe(this, basicResponse -> {

                if (basicResponse != null) {
                    if (basicResponse.getCode().equals(SUCCESS_CODE)) {
                        Toast.makeText(this, basicResponse.getMsg(), Toast.LENGTH_SHORT).show();

                        if (isAddOrRemove == 1) {

                            AnalyticsManager.getInstance().sendAnalytics("add_to_wish_list", bundle);
                            AnalyticsManager.getInstance().sendFacebookAnalytics("Add to Wishlist", bundle);

                            activityProductDetailBinding.btnAddToWish.setImageResource(R.drawable.wish_list_filled_icon);
                            // activityProductDetailBinding.btnAddToWish.setBackground(getResources().getDrawable(R.drawable.bg_round_corner_white_accent));
                        } else {

                            AnalyticsManager.getInstance().sendAnalytics("remove_from_wishlist", bundle);
                            AnalyticsManager.getInstance().sendFacebookAnalytics("remove_from_wishlist", bundle);

                            activityProductDetailBinding.btnAddToWish.setImageResource(R.drawable.wish_list_icon);
                            activityProductDetailBinding.btnAddToWish.setBackground(getResources().getDrawable(R.drawable.bg_round_corner_white));
                        }
                    } else if (basicResponse.getCode().equals(VALIDATION_FAIL_CODE)) {
                        Toast.makeText(this, basicResponse.getMsg(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, GENERAL_ERROR, Toast.LENGTH_SHORT).show();
                    }

                } else
                    Toast.makeText(this, GENERAL_ERROR, Toast.LENGTH_SHORT).show();

                activityProductDetailBinding.pbLoading.setVisibility(View.GONE);


                AppUtils.unBlockUi(this);

            });
        }
    }

    public void setBottomSheet(View view, boolean isBuyNow) {

        if (activityProductDetailBinding.pbLoading.getVisibility() == View.VISIBLE)
            return;

        // is buy now or just add to cart
        this.isBuyNow = isBuyNow;
        mBottomSheetDialog.show();

    }

    public void hideBottomSheet() {
        mBottomSheetDialog.hide();
    }

    void initBottomSheet() {

        mBottomSheetDialog = new BottomSheetDialog(ProductDetailActivity.this);
        prouctDetailBottomSheetLayoutBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.prouct_detail_bottom_sheet_layout, null, false);
        prouctDetailBottomSheetLayoutBinding.setVariationListener(this);
        mBottomSheetDialog.setContentView(prouctDetailBottomSheetLayoutBinding.getRoot());

        // if all variations are not out of stock if true then select first's price
        if (getFirstSelectedVariationIndex() != -1) {
            prouctDetailBottomSheetLayoutBinding.tvProductSalePrice.setText(AppUtils.formatPriceString(mProductVariation.get(getFirstSelectedVariationIndex()).getSalePrice()));
            prouctDetailBottomSheetLayoutBinding.tvProductActualPrice.setText(AppUtils.formatPriceString(mProductVariation.get(getFirstSelectedVariationIndex()).getActualPrice()));

        } else {
            prouctDetailBottomSheetLayoutBinding.tvProductSalePrice.setText(AppUtils.formatPriceString(mProductVariation.get(0).getSalePrice()));
            prouctDetailBottomSheetLayoutBinding.tvProductActualPrice.setText(AppUtils.formatPriceString(mProductVariation.get(0).getActualPrice()));
        }
        AppUtils.showSalePrice(prouctDetailBottomSheetLayoutBinding.tvProductActualPrice);


        // variation_id = first by default selected item
        productVariationAdapter = new ProductVariationAdapter(this, this, getFirstSelectedVariationIndex());
        productVariationAdapter.setData(mProductVariation);
        prouctDetailBottomSheetLayoutBinding.rvVariation.setAdapter(productVariationAdapter);

        prouctDetailBottomSheetLayoutBinding.rvVariation.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        prouctDetailBottomSheetLayoutBinding.rvVariation.setHasFixedSize(true);


        prouctDetailBottomSheetLayoutBinding.setProductDetails(productDetails);

        prouctDetailBottomSheetLayoutBinding.btnSub.setOnClickListener(v -> {

            if (quantity > 1) {
                quantity--;
                prouctDetailBottomSheetLayoutBinding.tvCount.setText(String.valueOf(quantity));
            } else
                Toast.makeText(this, "Quantity could not be less than 1", Toast.LENGTH_SHORT).show();
        });

        prouctDetailBottomSheetLayoutBinding.btnAdd.setOnClickListener(v -> {

            for (int i = 0; i < mProductVariation.size(); i++) {
                if (variation_id == mProductVariation.get(i).getId()) {

                    if (quantity < Integer.parseInt(mProductVariation.get(i).getRemainingQuantity())) {
                        quantity++;
                        prouctDetailBottomSheetLayoutBinding.tvCount.setText(String.valueOf(quantity));
                        return;
                    }
                }
            }

            Toast.makeText(this, "Insufficient Stock", Toast.LENGTH_SHORT).show();
        });
    }

    public void addDots() {

        if (mProductMultimedia.size() < 2) {
            activityProductDetailBinding.dots.setVisibility(View.GONE);
            return;
        }

        for (int i = 0; i <= mProductMultimedia.size() - 1; i++) {
            ImageView dot = new ImageView(this);
            dot.setImageDrawable(getResources().getDrawable(R.drawable.empty_circle));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            activityProductDetailBinding.dots.addView(dot, params);

            dots.add(dot);
        }

        selectDot(0);

        activityProductDetailBinding.vpImages.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                selectDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public void selectDot(int idx) {
        Resources res = getResources();
        for (int i = 0; i <= mProductMultimedia.size() - 1; i++) {
            int drawableId = (i == idx) ? (R.drawable.filled_circle) : (R.drawable.empty_circle);
            Drawable drawable = res.getDrawable(drawableId);
            dots.get(i).setImageDrawable(drawable);
        }
    }

    @Override
    public void onBackPressed() {

        // if user comes from order receive activity, user has finish all activities
        if (isLastActivity()) {
            AppConstants.LOAD_HOME_FRAGMENT_INDEX = 0;
            intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else
            finish();
    }

    boolean isLastActivity() {

        ActivityManager mngr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);

        if (taskList.get(0).numActivities == 1 &&
                taskList.get(0).topActivity.getClassName().equals(this.getClass().getName())) {

            Log.i(AppConstants.TAG, "This is last activity in the stack");
            return true;
        } else
            return false;
    }

    @Override
    public void onProductVariationClicked(ProductVariation productVariation) {

        if (AppUtils.toInteger(productVariation.getRemainingQuantity()) > 0) {

            bundle.putString("variation_id", String.valueOf(productVariation.getId()));
            bundle.putString("price", String.valueOf(productVariation.getSalePrice()));

            variation_id = productVariation.getId();
            prouctDetailBottomSheetLayoutBinding.tvProductSalePrice.setText(AppUtils.formatPriceString(productVariation.getSalePrice()));
            prouctDetailBottomSheetLayoutBinding.tvProductActualPrice.setText(AppUtils.formatPriceString(productVariation.getActualPrice()));
        }

        Log.d(AppConstants.TAG, "onProductVariationClicked:" + variation_id);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                loginToken = appPreference.getString(SIGNIN_TOKEN);
                headers.put(SIGNIN_TOKEN, loginToken);
                addRemoveToWishList(activityProductDetailBinding.getRoot());
            } else if (requestCode == 200) {

                if (data != null) {

                    getDeliveryOption(data);
                }
            }
        }
    }

    private void getDeliveryOption(Intent intent) {

        activityProductDetailBinding.pbLoading.setVisibility(View.VISIBLE);

        deliverOptionparameter.put("merchant_id", String.valueOf(productDetails.getMerchantId()));
        deliverOptionparameter.put("product_id", String.valueOf(productDetails.getId()));
        deliverOptionparameter.put("city_id", String.valueOf(intent.getStringExtra("city_id")));

        orderViewModel.getDeliveryOption(deliverOptionparameter).observe(this, deliveryOptionDataContainer -> {

            if (deliveryOptionDataContainer != null) {

                String cityName = intent.getStringExtra("city_name");
                String deliverCharges = String.valueOf(deliveryOptionDataContainer.getData().getToReturn().getAmount());

                activityProductDetailBinding.tvDeliveryCharges.setText(String.format(getResources().getString(R.string.delivery_charges_option), deliverCharges));
                activityProductDetailBinding.tvCityName.setText(cityName);
                activityProductDetailBinding.tvDeliveryCharges.setVisibility(View.VISIBLE);
            }

            activityProductDetailBinding.pbLoading.setVisibility(View.GONE);
        });
    }
}
