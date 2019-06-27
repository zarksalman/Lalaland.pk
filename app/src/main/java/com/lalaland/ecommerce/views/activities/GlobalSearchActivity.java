package com.lalaland.ecommerce.views.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.adapters.SearchAdapter;
import com.lalaland.ecommerce.adapters.SearchProductAdapter;
import com.lalaland.ecommerce.data.models.globalSearch.SearchCategory;
import com.lalaland.ecommerce.data.models.globalSearch.SearchProduct;
import com.lalaland.ecommerce.databinding.ActivityGlobalSearchBinding;
import com.lalaland.ecommerce.helpers.AppPreference;
import com.lalaland.ecommerce.viewModels.products.ProductViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.lalaland.ecommerce.helpers.AppConstants.ACTION_ID;
import static com.lalaland.ecommerce.helpers.AppConstants.ACTION_NAME;
import static com.lalaland.ecommerce.helpers.AppConstants.PRODUCT_ID;
import static com.lalaland.ecommerce.helpers.AppConstants.PRODUCT_TYPE;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;

public class GlobalSearchActivity extends AppCompatActivity implements SearchProductAdapter.SearchListener {

    private ActivityGlobalSearchBinding activityGlobalSearchBinding;
    private AppPreference appPreference;
    private StringBuilder searches = new StringBuilder();
    private String[] globalSearches;
    List<String> mGlobalSearches = new ArrayList<>();
    private SearchAdapter searchAdapter;
    private SearchProductAdapter searchProductAdapter, saveSearchAdapter;

    private ProductViewModel productViewModel;
    private List<SearchProduct> searchProducts = new ArrayList<>();
    private List<SearchCategory> searchCategories = new ArrayList<>();
    private List<SearchCategory> savedSearchCategories = new ArrayList<>();
    private boolean isHistory = true;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityGlobalSearchBinding = DataBindingUtil.setContentView(this, R.layout.activity_global_search);

