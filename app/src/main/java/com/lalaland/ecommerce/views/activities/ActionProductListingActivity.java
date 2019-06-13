package com.lalaland.ecommerce.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.adapters.ActionProductsAdapter;
import com.lalaland.ecommerce.data.models.actionProducs.ActionProducts;
import com.lalaland.ecommerce.databinding.ActivityProductListingBinding;
import com.lalaland.ecommerce.databinding.SortFilterBottomSheetLayoutBinding;
import com.lalaland.ecommerce.helpers.AppConstants;
import com.lalaland.ecommerce.viewModels.products.ProductViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lalaland.ecommerce.helpers.AppConstants.ACTION_ID;
import static com.lalaland.ecommerce.helpers.AppConstants.ACTION_NAME;
import static com.lalaland.ecommerce.helpers.AppConstants.BRANDS_IN_FOCUS_PRODUCTS;
import static com.lalaland.ecommerce.helpers.AppConstants.CATEGORY_PRODUCTS;
import static com.lalaland.ecommerce.helpers.AppConstants.CUSTOM_LIST_PRODUCTS;
import static com.lalaland.ecommerce.helpers.AppConstants.LENGTH;
import static com.lalaland.ecommerce.helpers.AppConstants.NEW_ARRIVAL_PRODUCTS;
import static com.lalaland.ecommerce.helpers.AppConstants.PICK_OF_THE_WEEK_PRODUCTS;
import static com.lalaland.ecommerce.helpers.AppConstants.PRODUCT_ID;
import static com.lalaland.ecommerce.helpers.AppConstants.PRODUCT_TYPE;
import static com.lalaland.ecommerce.helpers.AppConstants.SALE_PRODUCT;
import static com.lalaland.ecommerce.helpers.AppConstants.START_INDEX;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;

public class ActionProductListingActivity extends AppCompatActivity implements ActionProductsAdapter.ActionProductsListener {

    private ActivityProductListingBinding activityProductListingBinding;
    private ProductViewModel productViewModel;
    ActionProductsAdapter actionProductsAdapter;
    private List<ActionProducts> actionProductsArrayList = new ArrayList<>();
    BottomSheetDialog mBottomSheetDialog;
    SortFilterBottomSheetLayoutBinding sheetView;
    Map<String, String> parameter = new HashMap<>();
    String action_name = "custom_list", action_id = "-1", products_type = "action_products", category_name;
    private static final String ID = "id";
    private static final String SORT_BY = "sort_by";
    GridLayoutManager gridLayoutManager;
    private boolean isScrolling;
    int start = 0, length = 20, size = 20;
    String sortBy = "az";

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activityProductListingBinding = DataBindingUtil.setContentView(this, R.layout.activity_product_listing);

