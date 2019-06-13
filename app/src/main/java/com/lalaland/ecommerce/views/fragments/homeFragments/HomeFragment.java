package com.lalaland.ecommerce.views.fragments.homeFragments;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.adapters.ActionAdapter;
import com.lalaland.ecommerce.adapters.BrandsFocusAdapter;
import com.lalaland.ecommerce.adapters.PickOfWeekAdapter;
import com.lalaland.ecommerce.adapters.ProductAdapter;
import com.lalaland.ecommerce.adapters.ProductPagedListAdapter;
import com.lalaland.ecommerce.data.models.home.Actions;
import com.lalaland.ecommerce.data.models.home.FeaturedBrand;
import com.lalaland.ecommerce.data.models.home.HomeBanner;
import com.lalaland.ecommerce.data.models.home.PicksOfTheWeek;
import com.lalaland.ecommerce.data.models.products.Product;
import com.lalaland.ecommerce.databinding.FragmentHomeBinding;
import com.lalaland.ecommerce.helpers.AppPreference;
import com.lalaland.ecommerce.viewModels.products.HomeViewModel;
import com.lalaland.ecommerce.viewModels.products.ProductViewModelFactory;
import com.lalaland.ecommerce.views.activities.ActionProductListingActivity;
import com.lalaland.ecommerce.views.activities.ProductDetailActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lalaland.ecommerce.helpers.AppConstants.ACTION_ID;
import static com.lalaland.ecommerce.helpers.AppConstants.ACTION_NAME;
import static com.lalaland.ecommerce.helpers.AppConstants.BANNER_STORAGE_BASE_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.BRANDS_IN_FOCUS_PRODUCTS;
import static com.lalaland.ecommerce.helpers.AppConstants.LENGTH;
import static com.lalaland.ecommerce.helpers.AppConstants.PICK_OF_THE_WEEK_PRODUCTS;
import static com.lalaland.ecommerce.helpers.AppConstants.PRODUCT_ID;
import static com.lalaland.ecommerce.helpers.AppConstants.PRODUCT_TYPE;
import static com.lalaland.ecommerce.helpers.AppConstants.RECOMMENDED_CAT_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.START_INDEX;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;

