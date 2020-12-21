package com.lalaland.ecommerce.views.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.adapters.FitAndSizingAdapter;
import com.lalaland.ecommerce.adapters.ProductColorAdapter;
import com.lalaland.ecommerce.adapters.ProductImageAdapter;
import com.lalaland.ecommerce.adapters.ProductVariationAdapter;
import com.lalaland.ecommerce.adapters.ReviewRatingAdapter;
import com.lalaland.ecommerce.data.models.productDetails.FitAndSizing;
import com.lalaland.ecommerce.data.models.productDetails.LinkedProduct;
import com.lalaland.ecommerce.data.models.productDetails.ProductDetailData;
import com.lalaland.ecommerce.data.models.productDetails.ProductDetailDataContainer;
import com.lalaland.ecommerce.data.models.productDetails.ProductDetails;
import com.lalaland.ecommerce.data.models.productDetails.ProductMultimedium;
import com.lalaland.ecommerce.data.models.productDetails.ProductReview;
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
import java.util.Timer;
import java.util.TimerTask;

import static com.lalaland.ecommerce.helpers.AppConstants.ACTION_ID;
import static com.lalaland.ecommerce.helpers.AppConstants.ACTION_NAME;
import static com.lalaland.ecommerce.helpers.AppConstants.ADD_TO_CART;
import static com.lalaland.ecommerce.helpers.AppConstants.BASE_URL_PRODUCT_SHARE;
import static com.lalaland.ecommerce.helpers.AppConstants.BRANDS_IN_FOCUS_PRODUCTS;
import static com.lalaland.ecommerce.helpers.AppConstants.CART_SESSION_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.IS_WISH_LIST;
import static com.lalaland.ecommerce.helpers.AppConstants.ITEM_SOLD;
import static com.lalaland.ecommerce.helpers.AppConstants.LOAD_HOME_FRAGMENT_INDEX;
import static com.lalaland.ecommerce.helpers.AppConstants.PRODUCT_ID;
import static com.lalaland.ecommerce.helpers.AppConstants.PRODUCT_STORAGE_BASE_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.PRODUCT_TYPE;
import static com.lalaland.ecommerce.helpers.AppConstants.PRODUCT_VARIATION_ID;
import static com.lalaland.ecommerce.helpers.AppConstants.QUANTITY;
import static com.lalaland.ecommerce.helpers.AppConstants.SIGNIN_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.SIZE_CHART_STORAGE_BASE_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;
import static com.lalaland.ecommerce.helpers.AppConstants.VALIDATION_FAIL_CODE;

public class ProductDetailActivity extends AppCompatActivity implements ProductVariationAdapter.ProductVariationListener {

    private ActivityProductDetailBinding activityProductDetailBinding;
    private ProductDetailDataContainer mProductDetailDataContainer;
    private List<ProductVariation> mProductVariation = new ArrayList<>();
    private List<ProductMultimedium> mProductMultimedia = new ArrayList<>();
    private List<FitAndSizing> mFitAndSizings = new ArrayList<>();
    private List<ProductReview> mProductReviews = new ArrayList<>();

    private List<LinkedProduct> mLinkedProducts = new ArrayList<>();
    private ProductViewModel productViewModel;
    private OrderViewModel orderViewModel;

    private String productImage;
    private boolean isOutOfStock = false;
    Intent intent, dataIntent;
    ProductDetailData productDetailData;
    private Map<String, String> parameter = new HashMap<>();
    private Map<String, String> deliverOptionparameter = new HashMap<>();
    private Map<String, String> headers = new HashMap<>();
    private int product_id, variation_id, quantity = 1;
    private String loginToken;
    private String cartSessionToken;
    private String generalDescription;
    private String materialDescription;
    private AppPreference appPreference;
    private ProductDetails productDetails;
    private ProuctDetailBottomSheetLayoutBinding prouctDetailBottomSheetLayoutBinding;
    private boolean isBuyNow = false;
    private List<ImageView> dots = new ArrayList<>();
    private int isAddOrRemove;
    private BottomSheetDialog mBottomSheetDialog;
    private ProductVariationAdapter productVariationAdapter;
    String productShareUrl;
    StringBuilder price = new StringBuilder();
    StringBuilder aPrice = new StringBuilder();
    Bundle bundle = new Bundle();
    boolean isDeeplink;
    String imgUrl = "";
    ReviewRatingAdapter reviewRatingAdapter;
    Float avgRating = 0f;
    int currentPage = 0;

