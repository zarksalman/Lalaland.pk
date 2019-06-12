package com.lalaland.ecommerce.views.activities;

import android.os.Bundle;
import android.view.View;
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

import static com.lalaland.ecommerce.helpers.AppConstants.SEARCHES;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;

public class GlobalSearchActivity extends AppCompatActivity implements SearchAdapter.SearchListener, SearchProductAdapter.SearchListener {

    private ActivityGlobalSearchBinding activityGlobalSearchBinding;
    private AppPreference appPreference;
    private StringBuilder searches = new StringBuilder();
    private String[] globalSearches;
    List<String> mGlobalSearches = new ArrayList<>();
    private SearchAdapter searchAdapter;
    private SearchProductAdapter searchProductAdapter;

    private ProductViewModel productViewModel;
    private List<SearchProduct> searchProducts = new ArrayList<>();
    private List<SearchCategory> searchCategories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityGlobalSearchBinding = DataBindingUtil.setContentView(this, R.layout.activity_global_search);

        appPreference = AppPreference.getInstance(this);
        searches.append(appPreference.getString(SEARCHES));

        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);

        setHistoryAdapter();
        setAdapter();

        activityGlobalSearchBinding.svGlobalSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchAdapter.filter(query);

                if (query.isEmpty()) {
                    activityGlobalSearchBinding.recentSearches.setVisibility(View.VISIBLE);
                    searchProducts.clear();
                } else {
                    callToApi(query);
                    activityGlobalSearchBinding.rvSearchProducts.setVisibility(View.GONE);
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchAdapter.filter(newText);

                if (newText.isEmpty() && !searches.toString().isEmpty()) {
                    activityGlobalSearchBinding.recentSearches.setVisibility(View.VISIBLE);
                    searchProducts.clear();
                    searchAdapter.notifyDataSetChanged();
                }
                return true;
            }
        });

    }

    private void setHistoryAdapter() {

        if (!searches.toString().isEmpty()) {
            activityGlobalSearchBinding.emptyState.setVisibility(View.GONE);
            globalSearches = searches.toString().split(",");
            //   mGlobalSearches = Arrays.asList(globalSearches);

            for (int i = 0; i < globalSearches.length; i++) {
                mGlobalSearches.add(globalSearches[i]);
            }

            activityGlobalSearchBinding.recentSearches.setVisibility(View.VISIBLE);
        }

        searchAdapter = new SearchAdapter(this, this);
        activityGlobalSearchBinding.rvSearches.setLayoutManager(new LinearLayoutManager(this));
        activityGlobalSearchBinding.rvSearches.setAdapter(searchAdapter);
        searchAdapter.setData(mGlobalSearches);

    }

    private void setAdapter() {

        searchProductAdapter = new SearchProductAdapter(this, this);
        searchProductAdapter.setData(searchCategories);
        activityGlobalSearchBinding.rvSearchProducts.setHasFixedSize(true);
        activityGlobalSearchBinding.rvSearchProducts.setAdapter(searchProductAdapter);
        activityGlobalSearchBinding.rvSearchProducts.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, true));
    }

    private void callToApi(String queryString) {

        activityGlobalSearchBinding.recentSearches.setVisibility(View.GONE);

        productViewModel.searchItems(queryString).observe(this, searchDataContainer -> {

            if (searchDataContainer.getCode().equals(SUCCESS_CODE)) {
                searchProducts.clear();
                searchCategories.clear();

                searchProducts.addAll(searchDataContainer.getData().getProduct());
                searchCategories.addAll(searchDataContainer.getData().getCategory());

                createModelsForAdapter();

                if (searchProducts.size() > 0) {

                    activityGlobalSearchBinding.emptyState.setVisibility(View.GONE);
                    activityGlobalSearchBinding.rvSearchProducts.setVisibility(View.VISIBLE);

                    if (!searches.toString().isEmpty()) {
                        searches.append(",");
                        mGlobalSearches.add(queryString);
                        searchAdapter.notifyItemInserted(mGlobalSearches.size());
                    }

                    searches.append(queryString);

                    appPreference.setString(SEARCHES, searches.toString());
                } else {

                    Toast.makeText(this, "Items Not Found", Toast.LENGTH_SHORT).show();
                    searchCategories.clear();
                }

                searchProductAdapter.setData(searchCategories);
                searchProductAdapter.notifyDataSetChanged();
            }
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
    public void onSearchClicked(String search) {

        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSearchDelete(String search) {

        int index = mGlobalSearches.indexOf(search);
        mGlobalSearches.remove(search);
        searchAdapter.notifyItemRemoved(index);
    }

    @Override
    public void onSearchClicked(SearchCategory search) {

    }

    @Override
    public void onSearchDelete(SearchCategory search) {

    }
}