public class HomeFragment extends Fragment implements ActionAdapter.ActionClickListener, PickOfWeekAdapter.WeekProductClickListener,
        BrandsFocusAdapter.FeatureBrandClickListener, ProductPagedListAdapter.ProductListener, ProductAdapter.ProductListener {

    public static final String TAG = HomeFragment.class.getSimpleName();
    private HomeViewModel homeViewModel;

    private FragmentHomeBinding fragmentHomeBinding;
    private List<Actions> actionsList = new ArrayList<>();
    private List<HomeBanner> bannerList = new ArrayList<>();
    private List<PicksOfTheWeek> picksOfTheWeekList = new ArrayList<>();
    private List<FeaturedBrand> featuredBrandList = new ArrayList<>();
    private List<Product> productList = new ArrayList<>();

    private Map<String, String> parameters = new HashMap<>();
    private ProductAdapter recommendationProductAdapter;
    private GridLayoutManager gridLayoutManager;

    Boolean isLoading = false;
    public static int INITIAL_INDEX = 0;
    public static int END_INDEX = 30;
    public static final int NUMBER_OF_ITEM = 30;
    private String recommended_cat;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        homeViewModel = ViewModelProviders.of(this, new ProductViewModelFactory()).get(HomeViewModel.class);

        fragmentHomeBinding.setHomeListener(this);

        recommended_cat = AppPreference.getInstance(getContext()).getString(RECOMMENDED_CAT_TOKEN);
        requestInitialProducts();
        return fragmentHomeBinding.getRoot();
    }


    void requestInitialProducts() {

        homeViewModel.getHomeData().observe(this, homeDataContainer -> {

            if (homeDataContainer != null && homeDataContainer.getCode().equals(SUCCESS_CODE)) {

                bannerList = new ArrayList<>();
                actionsList = new ArrayList<>();
                featuredBrandList = new ArrayList<>();
                picksOfTheWeekList = new ArrayList<>();


                bannerList.addAll(homeDataContainer.getHomeData().getHomeBanners());
                actionsList.addAll(homeDataContainer.getHomeData().getactions());

                //recommendationList.addAll(homeDataContainer.getHomeData().getRecommendation());
                featuredBrandList.addAll(homeDataContainer.getHomeData().getFeaturedBrands());
                picksOfTheWeekList.addAll(homeDataContainer.getHomeData().getPicksOfTheWeek());
                
                setBannerSlider();
                setActions();
                setPickOfTheWeek();
                setFeaturedBrands();

                // setRecommendationProducts();

                fragmentHomeBinding.containersParent.setVisibility(View.VISIBLE);
                fragmentHomeBinding.pbLoading.setVisibility(View.GONE);
            }
        });

        setRecommendationProducts();


    }

    void getProductItems() {

        parameters.put(RECOMMENDED_CAT_TOKEN, recommended_cat);

        homeViewModel.getRecommendations(parameters).observe(this, productContainer -> {

            if (productContainer != null && productContainer.getProductData().getProducts().size() > 0) {

                int startPosition;

                startPosition = productList.size();

                productList.addAll(productContainer.getProductData().getProducts());
                recommendationProductAdapter.notifyItemRangeInserted(startPosition, productList.size());

                Log.d(TAG, "getProductItems" + productList.size());
                isLoading = false;

            }

            fragmentHomeBinding.pbProductLoad.setVisibility(View.GONE);

        });
    }

    private void setBannerSlider() {

        for (int i = 0; i < bannerList.size(); i++) {

            String bannerImageUrl = BANNER_STORAGE_BASE_URL.concat(bannerList.get(i).getBannerImage());

//            AppConstants.testImagesUrl.add(bannerImageUrl); // for testing

            ImageView imageView = new ImageView(getContext());

            Glide
                    .with(getContext())
                    .load(bannerImageUrl)
                    .centerCrop()
                    .into(imageView);

            fragmentHomeBinding.vfSlider.addView(imageView);
            fragmentHomeBinding.vfSlider.setFlipInterval(4000);
            fragmentHomeBinding.vfSlider.setAutoStart(true);

            fragmentHomeBinding.vfSlider.setInAnimation(getContext(), android.R.anim.slide_in_left);
            fragmentHomeBinding.vfSlider.setOutAnimation(getContext(), android.R.anim.slide_out_right);
        }

        fragmentHomeBinding.vfSlider.startFlipping();
    }

    private void setActions() {
        ActionAdapter actionAdapter = new ActionAdapter(getContext(), this);
        fragmentHomeBinding.rvActions.setAdapter(actionAdapter);
        fragmentHomeBinding.rvActions.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        actionAdapter.setData(actionsList);
    }

    @Override
    public void onActionClicked(Actions actions) {

        Intent intent = new Intent(getContext(), ActionProductListingActivity.class);

        intent.putExtra(ACTION_NAME, actions.getName());
        intent.putExtra(ACTION_ID, String.valueOf(actions.getActionId()));
        intent.putExtra(PRODUCT_TYPE, actions.getActionName());

        startActivity(intent);
    }

    private void setPickOfTheWeek() {

        PickOfWeekAdapter pickOfWeekAdapter = new PickOfWeekAdapter(getContext(), this);
        fragmentHomeBinding.rvPicksOfWeek.setAdapter(pickOfWeekAdapter);
        fragmentHomeBinding.rvPicksOfWeek.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        pickOfWeekAdapter.setData(picksOfTheWeekList);
    }

    public void showAllProductsWeekProducts(View view) {

        Intent intent = new Intent(getContext(), ActionProductListingActivity.class);
        intent.putExtra(ACTION_NAME, getResources().getString(R.string.picks_of_the_week));
        intent.putExtra(PRODUCT_TYPE, PICK_OF_THE_WEEK_PRODUCTS);
        startActivity(intent);
    }

    @Override
    public void onWeekProductClicked(PicksOfTheWeek picksOfTheWeek) {

        Intent intent = new Intent(getContext(), ProductDetailActivity.class);
        intent.putExtra(PRODUCT_ID, picksOfTheWeek.getId());
        startActivity(intent);
    }

    private void setFeaturedBrands() {

        BrandsFocusAdapter brandsFocusAdapter = new BrandsFocusAdapter(getContext(), this);
        fragmentHomeBinding.rvBrandsInFocus.setAdapter(brandsFocusAdapter);
        fragmentHomeBinding.rvBrandsInFocus.setLayoutManager(new GridLayoutManager(getContext(), 2));
        brandsFocusAdapter.setData(featuredBrandList);
    }

    @Override
    public void onBrandClicked(FeaturedBrand featuredBrand) {

        Intent intent = new Intent(getContext(), ActionProductListingActivity.class);

        intent.putExtra(ACTION_NAME, featuredBrand.getName());
        intent.putExtra(ACTION_ID, String.valueOf(featuredBrand.getId()));
        intent.putExtra(PRODUCT_TYPE, BRANDS_IN_FOCUS_PRODUCTS);
        startActivity(intent);
    }

    void setRecommendationProducts() {

        recommendationProductAdapter = new ProductAdapter(getContext(), this);
        recommendationProductAdapter.setData(productList);
        fragmentHomeBinding.rvRecommendedProducts.setAdapter(recommendationProductAdapter);
        gridLayoutManager = new GridLayoutManager(getContext(), 3);
        fragmentHomeBinding.rvRecommendedProducts.setLayoutManager(gridLayoutManager);

        //  ViewCompat.setNestedScrollingEnabled(fragmentHomeBinding.rvRecommendedProducts, false);

        // It takes almost 3 4 days jut because, recyclerview is under nestedScrollView (onScrolled does not call)
        fragmentHomeBinding.containersParent.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (v.getChildAt(v.getChildCount() - 1) != null) {
                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                        scrollY > oldScrollY) {
                    //code to fetch more data for endless scrolling

                    if (!isLoading) {

                        fragmentHomeBinding.pbProductLoad.setVisibility(View.VISIBLE);

                        isLoading = true;

                        parameters.clear();
                        INITIAL_INDEX = END_INDEX;
                        INITIAL_INDEX++; // starting from end+1
                        END_INDEX += NUMBER_OF_ITEM;

                        parameters.put(START_INDEX, String.valueOf(INITIAL_INDEX));
                        parameters.put(LENGTH, String.valueOf(NUMBER_OF_ITEM));

                        getProductItems();
                    }

                }
            }
        });


        parameters.put(START_INDEX, String.valueOf(INITIAL_INDEX));
        parameters.put(LENGTH, String.valueOf(END_INDEX)); // multiple of 3 due to 3 products are listing in a row
        getProductItems();

    }

    //This method would check that the recyclerview scroll has reached the bottom or not
    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                return true;
        }
        return false;
    }

    @Override
    public void onProductProductClicked(Product product) {

        Intent intent = new Intent(getContext(), ProductDetailActivity.class);
        intent.putExtra(PRODUCT_ID, product.getId());
        startActivity(intent);
    }
}
