package com.lalaland.ecommerce.views.activities;

import android.os.Bundle;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.tabs.TabLayout;
import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.adapters.ActionProductsAdapter;
import com.lalaland.ecommerce.data.models.actionProducs.ActionProducts;
import com.lalaland.ecommerce.databinding.ActivityProductListingBinding;
import com.lalaland.ecommerce.databinding.SortFilterBottomSheetLayoutBinding;
import com.lalaland.ecommerce.viewModels.products.ActionProductViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lalaland.ecommerce.helpers.AppConstants.ACTION_ID;
import static com.lalaland.ecommerce.helpers.AppConstants.ACTION_NAME;

public class ProductListingActivity extends AppCompatActivity implements ActionProductsAdapter.ActionProductsListener {

    private ActivityProductListingBinding activityProductListingBinding;
    private ActionProductViewModel actionProductViewModel;
    ActionProductsAdapter actionProductsAdapter;
    private List<ActionProducts> actionProductsArrayList = new ArrayList<>();
    BottomSheetDialog mBottomSheetDialog;
    SortFilterBottomSheetLayoutBinding sheetView;
    Map<String, String> parameter = new HashMap<>();
    String action_name = "custom_list", action_id = "2";
    private static final String ID = "id";
    private static final String SORT_BY = "sort_by";

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activityProductListingBinding = DataBindingUtil.setContentView(this, R.layout.activity_product_listing);

        if (getIntent().getExtras() != null) {
            action_name = getIntent().getStringExtra(ACTION_NAME);
            action_id = getIntent().getStringExtra(ACTION_ID);
        }
        
        parameter.put(ID, action_id);

        actionProductViewModel = ViewModelProviders.of(this).get(ActionProductViewModel.class);
        setListeners();
        setActionProducts();
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

            }
        });
    }

    public void setBottomSheet(boolean isFilter) {


        mBottomSheetDialog = new BottomSheetDialog(ProductListingActivity.this);


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
                parameter.put("sort_by", "newest");
                break;

            case R.id.tv_oldest:
                parameter.put("sort_by", "oldest");
                break;

            case R.id.tv_low_to_high:
                parameter.put("sort_by", "price_asc");
                break;

            case R.id.tv_high_to_low:
                parameter.put("sort_by", "price_desc");
                break;
        }

        parameter.put(ID, action_id);
        setActionProducts();
        mBottomSheetDialog.hide();
        showList();

    }

    private void setActionProducts() {


        actionProductViewModel.getActionProducts("sale", parameter).observe(this, actionProductsContainer -> {

            if (actionProductsContainer != null && actionProductsContainer.getData().getProducts().size() > 0) {
                actionProductsArrayList = new ArrayList<>();
                actionProductsArrayList = actionProductsContainer.getData().getProducts();

                activityProductListingBinding.rvProducts.setLayoutManager(new GridLayoutManager(this, 2));

                actionProductsAdapter = new ActionProductsAdapter(this, this);
                activityProductListingBinding.rvProducts.setAdapter(actionProductsAdapter);
                actionProductsAdapter.setData(actionProductsArrayList);

                activityProductListingBinding.pbLoadingActionProducts.setVisibility(View.GONE);
                activityProductListingBinding.rvProducts.setVisibility(View.VISIBLE);

            }

        });

    }

    @Override
    public void onActionProductClicked(ActionProducts actionProducts) {

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