    Handler handler = new Handler();
    Runnable runnable;
    Timer timer = new Timer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityProductDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_product_detail);
        activityProductDetailBinding.setListener(this);

        appPreference = AppPreference.getInstance(this);
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        product_id = getIntent().getIntExtra(PRODUCT_ID, 0);

        activityProductDetailBinding.svProductDetail.setVisibility(View.GONE);
        activityProductDetailBinding.pbLoading.setVisibility(View.VISIBLE);

        intent = new Intent(this, MainActivity.class);

        isDeeplink = appPreference.getBoolean("is_deep_link");

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
            productShareUrl += AppUtils.createProductUrl(mProductDetailDataContainer.getData());
            Log.d("product_urls", productShareUrl);
            startActivity(AppUtils.getProductShareIntent(productShareUrl));
        });

        activityProductDetailBinding.btnBack.setOnClickListener(v -> {
            onBackPressed();
        });

        activityProductDetailBinding.loginContainer.setOnClickListener(v -> {
            startActivityForResult(new Intent(this, RegistrationActivity.class), 300);
        });

        activityProductDetailBinding.tvBrandName.setOnClickListener(v -> {
            intent = new Intent(this, ActionProductListingActivity.class);
            intent.putExtra(ACTION_NAME, productDetails.getBrandName());
            intent.putExtra(PRODUCT_TYPE, BRANDS_IN_FOCUS_PRODUCTS);
            intent.putExtra(ACTION_ID, String.valueOf(productDetails.getBrandId()));
            startActivity(intent);
        });

        activityProductDetailBinding.btnSubmitReview.setOnClickListener(v -> submitReview());
    }

    void loadProductDetail() {

        if (loginToken.isEmpty())
            activityProductDetailBinding.loginContainer.setVisibility(View.VISIBLE);
        else
            activityProductDetailBinding.loginContainer.setVisibility(View.GONE);

        activityProductDetailBinding.tvCounter.setText(String.valueOf(AppConstants.CART_COUNTER));

        //setting viewpagger height because in scrollview wrap/match does not calculate their height correctly
        android.view.Display display = ((android.view.WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        activityProductDetailBinding.vpImages.getLayoutParams().height = ((int) (display.getHeight() * 0.74));
        activityProductDetailBinding.vpImages.getLayoutParams().width = ((int) (display.getWidth() * 1.0));

        ProductImageAdapter productImageAdapter = new ProductImageAdapter(this, mProductMultimedia, productDetails.getProductMultiMediaDesciption());
        activityProductDetailBinding.vpImages.setAdapter(productImageAdapter);

        productImage = PRODUCT_STORAGE_BASE_URL + productDetails.getPrimaryImage();

        // setting cart related data
        loginToken = appPreference.getString(SIGNIN_TOKEN);
        cartSessionToken = appPreference.getString(CART_SESSION_TOKEN);
        headers.put(SIGNIN_TOKEN, loginToken);
        headers.put(CART_SESSION_TOKEN, cartSessionToken);

        addDots();
        setupAutoPager();

        if (productDetails.getIsWishListItem() != null) {
            activityProductDetailBinding.btnAddToWish.setImageResource(R.drawable.wish_list_filled_icon);
            isAddOrRemove = 1;  // setting initial showing that it is added to list
        } else {
            isAddOrRemove = 0; // setting initial showing that it is not added to list
        }

        activityProductDetailBinding.tvProductName.setText(productDetails.getName());
        activityProductDetailBinding.tvBrandName.setText(productDetails.getBrandName());
        setPrice();
        setRating();
        setProductColors();
        generalDescription = productDetails.getGeneralDescription();
        materialDescription = productDetails.getMaterialDescription();

        setProductGeneralDescription(generalDescription, materialDescription);
        setFitAndSizing();

        activityProductDetailBinding.tvProductCode.setText(String.valueOf(productDetails.getProductNum()));
        activityProductDetailBinding.tvSoldByMerchant.setText(productDetails.getMerchantName());

        setReviewsAdapter();

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
        price = new StringBuilder();
        aPrice = new StringBuilder();
        maxActualPrice = Double.parseDouble(productDetails.getMaxActualPrice());
        maxSalePrice = Double.parseDouble(productDetails.getMaxSalePrice());

        minActualPrice = Double.parseDouble(productDetails.getMinActualPrice());
        minSalePrice = Double.parseDouble(productDetails.getMinSalePrice());

        if (maxSalePrice > minSalePrice) {

            price.append(minSalePrice);
            price.append("-");
            price.append(maxSalePrice);

            try {
                activityProductDetailBinding.tvProductPrice.setText(AppUtils.formatPriceString(price.toString()));
            } catch (NumberFormatException e) {

                activityProductDetailBinding.tvProductPrice.setText(price.toString());
                e.printStackTrace();
            }

        } else {

            price.append(minSalePrice);

            try {
                activityProductDetailBinding.tvProductPrice.setText(AppUtils.formatPriceString(price.toString()));
            } catch (NumberFormatException e) {

                activityProductDetailBinding.tvProductPrice.setText(price.toString());
                e.printStackTrace();
            }
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

    void setRating() {

        avgRating = mProductDetailDataContainer.getData().getRatingAverage();
        activityProductDetailBinding.rbProductRatings.setRating(avgRating);
    }

    void setProductColors() {

        if (mLinkedProducts.size() < 1) {
            activityProductDetailBinding.tvProductColor.setVisibility(View.GONE);
            return;
        }

        ProductColorAdapter productColorAdapter = new ProductColorAdapter(this, linkedProduct -> {
            timer.cancel();
            activityProductDetailBinding.pbLoading.setVisibility(View.VISIBLE);
            product_id = linkedProduct.getProductId();
            getProductDetail();
        });

        activityProductDetailBinding.rvProductColors.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        activityProductDetailBinding.rvProductColors.setHasFixedSize(true);
        activityProductDetailBinding.rvProductColors.setAdapter(productColorAdapter);
        productColorAdapter.setData(mLinkedProducts);
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

                    productDetailData = mProductDetailDataContainer.getData();
                    mProductVariation = productDetailDataContainer.getData().getProductVariations();
                    mProductMultimedia = productDetailDataContainer.getData().getProductMultimedia();
                    mFitAndSizings = mProductDetailDataContainer.getData().getFitAndSizing();
                    productDetails = mProductDetailDataContainer.getData().getProductDetails();
                    mLinkedProducts = productDetailDataContainer.getData().getLinkedProducts();
                    mProductReviews = mProductDetailDataContainer.getData().getProductReviews();

                    for (int i = 0; i < mProductMultimedia.size(); i++) {
                        mProductMultimedia.get(i).setMediaDescription(productDetails.getProductMultiMediaDesciption());
                    }

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

                        activityProductDetailBinding.btnBuyNow.setText("Sold Out");
                        activityProductDetailBinding.btnBuyNow.setBackgroundColor(getResources().getColor(R.color.colorMediumGray));
                        activityProductDetailBinding.btnBuyNow.setOnClickListener(null);

                    }

                    activityProductDetailBinding.setListener(this);
                }
            }

            AppUtils.unBlockUi(this);
        });
    }

    void setReviewsAdapter() {

        reviewRatingAdapter = new ReviewRatingAdapter(this);
        activityProductDetailBinding.rvReview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        reviewRatingAdapter.setData(mProductReviews);
        activityProductDetailBinding.rvReview.setAdapter(reviewRatingAdapter);

    }

    void submitReview() {

        activityProductDetailBinding.pbLoading.setVisibility(View.VISIBLE);
        AppUtils.blockUi(this);

        Map<String, String> parameter = new HashMap<>();
        String review = activityProductDetailBinding.etReview.getText().toString().trim();
        String rating = String.valueOf(activityProductDetailBinding.rbUserRating.getRating());

        parameter.put("product_id", String.valueOf(product_id));
        parameter.put("user_review", review);
        parameter.put("user_rating", rating);

        productViewModel.submitReview(parameter).observe(this, productReviewsDataContainer -> {

            activityProductDetailBinding.pbLoading.setVisibility(View.GONE);
            AppUtils.unBlockUi(this);

            if (productReviewsDataContainer.getCode().equals(SUCCESS_CODE)) {

                activityProductDetailBinding.rbUserRating.setRating(0);
                activityProductDetailBinding.etReview.setText("");

                mProductReviews = productReviewsDataContainer.getData().getProductReviews();
                reviewRatingAdapter.setData(mProductReviews);
                reviewRatingAdapter.notifyDataSetChanged();

                avgRating = productReviewsDataContainer.getData().getRatingAverage();
                activityProductDetailBinding.rbProductRatings.setRating(avgRating);

            } else if (productReviewsDataContainer.getCode().equals(VALIDATION_FAIL_CODE)) {
                Toast.makeText(this, productReviewsDataContainer.getMsg(), Toast.LENGTH_SHORT).show();
                parameter.clear();
            }
        });

        reviewRatingAdapter.setData(mProductReviews);
        reviewRatingAdapter.notifyDataSetChanged();
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
                    Toast.makeText(this, basicResponse.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
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
                        } else {

                            AnalyticsManager.getInstance().sendAnalytics("remove_from_wishlist", bundle);
                            AnalyticsManager.getInstance().sendFacebookAnalytics("remove_from_wishlist", bundle);

                            activityProductDetailBinding.btnAddToWish.setImageResource(R.drawable.wish_list_icon);
                            activityProductDetailBinding.btnAddToWish.setBackground(getResources().getDrawable(R.drawable.bg_round_corner_white));
                        }
                    } else if (basicResponse.getCode().equals(VALIDATION_FAIL_CODE)) {
                        Toast.makeText(this, basicResponse.getMsg(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, basicResponse.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                }
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
            Toast.makeText(this, R.string.insufficient_stock, Toast.LENGTH_SHORT).show();
        });

        if (mProductDetailDataContainer.getData().getSizeChart() != null) {
            if (!mProductDetailDataContainer.getData().getSizeChart().isEmpty()) {

                prouctDetailBottomSheetLayoutBinding.tvProductSize.setVisibility(View.VISIBLE);
                prouctDetailBottomSheetLayoutBinding.ivForwardArrow.setVisibility(View.VISIBLE);

                if (mProductDetailDataContainer.getData().getSizeChart() != null)
                    imgUrl = SIZE_CHART_STORAGE_BASE_URL.concat(mProductDetailDataContainer.getData().getSizeChart());

                View.OnClickListener onClickListener = v -> {

                    Intent mIntent = new Intent(this, ZoomInZoomOutActivity.class);
                    mIntent.putExtra("size_chart", imgUrl);
                    startActivity(mIntent);
                };

                prouctDetailBottomSheetLayoutBinding.tvProductSize.setOnClickListener(onClickListener);
                prouctDetailBottomSheetLayoutBinding.ivForwardArrow.setOnClickListener(onClickListener);
            }
        }
    }

    public void addDots() {

        dots = new ArrayList<>();

        if (activityProductDetailBinding.dots.getChildCount() > 0)
            activityProductDetailBinding.dots.removeAllViews();

        if (mProductMultimedia.size() < 2) {
            activityProductDetailBinding.dots.setVisibility(View.GONE);
            return;
        }

        for (int i = 0; i <= mProductMultimedia.size() - 1; i++) {
            ImageView dot = new ImageView(this);
            dot.setScaleType(ImageView.ScaleType.CENTER_CROP);
            dot.setImageDrawable(getResources().getDrawable(R.drawable.ic_sliders_empty_icon));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.rightMargin = 8;
            params.leftMargin = 8;

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
                currentPage = position;
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
            int drawableId = (i == idx) ? (R.drawable.ic_sliders_fill_icon) : (R.drawable.ic_sliders_empty_icon);
            Drawable drawable = res.getDrawable(drawableId);
            dots.get(i).setImageDrawable(drawable);
        }
    }

    private void setupAutoPager() {

        runnable = () -> {

            activityProductDetailBinding.vpImages.setCurrentItem(currentPage, true);
            if (currentPage == mProductMultimedia.size()) {
                currentPage = 0;
            } else {

                ++currentPage;
            }
        };


        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(runnable);
            }
        }, 5000, 5000);
    }

    @Override
    public void onBackPressed() {

        /* if user comes from order receive activity, user has finish all activities
         * or if user came from deep link to see products multiple times
         */

        if (isLastActivity() || isDeeplink) {

            isDeeplink = false;
            appPreference.setBoolean("is_deep_link", isDeeplink);
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

            quantity = 1;
            prouctDetailBottomSheetLayoutBinding.tvCount.setText("1");
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
            } else if (requestCode == 300) {
                loginToken = appPreference.getString(SIGNIN_TOKEN);
                activityProductDetailBinding.loginContainer.setVisibility(View.GONE);
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        startActivity(intent);
        finish();
    }
}
