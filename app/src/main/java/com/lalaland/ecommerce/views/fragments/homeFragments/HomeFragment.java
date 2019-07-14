package com.lalaland.ecommerce.views.fragments.homeFragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

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
import com.lalaland.ecommerce.databinding.ActionLayoutBinding;
import com.lalaland.ecommerce.databinding.FragmentHomeBinding;
import com.lalaland.ecommerce.databinding.PickOfWeekItemBinding;
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
    private static final int TAG_IV = 1;

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
    public static final int NUMBER_OF_ITEM = 20;
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

        android.view.Display display = ((android.view.WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        fragmentHomeBinding.containersParent.getLayoutParams().height = ((int) (display.getHeight() * 0.9));

        requestInitialProducts();

        fragmentHomeBinding.containersParent.fullScroll(ScrollView.FOCUS_UP);
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
                setRecommendationProducts();

                // setRecommendationProducts();

                new Handler().postDelayed(() -> {

                    fragmentHomeBinding.containersParent.setVisibility(View.VISIBLE);
                    fragmentHomeBinding.pbLoading.setVisibility(View.GONE);

                }, 2000);


            }
        });



    }

    void getProductItems() {

        parameters.put(RECOMMENDED_CAT_TOKEN, recommended_cat);

        homeViewModel.getRecommendations(parameters).observe(this, productContainer -> {

            if (productContainer != null && productContainer.getProductData().getProducts().size() > 0) {

                int startPosition;

                startPosition = productList.size();

                productList.addAll(productContainer.getProductData().getProducts());
                recommendationProductAdapter.notifyDataSetChanged();
                // recommendationProductAdapter.notifyItemRangeInserted(startPosition, productList.size());

                Log.d(TAG, "getProductItems" + productList.size());
                isLoading = false;
            }

            new Handler().postDelayed(() -> {
                fragmentHomeBinding.pbProductLoad.setVisibility(View.GONE);
            }, 0);

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


        //   fragmentHomeBinding.rvActionContainer.removeAllViews();

        Integer weight = (100 / actionsList.size());
//        Integer weight = 1;

        for (Integer i = 0; i < actionsList.size(); i++) {

            ActionLayoutBinding layout = DataBindingUtil.inflate(getLayoutInflater(), R.layout.action_layout, null, false);
            layout.ivAction.setTag(R.string.action_tag, i);

            if (layout.actionParent.getParent() != null)
                layout.actionParent.removeView(layout.actionParent);

            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    weight
            );

            //  layout.actionParent.setLayoutParams(param);

            layout.setAction(actionsList.get(i));

            layout.actionParent.setClickable(true);
            layout.ivAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(getContext(), v.getTag(R.string.tag).toString(), Toast.LENGTH_SHORT).show();
                    onActionClicked(Integer.parseInt(v.getTag(R.string.action_tag).toString()));
                }
            });

            fragmentHomeBinding.rvActionContainer.addView(layout.getRoot(), param);


        }
        /*
         ActionAdapter actionAdapter = new ActionAdapter(getContext(), this);
        fragmentHomeBinding.rvActions.setAdapter(actionAdapter);
        fragmentHomeBinding.rvActions.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        actionAdapter.setData(actionsList);*/
    }

    @Override
    public void onActionClicked(Actions actions) {

        Intent intent = new Intent(getContext(), ActionProductListingActivity.class);

        intent.putExtra(ACTION_NAME, actions.getName());
        intent.putExtra(ACTION_ID, String.valueOf(actions.getActionId()));
        intent.putExtra(PRODUCT_TYPE, actions.getActionName());

        startActivity(intent);
    }

    private void onActionClicked(Integer index) {

        Actions actions = actionsList.get(index);
        Intent intent = new Intent(getContext(), ActionProductListingActivity.class);

        intent.putExtra(ACTION_NAME, actions.getName());
        intent.putExtra(ACTION_ID, String.valueOf(actions.getActionId()));
        intent.putExtra(PRODUCT_TYPE, actions.getActionName());

        startActivity(intent);
    }

    private void setPickOfTheWeek() {

        Integer weight = (100 / picksOfTheWeekList.size());

        for (int i = 0; i < picksOfTheWeekList.size(); i++) {

            PickOfWeekItemBinding layout = DataBindingUtil.inflate(getLayoutInflater(), R.layout.pick_of_week_item, null, false);
            layout.ivAction.setTag(R.string.pick_of_week_tag, i);

            if (layout.actionParent.getParent() != null)
                layout.actionParent.removeView(layout.actionParent);

            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    weight
            );

            //  layout.actionParent.setLayoutParams(param);

            layout.setPicks(picksOfTheWeekList.get(i));

            layout.ivAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onWeekProductClicked(Integer.parseInt(v.getTag(R.string.pick_of_week_tag).toString()));
                }
            });

            fragmentHomeBinding.rvPicksOfWeekContainer.addView(layout.getRoot(), param);


        }

/*        PickOfWeekAdapter pickOfWeekAdapter = new PickOfWeekAdapter(getContext(), this);
        fragmentHomeBinding.rvPicksOfWeek.setAdapter(pickOfWeekAdapter);
        fragmentHomeBinding.rvPicksOfWeek.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        pickOfWeekAdapter.setData(picksOfTheWeekList);*/
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

    public void onWeekProductClicked(Integer index) {

        PicksOfTheWeek picksOfTheWeek = picksOfTheWeekList.get(index);
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
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        fragmentHomeBinding.rvRecommendedProducts.setLayoutManager(gridLayoutManager);
        // just to remove lag from recommendation products adapter
        ViewCompat.setNestedScrollingEnabled(fragmentHomeBinding.rvRecommendedProducts, false);

        // It takes almost 3 4 days just because, recyclerview is under nestedScrollView (onScrolled does not call)
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


    @Override
    public void onProductProductClicked(Product product) {

        Intent intent = new Intent(getContext(), ProductDetailActivity.class);
        intent.putExtra(PRODUCT_ID, product.getId());
        startActivity(intent);
    }


}
