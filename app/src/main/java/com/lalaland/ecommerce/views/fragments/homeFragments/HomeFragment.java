package com.lalaland.ecommerce.views.fragments.homeFragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.adapters.ActionAdapter;
import com.lalaland.ecommerce.adapters.BrandsFocusAdapter;
import com.lalaland.ecommerce.adapters.PickOfWeekAdapter;
import com.lalaland.ecommerce.adapters.ProductAdapter;
import com.lalaland.ecommerce.adapters.RecommendationAdapter;
import com.lalaland.ecommerce.data.models.home.Actions;
import com.lalaland.ecommerce.data.models.home.FeaturedBrand;
import com.lalaland.ecommerce.data.models.home.HomeBanner;
import com.lalaland.ecommerce.data.models.home.PicksOfTheWeek;
import com.lalaland.ecommerce.data.models.home.Recommendation;
import com.lalaland.ecommerce.databinding.FragmentHomeBinding;
import com.lalaland.ecommerce.helpers.AppConstants;
import com.lalaland.ecommerce.viewModels.products.HomeViewModel;
import com.lalaland.ecommerce.viewModels.products.ProductViewModel;
import com.lalaland.ecommerce.views.activities.ProductListingActivity;

import java.util.ArrayList;
import java.util.List;

import static com.lalaland.ecommerce.helpers.AppConstants.ACTION_ID;
import static com.lalaland.ecommerce.helpers.AppConstants.ACTION_NAME;
import static com.lalaland.ecommerce.helpers.AppConstants.BANNER_STORAGE_BASE_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;

public class HomeFragment extends Fragment implements ActionAdapter.ActionClickListener, PickOfWeekAdapter.WeekProductClickListener,
        BrandsFocusAdapter.FeatureBrandClickListener, RecommendationAdapter.RecommendationProductListener {

    public static final String TAG = HomeFragment.class.getSimpleName();
    private ProductViewModel productViewModel;
    private HomeViewModel homeViewModel;

    private FragmentHomeBinding fragmentHomeBinding;
    private ProductAdapter productAdapter;

    private List<Actions> actionsList = new ArrayList<>();
    private List<Recommendation> recommendationList = new ArrayList<>();
    private List<HomeBanner> bannerList = new ArrayList<>();
    private List<PicksOfTheWeek> picksOfTheWeekList = new ArrayList<>();
    private List<FeaturedBrand> featuredBrandList = new ArrayList<>();


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
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        fragmentHomeBinding.setHomeListener(this);
        requestInitialProducts();
        return fragmentHomeBinding.getRoot();
    }


    void requestInitialProducts() {

        homeViewModel.getHomeData().observe(this, homeDataContainer -> {

            if (homeDataContainer != null && homeDataContainer.getCode().equals(SUCCESS_CODE)) {

                bannerList = new ArrayList<>();
                actionsList = new ArrayList<>();
                recommendationList = new ArrayList<>();
                featuredBrandList = new ArrayList<>();
                picksOfTheWeekList = new ArrayList<>();

                bannerList.addAll(homeDataContainer.getHomeData().getHomeBanners());
                actionsList.addAll(homeDataContainer.getHomeData().getactions());
                recommendationList.addAll(homeDataContainer.getHomeData().getRecommendation());
                featuredBrandList.addAll(homeDataContainer.getHomeData().getFeaturedBrands());
                picksOfTheWeekList.addAll(homeDataContainer.getHomeData().getPicksOfTheWeek());

                setBannerSlider();
                setActions();
                setPickOfTheWeek();
                setFeaturedBrands();
                setRecommendationProducts();

                fragmentHomeBinding.containersParent.setVisibility(View.VISIBLE);
                fragmentHomeBinding.pbLoading.setVisibility(View.GONE);
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

        Intent intent = new Intent(getContext(), ProductListingActivity.class);

        intent.putExtra(ACTION_NAME, actions.getActionName());
        intent.putExtra(ACTION_ID, String.valueOf(actions.getActionId()));
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
        Toast.makeText(getContext(), picksOfTheWeek.getName(), Toast.LENGTH_SHORT).show();
    }

    private void setFeaturedBrands() {

        BrandsFocusAdapter brandsFocusAdapter = new BrandsFocusAdapter(getContext(), this);
        fragmentHomeBinding.rvBrandsInFocus.setAdapter(brandsFocusAdapter);
        fragmentHomeBinding.rvBrandsInFocus.setLayoutManager(new GridLayoutManager(getContext(), 2));
        brandsFocusAdapter.setData(featuredBrandList);
    }

    @Override
    public void onBrandClicked(FeaturedBrand featuredBrand) {
        Toast.makeText(getContext(), featuredBrand.getName(), Toast.LENGTH_SHORT).show();
    }

    void setRecommendationProducts() {
        RecommendationAdapter recommendationAdapter = new RecommendationAdapter(getContext(), this);
        fragmentHomeBinding.rvRecommendedProducts.setAdapter(recommendationAdapter);
        fragmentHomeBinding.rvRecommendedProducts.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recommendationAdapter.setData(recommendationList);
    }

    @Override
    public void onRecommendationProductClicked(Recommendation recommendation) {
        Toast.makeText(getContext(), recommendation.getBrandName(), Toast.LENGTH_SHORT).show();
    }
}