        appPreference = AppPreference.getInstance(this);
        //searches.append(appPreference.getString(SEARCHES));

        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);

        // setting search bar text size
        LinearLayout linearLayout1 = (LinearLayout) activityGlobalSearchBinding.svGlobalSearch.getChildAt(0);
        LinearLayout linearLayout2 = (LinearLayout) linearLayout1.getChildAt(2);
        LinearLayout linearLayout3 = (LinearLayout) linearLayout2.getChildAt(1);
        AutoCompleteTextView autoComplete = (AutoCompleteTextView) linearLayout3.getChildAt(0);
        autoComplete.setTextSize(13);

        setHistoryAdapter();
        setAdapter();

        productViewModel.getAllSearchCategories().observe(this, searchCategories1 -> {

            if (searchCategories1.size() > 0) {
                savedSearchCategories.clear();
                savedSearchCategories.addAll(searchCategories1);
                saveSearchAdapter.notifyDataSetChanged();

                if (isHistory) {
                    activityGlobalSearchBinding.recentSearches.setVisibility(View.VISIBLE);
                    activityGlobalSearchBinding.emptyState.setVisibility(View.GONE);
                }
            } else {
                activityGlobalSearchBinding.recentSearches.setVisibility(View.GONE);
                activityGlobalSearchBinding.emptyState.setVisibility(View.VISIBLE);
            }
        });
        activityGlobalSearchBinding.svGlobalSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //searchAdapter.filter(query);

                if (query.isEmpty()) {

                    if (searchCategories.size() > 0)
                        activityGlobalSearchBinding.recentSearches.setVisibility(View.VISIBLE);
                    else
                        activityGlobalSearchBinding.emptyState.setVisibility(View.VISIBLE);

                    searchProducts.clear();
                    searchCategories.clear();

                } else {

                    activityGlobalSearchBinding.pbLoading.setVisibility(View.VISIBLE);
                    callToApi(query);
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //  searchAdapter.filter(newText);

                if (newText.isEmpty() && savedSearchCategories.size() > 0) {
                    activityGlobalSearchBinding.recentSearches.setVisibility(View.VISIBLE);
                    activityGlobalSearchBinding.rvSearchProducts.setVisibility(View.GONE);
                    searchCategories.clear();
                    saveSearchAdapter.notifyDataSetChanged();
                } else if (savedSearchCategories.size() < 1) {
                    activityGlobalSearchBinding.emptyState.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });

        activityGlobalSearchBinding.tvClearAll.setOnClickListener(v -> {

            if (savedSearchCategories.size() > 0)
                productViewModel.deleteAllSearches();
        });

        activityGlobalSearchBinding.ivBackArrow.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void setHistoryAdapter() {

        if (savedSearchCategories.size() > 0) {

            activityGlobalSearchBinding.emptyState.setVisibility(View.GONE);
            activityGlobalSearchBinding.recentSearches.setVisibility(View.VISIBLE);

        } else {
            activityGlobalSearchBinding.emptyState.setVisibility(View.VISIBLE);
        }

        saveSearchAdapter = new SearchProductAdapter(this, this, true);
        activityGlobalSearchBinding.rvSearches.setLayoutManager(new LinearLayoutManager(this));
        activityGlobalSearchBinding.rvSearches.setAdapter(saveSearchAdapter);
        saveSearchAdapter.setData(savedSearchCategories);
        saveSearchAdapter.notifyDataSetChanged();
    }

    private void setAdapter() {

        searchProductAdapter = new SearchProductAdapter(this, this, false);
        activityGlobalSearchBinding.rvSearchProducts.setHasFixedSize(true);
        activityGlobalSearchBinding.rvSearchProducts.setAdapter(searchProductAdapter);
        activityGlobalSearchBinding.rvSearchProducts.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        searchProductAdapter.setData(searchCategories);
    }

    private void callToApi(String queryString) {

        closeKeyboard();

        activityGlobalSearchBinding.recentSearches.setVisibility(View.GONE);
        activityGlobalSearchBinding.emptyState.setVisibility(View.GONE);

        productViewModel.searchItems(queryString).observe(this, searchDataContainer -> {

            if (searchDataContainer.getCode().equals(SUCCESS_CODE)) {
                searchProducts.clear();
                searchCategories.clear();

                searchProducts.addAll(searchDataContainer.getData().getProduct());
                searchCategories.addAll(searchDataContainer.getData().getCategory());

                createModelsForAdapter();

                if (searchCategories.size() > 0) {

                    activityGlobalSearchBinding.emptyState.setVisibility(View.GONE);
                    activityGlobalSearchBinding.rvSearchProducts.setVisibility(View.VISIBLE);

                } else {
                    Toast.makeText(this, "Items Not Found", Toast.LENGTH_SHORT).show();
                    searchCategories.clear();
                }

                searchProductAdapter.setData(searchCategories);
                searchProductAdapter.notifyDataSetChanged();
            }

            activityGlobalSearchBinding.pbLoading.setVisibility(View.GONE);
        });
    }

    private void createModelsForAdapter() {

        List<SearchCategory> categoryList = new ArrayList<>();
        SearchCategory searchCategory;

        for (SearchProduct searchProduct : searchProducts) {

            searchCategory = new SearchCategory();

            searchCategory.setId(searchProduct.getId());
            searchCategory.setName(searchProduct.getName());
            searchCategory.setRemainingQuantity(Integer.parseInt(searchProduct.getRemainingQuantity()));
            searchCategory.setParentId(-1); // -1 means it is not a category it is a product
            categoryList.add(searchCategory);
        }

        searchCategories.addAll(categoryList);

    }

    @Override
    public void onSearchProductClicked(int position, boolean isHistory) {

        this.isHistory = isHistory;
        SearchCategory searchCategory;

        if (isHistory) {
            searchCategory = savedSearchCategories.get(position);
        } else {
            searchCategory = searchCategories.get(position);
            productViewModel.insertSearch(searchCategory);
        }

        // if it is a category instead of product
        if (searchCategory.getParentId() != -1) {
            intent = new Intent(this, ActionProductListingActivity.class);
            intent.putExtra(ACTION_NAME, "Search Products");
            intent.putExtra(PRODUCT_TYPE, "category");
            intent.putExtra(ACTION_ID, String.valueOf(searchCategory.getId()));
        } else {

            intent = new Intent(this, ProductDetailActivity.class);
            intent.putExtra(PRODUCT_ID, searchCategory.getId());
        }

        startActivity(intent);
        //   Toast.makeText(this, "insert and show details", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onSearchProductDelete(int position, boolean isHistory) {

        this.isHistory = isHistory;

        if (isHistory) {
            SearchCategory searchCategory = savedSearchCategories.get(position);
            productViewModel.deleteSearch(searchCategory);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    void closeKeyboard() {
        View view = this.getCurrentFocus();

        if (view != null) {

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
