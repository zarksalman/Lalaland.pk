package com.lalaland.ecommerce.views.fragments.homeFragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.lalaland.ecommerce.data.models.home.Actions;
import com.lalaland.ecommerce.data.models.home.FeaturedBrand;
import com.lalaland.ecommerce.data.models.home.HomeBanner;
import com.lalaland.ecommerce.data.models.home.PicksOfTheWeek;
import com.lalaland.ecommerce.data.models.products.Product;
import com.lalaland.ecommerce.databinding.FragmentHomeBinding;
import com.lalaland.ecommerce.helpers.AppConstants;
import com.lalaland.ecommerce.helpers.AppPreference;
import com.lalaland.ecommerce.viewModels.products.HomeViewModel;
import com.lalaland.ecommerce.views.activities.ActionProductListingActivity;
import com.lalaland.ecommerce.views.activities.ProductDetailActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lalaland.ecommerce.helpers.AppConstants.ACTION_ID;
import static com.lalaland.ecommerce.helpers.AppConstants.ACTION_NAME;
import static com.lalaland.ecommerce.helpers.AppConstants.BANNER_STORAGE_BASE_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.LENGTH;
import static com.lalaland.ecommerce.helpers.AppConstants.PRODUCT_ID;
import static com.lalaland.ecommerce.helpers.AppConstants.PRODUCT_TYPE;
import static com.lalaland.ecommerce.helpers.AppConstants.RECOMMENDED_CAT_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.START_INDEX;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;

public class HomeFragment extends Fragment implements ActionAdapter.ActionClickListener, PickOfWeekAdapter.WeekProductClickListener,
        BrandsFocusAdapter.FeatureBrandClickListener, ProductAdapter.ProductListener {

    public static final String TAG = HomeFragment.class.getSimpleName();
    private HomeViewModel homeViewModel;

    private FragmentHomeBinding fragmentHomeBinding;
    private List<Actions> actionsList = new ArrayList<>();
    private List<HomeBanner> bannerList = new ArrayList<>();
    private List<PicksOfTheWeek> picksOfTheWeekList = new ArrayList<>();
    private List<FeaturedBrand> featuredBrandList = new ArrayList<>();
    private List<Product> productList = new ArrayList<>();

    private Map<String, String> parameters = new HashMap<>();
    private boolean isScrolling = false;
    private int currentItem, totalItems, scrolledItems;
    private ProductAdapter recommendationProductAdapter;

    public static int initialIndex = 0;
    public static int numberOfItems = 30;
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
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

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

        parameters.put(START_INDEX, "0");
        parameters.put(LENGTH, "30"); // multiple of 3 due to 3 products are listing in a row

        getProductItems();
    }

    void getProductItems() {

        parameters.put(RECOMMENDED_CAT_TOKEN, recommended_cat);

        homeViewModel.getRecommendations(parameters).observe(this, productContainer -> {

            if (productContainer != null) {

                productList.addAll(productContainer.getProductData().getProducts());
                setRecommendationProducts();
            }
        });
    }

    private void setBannerSlider() {

        for (int i = 0; i < bannerList.size(); i++) {

            String bannerImageUrl = BANNER_STORAGE_BASE_URL.concat(bannerList.get(i).getBannerImage());

            AppConstants.testImagesUrl.add(bannerImageUrl); // for testing

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

        intent.putExtra(ACTION_NAME, actions.getActionName());
        intent.putExtra(ACTION_ID, String.valueOf(actions.getActionId()));
        intent.putExtra(PRODUCT_TYPE, "action_products");

        startActivity(intent);
        //  Log.d(TAG, "onActionClicked: " + actions.getName() + actions.getActionId());
    }

    private void setPickOfTheWeek() {

        PickOfWeekAdapter pickOfWeekAdapter = new PickOfWeekAdapter(getContext(), this);
        fragmentHomeBinding.rvPicksOfWeek.setAdapter(pickOfWeekAdapter);
        fragmentHomeBinding.rvPicksOfWeek.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        pickOfWeekAdapter.setData(picksOfTheWeekList);
    }

    public void showAllProductsWeekProducts(View view) {
        Toast.makeText(getContext(), TAG, Toast.LENGTH_SHORT).show();
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

/*
        Intent intent = new Intent(getContext(), ActionProductListingActivity.class);
        intent.putExtra(PRODUCT_TYPE, "brands_products");
*/

        Toast.makeText(getContext(), featuredBrand.getName().concat(":").concat(String.valueOf(featuredBrand.getId())), Toast.LENGTH_SHORT).show();
    }

    void setRecommendationProducts() {

        //RecommendationAdapter recommendationAdapter = new RecommendationAdapter(getContext(), this);
        recommendationProductAdapter = new ProductAdapter(getContext(), this);

        fragmentHomeBinding.rvRecommendedProducts.setAdapter(recommendationProductAdapter);
        GridLayoutManager gridLayoutManager;
        gridLayoutManager = new GridLayoutManager(getContext(), 3);
        fragmentHomeBinding.rvRecommendedProducts.setLayoutManager(gridLayoutManager);
        recommendationProductAdapter.setData(productList);

        fragmentHomeBinding.rvRecommendedProducts.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                isScrolling = false;
                currentItem = gridLayoutManager.getChildCount();
                totalItems = gridLayoutManager.getItemCount();
                scrolledItems = gridLayoutManager.findFirstCompletelyVisibleItemPosition();

                if (isScrolling && (currentItem + scrolledItems == totalItems)) {

                    parameters.clear();
                    initialIndex = numberOfItems;
                    numberOfItems += initialIndex;

                    parameters.put(START_INDEX, String.valueOf(initialIndex));
                    parameters.put(LENGTH, String.valueOf(numberOfItems));

                    getProductItems();
                }
            }
        });
    }

    /*
    @Override
    public void onRecommendationProductClicked(Recommendation recommendation) {

        Intent intent = new Intent(getContext(), ProductDetailActivity.class);
        intent.putExtra(PRODUCT_ID, recommendation.getId());
        startActivity(intent);
    }*/

    @Override
    public void onProductProductClicked(Product product) {

        Intent intent = new Intent(getContext(), ProductDetailActivity.class);
        intent.putExtra(PRODUCT_ID, product.getId());
        startActivity(intent);
    }
}
