package com.lalaland.ecommerce.views.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;
import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.adapters.SearchAdapter;
import com.lalaland.ecommerce.adapters.SearchProductAdapter;
import com.lalaland.ecommerce.data.models.globalSearch.SearchCategory;
import com.lalaland.ecommerce.data.models.globalSearch.SearchDataContainer;
import com.lalaland.ecommerce.data.models.globalSearch.SearchProduct;
import com.lalaland.ecommerce.data.retrofit.RetrofitRxJavaClient;
import com.lalaland.ecommerce.databinding.ActivityGlobalSearchBinding;
import com.lalaland.ecommerce.helpers.AppConstants;
import com.lalaland.ecommerce.helpers.AppPreference;
import com.lalaland.ecommerce.viewModels.products.ProductViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

import static com.lalaland.ecommerce.helpers.AppConstants.ACTION_ID;
import static com.lalaland.ecommerce.helpers.AppConstants.ACTION_NAME;
import static com.lalaland.ecommerce.helpers.AppConstants.PRODUCT_ID;
import static com.lalaland.ecommerce.helpers.AppConstants.PRODUCT_TYPE;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;
import static com.lalaland.ecommerce.helpers.AppConstants.VALIDATION_FAIL_CODE;

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
    private boolean isReponseReceive = false;
    private Intent intent;
    private ImageView imageView;
    private CompositeDisposable disposable = new CompositeDisposable();
    private PublishSubject<String> publishSubject = PublishSubject.create();

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

        // setting searchview cross icon
        imageView = (ImageView) linearLayout3.getChildAt(1);
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.cross_icon));

        setHistoryAdapter();
        setAdapter();
        setSearchView();


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

       /* activityGlobalSearchBinding.etGlobalSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (activityGlobalSearchBinding.etGlobalSearch.getRight() - activityGlobalSearchBinding.etGlobalSearch.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        activityGlobalSearchBinding.etGlobalSearch.setText("");

                        return true;
                    }
                }
                return false;
            }
        });
*/
        DisposableObserver<SearchDataContainer> observer = getSearchObserver();

        disposable.add(
                publishSubject
                        .debounce(300, TimeUnit.MILLISECONDS)
                        .distinctUntilChanged()
                        .switchMapSingle(new Function<String, Single<SearchDataContainer>>() {
                            @Override
                            public Single<SearchDataContainer> apply(String s) throws Exception {
                                return RetrofitRxJavaClient.getInstance().createClient().globalRxSearch(s)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread());
                            }
                        })
                        .subscribeWith(observer));


           /*   .filter(textViewTextChangeEvent -> {
            if (textViewTextChangeEvent.text().toString().isEmpty()) {

                Drawable[] drawables = activityGlobalSearchBinding.etGlobalSearch.getCompoundDrawables();
                if (drawables[2] != null) {
                    drawables[2].mutate().setColorFilter(ContextCompat.getColor(this, android.R.color.transparent), PorterDuff.Mode.MULTIPLY);
                }
                return false;
            } else {

                Drawable[] drawables = activityGlobalSearchBinding.etGlobalSearch.getCompoundDrawables();
                if (drawables[2] != null) {
                    drawables[2].mutate().setColorFilter(ContextCompat.getColor(this, android.R.color.black), PorterDuff.Mode.MULTIPLY);
                }

                return true;
            }
        })*/
        // skipInitialValue() - skip for the first time when EditText empty
        disposable.add(
                RxTextView.textChangeEvents(activityGlobalSearchBinding.etGlobalSearch)
                        .skipInitialValue()
                        .debounce(300, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(searchContactsTextWatcher()));

        disposable.add(observer);

        // passing empty string fetches all the contacts
        publishSubject.onNext("");

        activityGlobalSearchBinding.tvClearAll.setOnClickListener(v -> {

            if (savedSearchCategories.size() > 0)
                productViewModel.deleteAllSearches();
        });

        activityGlobalSearchBinding.ivBackArrow.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void setSearchView() {

        activityGlobalSearchBinding.etGlobalSearch.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
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

    private DisposableObserver<SearchDataContainer> getSearchObserver() {
        return new DisposableObserver<SearchDataContainer>() {
            @Override
            public void onNext(SearchDataContainer searchDataContainer) {

                if (searchDataContainer != null) {

                    if (searchDataContainer.getCode().equals(SUCCESS_CODE)) {

                        if (searchDataContainer.getData().getCategory().size() > 0 || searchDataContainer.getData().getProduct().size() > 0) {
                            searchProducts.clear();
                            searchCategories.clear();

                            searchProducts.addAll(searchDataContainer.getData().getProduct());
                            searchCategories.addAll(searchDataContainer.getData().getCategory());

                            createModelsForAdapter();

                            if (searchCategories.size() > 0) {

                                activityGlobalSearchBinding.emptyState.setVisibility(View.GONE);
                                activityGlobalSearchBinding.rvSearchProducts.setVisibility(View.VISIBLE);

                            } else {
                                Toast.makeText(GlobalSearchActivity.this, "Items Not Found", Toast.LENGTH_SHORT).show();
                                searchCategories.clear();
                            }

                            searchProductAdapter.setData(searchCategories);
                            searchProductAdapter.notifyDataSetChanged();
                        } else {

                            searchProducts.clear();
                            searchCategories.clear();
                            searchProductAdapter.notifyDataSetChanged();
                        }
                    } else if (searchDataContainer.getCode().equals(VALIDATION_FAIL_CODE)) {

                        if (savedSearchCategories.size() > 0) {
                            activityGlobalSearchBinding.recentSearches.setVisibility(View.VISIBLE);
                            activityGlobalSearchBinding.rvSearchProducts.setVisibility(View.GONE);
                            searchCategories.clear();
                            saveSearchAdapter.notifyDataSetChanged();
                        } else {
                            activityGlobalSearchBinding.emptyState.setVisibility(View.VISIBLE);
                        }

                    }

                }

                activityGlobalSearchBinding.pbLoading.setVisibility(View.GONE);

            }

            @Override
            public void onError(Throwable e) {
                Log.e(AppConstants.TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        };
    }

    private DisposableObserver<TextViewTextChangeEvent> searchContactsTextWatcher() {
        return new DisposableObserver<TextViewTextChangeEvent>() {
            @Override
            public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
                Log.d(AppConstants.TAG, "Search query: " + textViewTextChangeEvent.text());

                publishSubject.onNext(textViewTextChangeEvent.text().toString());
            }

            @Override
            public void onError(Throwable e) {
                Log.e(AppConstants.TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        };
    }
}
