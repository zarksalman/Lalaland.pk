package com.lalaland.ecommerce.views.fragments.homeFragments;


import android.os.Bundle;
import android.util.Log;
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
import com.lalaland.ecommerce.adapters.PickOfWeekAdapter;
import com.lalaland.ecommerce.adapters.ProductAdapter;
import com.lalaland.ecommerce.data.models.home.Actions;
import com.lalaland.ecommerce.data.models.home.FeaturedBrand;
import com.lalaland.ecommerce.data.models.home.HomeBanner;
import com.lalaland.ecommerce.data.models.home.PicksOfTheWeek;
import com.lalaland.ecommerce.data.models.home.Recommendation;
import com.lalaland.ecommerce.data.models.products.Product;
import com.lalaland.ecommerce.databinding.FragmentHomeBinding;
import com.lalaland.ecommerce.viewModels.productsViewModels.HomeViewModel;
import com.lalaland.ecommerce.viewModels.productsViewModels.ProductViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.lalaland.ecommerce.helpers.AppConstants.BANNER_STORAGE_BASE_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.PRODUCT_STORAGE_BASE_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.PRODUCT_STORAGE_BASE_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;

public class HomeFragment extends Fragment implements ActionAdapter.ActionClickListener, PickOfWeekAdapter.WeekProductClickListener {

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

        fragmentHomeBinding.setPicksofWeek(this);
        requestInitialProducts();
        return fragmentHomeBinding.getRoot();
    }


    void requestInitialProducts() {

        homeViewModel.getHomeData().observe(this, homeDataContainer -> {

            if (homeDataContainer != null && homeDataContainer.getCode().equals(SUCCESS_CODE)) {

                bannerList.addAll(homeDataContainer.getHomeData().getHomeBanners());
                actionsList.addAll(homeDataContainer.getHomeData().getactions());
                recommendationList.addAll(homeDataContainer.getHomeData().getRecommendation());
                featuredBrandList.addAll(homeDataContainer.getHomeData().getFeaturedBrands());
                picksOfTheWeekList.addAll(homeDataContainer.getHomeData().getPicksOfTheWeek());

                Log.d(TAG, "data" + bannerList.size());
                Log.d(TAG, "data" + actionsList.size());
                Log.d(TAG, "data" + recommendationList.size());
                Log.d(TAG, "data" + featuredBrandList.size());
                Log.d(TAG, "data" + picksOfTheWeekList.size());

                setBannerSlider();
                setActions();
                setPickOfTheWeek();

                fragmentHomeBinding.pbLoading.setVisibility(View.GONE);
            }
        });
    }

    private void setBannerSlider() {

        for (int i = 0; i < bannerList.size(); i++) {

            String bannerImageUrl = BANNER_STORAGE_BASE_URL.concat(bannerList.get(i).getBannerImage());

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
        Log.d(TAG, "onActionClicked: " + actions.getName() + actions.getActionId());
    }

    void setPickOfTheWeek() {

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
}
