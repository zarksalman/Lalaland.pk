package com.lalaland.ecommerce.views.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.lalaland.ecommerce.helpers.AnalyticsManager;
import com.lalaland.ecommerce.helpers.AppConstants;
import com.lalaland.ecommerce.helpers.AppPreference;
import com.lalaland.ecommerce.viewModels.products.ProductViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import static com.lalaland.ecommerce.helpers.AppConstants.CART_SESSION_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.PRODUCT_ID;
import static com.lalaland.ecommerce.helpers.AppConstants.PRODUCT_TYPE;
import static com.lalaland.ecommerce.helpers.AppConstants.SIGNIN_TOKEN;
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
    Map<String, String> userInfo = new HashMap<>();
    Drawable drawableRight;
    boolean isSearchViewHasIcon = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityGlobalSearchBinding = DataBindingUtil.setContentView(this, R.layout.activity_global_search);

        appPreference = AppPreference.getInstance(this);

        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);

        setUserInfo();
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

        DisposableObserver<SearchDataContainer> observer = getSearchObserver();

        disposable.add(
                publishSubject
                        .debounce(300, TimeUnit.MILLISECONDS)
                        .distinctUntilChanged()
                        .switchMapSingle((Function<String, Single<SearchDataContainer>>) s -> RetrofitRxJavaClient.getInstance().createClient().globalRxSearch(userInfo, s)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread()))
                        .subscribeWith(observer));


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

        activityGlobalSearchBinding.etGlobalSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().isEmpty()) {
                    activityGlobalSearchBinding.etGlobalSearch.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    activityGlobalSearchBinding.ivCross.setVisibility(View.GONE);
                    isSearchViewHasIcon = false;
                } else {

                    activityGlobalSearchBinding.ivCross.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        activityGlobalSearchBinding.ivCross.setOnClickListener(v -> {
            activityGlobalSearchBinding.etGlobalSearch.setText("");
        });

        activityGlobalSearchBinding.ivSearch.setOnClickListener(v -> {
            callQstr();
        });

        activityGlobalSearchBinding.etGlobalSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    callQstr();
                    return true;
                }
                return false;
            }
        });
    }

    private void callQstr() {

        if (!activityGlobalSearchBinding.etGlobalSearch.getText().toString().isEmpty()) {

            String qstr = activityGlobalSearchBinding.etGlobalSearch.getText().toString();

            intent = new Intent(this, ActionProductListingActivity.class);

            intent.putExtra("qstr", qstr);
            intent.putExtra(ACTION_ID, String.valueOf(0));
            intent.putExtra(ACTION_NAME, "Search");
            intent.putExtra(PRODUCT_TYPE, "search_list");
            startActivity(intent);
        }
    }

    private void setUserInfo() {

        String token, cartSession;

        token = appPreference.getString(SIGNIN_TOKEN);
        cartSession = appPreference.getString(CART_SESSION_TOKEN);

        if (token.equals(SIGNIN_TOKEN))
            token = "";

        if (cartSession.equals(CART_SESSION_TOKEN))
            cartSession = "";

        userInfo.put("device-id", AppConstants.DEVICE_ID);
        userInfo.put("app-version", AppConstants.APP_BUILD_VERSION);
        userInfo.put("user-id", AppConstants.USER_ID);
        userInfo.put("device-name", AppConstants.DEVICE_NAME);
        userInfo.put("device-model", AppConstants.DEVICE_MODEL);
        userInfo.put("device-OS-version", AppConstants.DEVICE_OS);
        userInfo.put("fcm-token", AppConstants.FCM_TOKEN);
        userInfo.put("device-type", AppConstants.DEVICE_TYPE);


        userInfo.put(SIGNIN_TOKEN, token);
        userInfo.put(CART_SESSION_TOKEN, cartSession);

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

    private void createModelsForAdapter() {

        List<SearchCategory> categoryList = new ArrayList<>();
        SearchCategory searchCategory;

        for (SearchProduct searchProduct : searchProducts) {

            searchCategory = new SearchCategory();

            searchCategory.setId(searchProduct.getId());
            searchCategory.setUrlName(searchProduct.getName());
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
                            activityGlobalSearchBinding.rvSearchProducts.setVisibility(View.GONE);
                            searchCategories.clear();
                            saveSearchAdapter.notifyDataSetChanged();
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

                Bundle bundle = new Bundle();
                bundle.putString("searchString", textViewTextChangeEvent.toString());
                AnalyticsManager.getInstance().sendAnalytics("search", bundle);
                AnalyticsManager.getInstance().sendFacebookAnalytics("Search", bundle);

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