        if (getIntent().getExtras() != null) {

            products_type = getIntent().getStringExtra(PRODUCT_TYPE);
            category_name = getIntent().getStringExtra(ACTION_NAME);

            activityProductListingBinding.tvCategoryTitle.setText(category_name);
            switch (products_type) {

                // actions types
                case SALE_PRODUCT:

                    parameter.clear();
                    action_name = "sale";
                    action_id = getIntent().getStringExtra(ACTION_ID);
                    parameter.put(ID, action_id);

                    break;

                case NEW_ARRIVAL_PRODUCTS:
                    action_name = "newArrivals";
                    action_id = getIntent().getStringExtra(ACTION_ID);
                    parameter.put(ID, action_id);

                    break;

                case CATEGORY_PRODUCTS:
                    action_name = "categoryProducts";
                    action_id = getIntent().getStringExtra(ACTION_ID);
                    parameter.put(ID, action_id);

                    break;

                case CUSTOM_LIST_PRODUCTS:
                    action_name = "customListProducts";
                    action_id = getIntent().getStringExtra(ACTION_ID);
                    parameter.put(ID, action_id);

                    break;

                // category types
                case PICK_OF_THE_WEEK_PRODUCTS:

                    action_name = "productsPicksOfTheWeek";
                    parameter.put(ID, action_id);
                    break;

                case BRANDS_IN_FOCUS_PRODUCTS:
                    action_name = "brand";
                    action_id = getIntent().getStringExtra(ACTION_ID);
                    parameter.put(ID, action_id);
                    break;
            }

            parameter.put(SORT_BY, sortBy);
        }

        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        setListeners();
    }


    void setListeners() {

        activityProductListingBinding.setProductListingListener(this);

        activityProductListingBinding.tlSortFilter.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == 0) {

                    setBottomSheet(false);
                } else if (tab.getPosition() == 1) {
                    setBottomSheet(true);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {

                    setBottomSheet(false);
                } else if (tab.getPosition() == 1) {
                    setBottomSheet(true);
                }
            }
        });


        activityProductListingBinding.rvProducts.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int currentItems = gridLayoutManager.getChildCount();
                int totalItems = gridLayoutManager.getItemCount();
                int scrollOutItems = gridLayoutManager.findFirstVisibleItemPosition();

                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {

                    activityProductListingBinding.pbLoadingProducts.setVisibility(View.VISIBLE);

                    isScrolling = false;

                    setActionProducts();
                }
            }
        });

        parameter.put(START_INDEX, String.valueOf(start));
        parameter.put(LENGTH, String.valueOf(length));

        setAdapter();
        setActionProducts();
    }

    private void setAdapter() {


        gridLayoutManager = new GridLayoutManager(this, 2);
        activityProductListingBinding.rvProducts.setLayoutManager(gridLayoutManager);

        actionProductsAdapter = new ActionProductsAdapter(this, this);
        activityProductListingBinding.rvProducts.setAdapter(actionProductsAdapter);
        actionProductsAdapter.setData(actionProductsArrayList);
    }

    public void setBottomSheet(boolean isFilter) {


        mBottomSheetDialog = new BottomSheetDialog(ActionProductListingActivity.this);


        if (!isFilter) // if bottom sheet is filter
        {
            sheetView = DataBindingUtil.inflate(getLayoutInflater(), R.layout.sort_filter_bottom_sheet_layout, null, false);
        } else // if bottom sheet is sort
        {
            sheetView = DataBindingUtil.inflate(getLayoutInflater(), R.layout.sort_filter_bottom_sheet_layout, null, false);
        }

        sheetView.setSortSheetListener(this);
        mBottomSheetDialog.setContentView(sheetView.getRoot());
        mBottomSheetDialog.show();

        sheetView.ivSortIcon.setOnClickListener(v -> mBottomSheetDialog.hide());
    }

    public void bottomSheetClick(View view) {

        activityProductListingBinding.rvProducts.setVisibility(View.GONE);
        activityProductListingBinding.pbLoadingActionProducts.setVisibility(View.VISIBLE);
        parameter.clear();

        switch (view.getId()) {
            case R.id.tv_ascending_alphabetically:

                parameter.put(SORT_BY, "az");
                break;

            case R.id.tv_descending_alphabetically:
                parameter.put(SORT_BY, "za");
                break;

            case R.id.tv_newest:
                parameter.put(SORT_BY, "newest");
                break;

            case R.id.tv_oldest:
                parameter.put(SORT_BY, "oldest");
                break;

            case R.id.tv_low_to_high:
                parameter.put(SORT_BY, "price_asc");
                break;

            case R.id.tv_high_to_low:
                parameter.put(SORT_BY, "price_desc");
                break;
        }

        parameter.put(ID, action_id);
        actionProductsArrayList.clear();
        actionProductsAdapter.notifyDataSetChanged();
        setActionProducts();
        mBottomSheetDialog.hide();
        showList();

    }

    private void setActionProducts() {

        productViewModel.getActionProducts(action_name, parameter).observe(this, actionProductsContainer -> {

            if (actionProductsContainer != null) {

                if (actionProductsContainer.getCode().equals(SUCCESS_CODE)) {

                    if (actionProductsContainer.getData().getProducts().size() > 0) {

                        int startPosition = actionProductsArrayList.size();

                        actionProductsArrayList.addAll(actionProductsContainer.getData().getProducts());
                        //  actionProductsAdapter.notifyDataSetChanged();

                        actionProductsAdapter.notifyItemRangeInserted(startPosition, actionProductsArrayList.size());
                        activityProductListingBinding.rvProducts.setVisibility(View.VISIBLE);

                        start += size;
                        start++;

                        if (parameter.containsKey(SORT_BY)) {
                            sortBy = parameter.get(SORT_BY);
                        }

                        parameter.clear();

                        parameter.put(ID, action_id);
                        parameter.put(START_INDEX, String.valueOf(start));
                        parameter.put(LENGTH, String.valueOf(length));
                        parameter.put(SORT_BY, sortBy);
                    } else {
                        Toast.makeText(this, "Items not found", Toast.LENGTH_SHORT).show();
                    }
                    activityProductListingBinding.pbLoadingActionProducts.setVisibility(View.GONE);

                } else
                    Log.d(AppConstants.TAG, actionProductsContainer.getMsg());

                activityProductListingBinding.pbLoadingProducts.setVisibility(View.GONE);
            }

        });

    }

    @Override
    public void onActionProductClicked(ActionProducts actionProducts) {
        
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra(PRODUCT_ID, actionProducts.getId());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    void showList() {

        for (ActionProducts actionProducts : actionProductsArrayList) {

            Log.d("after_sort", "Name:" + actionProducts.getName() + "Price:" + actionProducts.getSalePrice());
            Log.d("after_sort", "Sales Price:" + actionProducts.getSalePrice());
            Log.d("after_sort", "Actual Price:" + actionProducts.getActualPrice());
        }
    }
}
