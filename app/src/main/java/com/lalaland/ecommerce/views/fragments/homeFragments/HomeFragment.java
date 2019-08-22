package com.lalaland.ecommerce.views.fragments.homeFragments;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.adapters.ActionAdapter;
import com.lalaland.ecommerce.adapters.BannerImageAdapter;
import com.lalaland.ecommerce.adapters.BlogPostsAdapter;
import com.lalaland.ecommerce.adapters.BrandsFocusAdapter;
import com.lalaland.ecommerce.adapters.FeatureCategoryAdapter;
import com.lalaland.ecommerce.adapters.GetTheLooksAdapter;
import com.lalaland.ecommerce.adapters.PickOfWeekAdapter;
import com.lalaland.ecommerce.adapters.ProductAdapter;
import com.lalaland.ecommerce.adapters.ProductPagedListAdapter;
import com.lalaland.ecommerce.data.models.home.Actions;
import com.lalaland.ecommerce.data.models.home.Advertisement;
import com.lalaland.ecommerce.data.models.home.BlogPost;
import com.lalaland.ecommerce.data.models.home.CustomProductsFive;
import com.lalaland.ecommerce.data.models.home.FeaturedBrand;
import com.lalaland.ecommerce.data.models.home.FeaturedCategory;
import com.lalaland.ecommerce.data.models.home.HomeBanner;
import com.lalaland.ecommerce.data.models.home.PicksOfTheWeek;
import com.lalaland.ecommerce.data.models.products.Product;
import com.lalaland.ecommerce.databinding.ActionLayoutBinding;
import com.lalaland.ecommerce.databinding.FragmentHomeNewBinding;
import com.lalaland.ecommerce.databinding.PickOfWeekItemBinding;
import com.lalaland.ecommerce.helpers.AppConstants;
import com.lalaland.ecommerce.helpers.AppPreference;
import com.lalaland.ecommerce.viewModels.products.HomeViewModel;
import com.lalaland.ecommerce.viewModels.products.ProductViewModelFactory;
import com.lalaland.ecommerce.views.activities.ActionProductListingActivity;
import com.lalaland.ecommerce.views.activities.ProductDetailActivity;
import com.lalaland.ecommerce.views.activities.WebViewActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.lalaland.ecommerce.helpers.AppConstants.ACTION_ID;
import static com.lalaland.ecommerce.helpers.AppConstants.ACTION_NAME;
import static com.lalaland.ecommerce.helpers.AppConstants.BLOG_URL;
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

    private FragmentHomeNewBinding fragmentHomeNewBinding;
    private List<Actions> actionsList = new ArrayList<>();
    private List<HomeBanner> bannerList = new ArrayList<>();
    private List<PicksOfTheWeek> picksOfTheWeekList = new ArrayList<>();
    private List<FeaturedBrand> featuredBrandList = new ArrayList<>();
    private List<CustomProductsFive> customProductsFives = new ArrayList<>();
    private List<FeaturedCategory> featuredCategories = new ArrayList<>();
    private List<BlogPost> blogPosts = new ArrayList<>();
    private Advertisement advertisement;
    private List<Product> productList = new ArrayList<>();

    private Map<String, String> parameters = new HashMap<>();
    private Map<String, String> homeParameter = new HashMap<>();
    private ProductAdapter recommendationProductAdapter;
    private GetTheLooksAdapter getTheLooksAdapter;
    private GridLayoutManager gridLayoutManager;

    Boolean isLoading = false;
    public static int INITIAL_INDEX = 0;
    public static int END_INDEX = 30;
    public static final int NUMBER_OF_ITEM = 30;
    private String recommended_cat;
    private List<ImageView> dots = new ArrayList<>();
    int currentPage = 0;

    //******************************* new home page *******************************
    private ProgressBar progressBar;

    private boolean isScrolling = false;
    int firstVisibleInListview;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentHomeNewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_new, container, false);
        homeViewModel = ViewModelProviders.of(this, new ProductViewModelFactory()).get(HomeViewModel.class);

        fragmentHomeNewBinding.setHomeListener(this);
        fragmentHomeNewBinding.pickOfWeekContainerParent.setHomeListener(this);

        recommended_cat = AppPreference.getInstance(getContext()).getString(RECOMMENDED_CAT_TOKEN);

        iniUi();
/*
        android.view.Display display = ((android.view.WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        fragmentHomeBinding.containersParent.getLayoutParams().height = ((int) (display.getHeight() * 0.9));
*/

        requestInitialProducts();

        fragmentHomeNewBinding.btnScrollUp.setOnClickListener(v -> {
            fragmentHomeNewBinding.containersParent.fullScroll(ScrollView.FOCUS_UP);
        });


//        fragmentHomeBinding.containersParent.fullScroll(ScrollView.FOCUS_UP);
        return fragmentHomeNewBinding.getRoot();
    }

    private void iniUi() {

        progressBar = new ProgressBar(getContext());

    }

    void requestInitialProducts() {

        homeViewModel.getHomeData().observe(this, homeDataContainer -> {

            if (homeDataContainer != null && homeDataContainer.getCode().equals(SUCCESS_CODE)) {

                bannerList = new ArrayList<>();
                actionsList = new ArrayList<>();
                featuredBrandList = new ArrayList<>();
                picksOfTheWeekList = new ArrayList<>();
                customProductsFives = new ArrayList<>();
                featuredCategories = new ArrayList<>();
                blogPosts = new ArrayList<>();

                bannerList.addAll(homeDataContainer.getHomeData().getHomeBanners());
                actionsList.addAll(homeDataContainer.getHomeData().getActions());
                picksOfTheWeekList.addAll(homeDataContainer.getHomeData().getPicksOfTheWeek());
                customProductsFives.addAll(homeDataContainer.getHomeData().getCustomProductsFive());
                featuredBrandList.addAll(homeDataContainer.getHomeData().getFeaturedBrands());
                featuredCategories.addAll(homeDataContainer.getHomeData().getFeaturedCategories());
                blogPosts.addAll(homeDataContainer.getHomeData().getBlogPosts());
                advertisement = homeDataContainer.getHomeData().getAdvertisement();

                setBannerSlider();
                setActions();
                setGetTheLook();
                setFeatureCategory();
                setBlogPost();
                setAdvertisement();

                setRecommendationProducts();
                setPickOfTheWeek();
                setFeaturedBrands();

                setupAutoPager();

                fragmentHomeNewBinding.swipeContainer.setOnRefreshListener(() -> {
                    productList.clear();
                    recommendationProductAdapter.notifyDataSetChanged();
                    requestInitialProducts();
                });
            }



            fragmentHomeNewBinding.swipeContainer.setRefreshing(false);
        });

    }

    private void setAdvertisement() {

        String url = AppConstants.ADVERTISEMENT_URL + advertisement.getImage();
        Glide
                .with(getContext())
                .load(url)
                .placeholder(R.drawable.placeholder_products)
                .error(R.drawable.placeholder_products)
                .into(fragmentHomeNewBinding.advertisementContainerParent.ivAdvertisement);

        fragmentHomeNewBinding.advertisementContainerParent.ivAdvertisement.setOnClickListener(v -> {

            Intent intent = new Intent(getContext(), ActionProductListingActivity.class);
            intent.putExtra(ACTION_NAME, advertisement.getName());
            intent.putExtra(ACTION_ID, String.valueOf(advertisement.getActionId()));
            intent.putExtra(PRODUCT_TYPE, advertisement.getActionName());
            startActivity(intent);

        });

        // fragmentHomeNewBinding.advertisementContainerParent.setAdvertisement(advertisement);
    }

    private void setBlogPost() {

        BlogPostsAdapter blogPostsAdapter = new BlogPostsAdapter(getContext(), blogPost -> {

            AppConstants.URL_TYPE = 0;
            String url = AppConstants.BLOG_URLS + blogPost.getId() + "/" + AppConstants.APP_NAME;
            Intent intent = new Intent(getContext(), WebViewActivity.class);
            intent.putExtra(BLOG_URL, url);
            startActivity(intent);
        });

        fragmentHomeNewBinding.blogsContainerParent.rvBlogs.setAdapter(blogPostsAdapter);
        fragmentHomeNewBinding.blogsContainerParent.rvBlogs.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        fragmentHomeNewBinding.blogsContainerParent.rvBlogs.setHasFixedSize(true);
        blogPostsAdapter.setData(blogPosts);
    }

    private void setGetTheLook() {

        getTheLooksAdapter = new GetTheLooksAdapter(getContext(), customProductsFive -> {

            Intent intent = new Intent(getContext(), ActionProductListingActivity.class);

            intent.putExtra(ACTION_NAME, customProductsFive.getName());
            intent.putExtra(ACTION_ID, String.valueOf(customProductsFive.getActionId()));
            intent.putExtra(PRODUCT_TYPE, customProductsFive.getActionName());
            startActivity(intent);
        });

        fragmentHomeNewBinding.getTheLooksContainerParent.rvGetTheLooks.setAdapter(getTheLooksAdapter);
        fragmentHomeNewBinding.getTheLooksContainerParent.rvGetTheLooks.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        fragmentHomeNewBinding.getTheLooksContainerParent.rvGetTheLooks.setHasFixedSize(true);
        getTheLooksAdapter.setData(customProductsFives);

    }

    void getProductItems() {

        parameters.put(RECOMMENDED_CAT_TOKEN, recommended_cat);

        homeViewModel.getRecommendations(parameters).observe(this, productContainer -> {

            if (productContainer != null && productContainer.getProductData().getProducts().size() > 0) {

                productList.addAll(productContainer.getProductData().getProducts());
                recommendationProductAdapter.notifyDataSetChanged();

                isLoading = false;
            }

            fragmentHomeNewBinding.containersParent.setVisibility(View.VISIBLE);
            fragmentHomeNewBinding.pbProductLoad.setVisibility(View.GONE);
            fragmentHomeNewBinding.pbLoading.setVisibility(View.GONE);

        });
    }

    private void setBannerSlider() {

        if (fragmentHomeNewBinding.dots.getChildCount() > 0)
            fragmentHomeNewBinding.dots.removeAllViews();

        BannerImageAdapter bannerImageAdapter = new BannerImageAdapter(getContext(), bannerList);
        fragmentHomeNewBinding.vpImages.setAdapter(bannerImageAdapter);


        //setting viewpagger height because in scrollview wrap/match does not calculate their height correctly
        android.view.Display display = ((android.view.WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        fragmentHomeNewBinding.vpImages.getLayoutParams().height = ((int) (display.getHeight() * 0.35));
        fragmentHomeNewBinding.vpImages.getLayoutParams().width = ((int) (display.getWidth() * 1.0));

        currentPage = 0;
        addDots();


     /*   SliderAdapterExample adapter = new SliderAdapterExample(getContext(), bannerList);

        fragmentHomeNewBinding.imageSlider.setSliderAdapter(adapter);

       fragmentHomeNewBinding.imageSlider.setIndicatorAnimation(IndicatorAnimations.FILL); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
       fragmentHomeNewBinding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
       fragmentHomeNewBinding.imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
       fragmentHomeNewBinding.imageSlider.setIndicatorSelectedColor(getResources().getColor(R.color.colorPrimary));
       fragmentHomeNewBinding.imageSlider.setIndicatorUnselectedColor(Color.GRAY);
       fragmentHomeNewBinding.imageSlider.setScrollTimeInSec(1); //set scroll delay in seconds :
       fragmentHomeNewBinding.imageSlider.startAutoCycle();*/


        /*
        if (fragmentHomeNewBinding.vfSlider.getChildCount() > 0)
            fragmentHomeNewBinding.vfSlider.removeAllViews();

        for (int i = 0; i < bannerList.size(); i++) {

            String bannerImageUrl = BANNER_STORAGE_BASE_URL.concat(bannerList.get(i).getBannerImage());
            ImageView imageView = new ImageView(getContext());

            imageView.setAdjustViewBounds(true);

            Glide
                    .with(getContext())
                    .load(bannerImageUrl)
                    .fitCenter()
                    .into(imageView);

            fragmentHomeNewBinding.vfSlider.addView(imageView);
            fragmentHomeNewBinding.vfSlider.setFlipInterval(4000);
            fragmentHomeNewBinding.vfSlider.setAutoStart(true);

            fragmentHomeNewBinding.vfSlider.setInAnimation(getContext(), android.R.anim.slide_in_left);
            fragmentHomeNewBinding.vfSlider.setInAnimation(getContext(), R.anim.anim_slide_in_left_slider);
            fragmentHomeNewBinding.vfSlider.setOutAnimation(getContext(), R.anim.anim_slide_out_left_slider);
        }

        fragmentHomeNewBinding.vfSlider.startFlipping();*/
    }

    private void setActions() {


        if (fragmentHomeNewBinding.rvActionContainer.getChildCount() > 0)
            fragmentHomeNewBinding.rvActionContainer.removeAllViews();

        Integer weight = (100 / actionsList.size());

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

            layout.setAction(actionsList.get(i));

            layout.actionParent.setClickable(true);
            layout.ivAction.setOnClickListener(v -> onActionClicked(Integer.parseInt(v.getTag(R.string.action_tag).toString())));

            fragmentHomeNewBinding.rvActionContainer.addView(layout.getRoot(), param);


        }
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

        if (fragmentHomeNewBinding.pickOfWeekContainerParent.rvPicksOfWeekContainer.getChildCount() > 0)
            fragmentHomeNewBinding.pickOfWeekContainerParent.rvPicksOfWeekContainer.removeAllViews();

        Integer weight = (90 / 3);

        for (int i = 0; i < 3; i++) {

            PickOfWeekItemBinding layout = DataBindingUtil.inflate(getLayoutInflater(), R.layout.pick_of_week_item, null, false);
            layout.ivAction.setTag(R.string.pick_of_week_tag, i);

            if (layout.actionParent.getParent() != null)
                layout.actionParent.removeView(layout.actionParent);

            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    weight
            );

            layout.setPicks(picksOfTheWeekList.get(i));
            layout.ivAction.setOnClickListener(v -> onWeekProductClicked(Integer.parseInt(v.getTag(R.string.pick_of_week_tag).toString())));
            fragmentHomeNewBinding.pickOfWeekContainerParent.rvPicksOfWeekContainer.addView(layout.getRoot(), param);

        }
    }

    public void showAllProductsWeekProducts() {

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
        fragmentHomeNewBinding.brandsInFocusContainerParent.rvBrandsInFocus.setAdapter(brandsFocusAdapter);
        fragmentHomeNewBinding.brandsInFocusContainerParent.rvBrandsInFocus.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        fragmentHomeNewBinding.brandsInFocusContainerParent.rvBrandsInFocus.setHasFixedSize(true);
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
        fragmentHomeNewBinding.recommendationContainerParent.rvRecommendedProducts.setAdapter(recommendationProductAdapter);
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        fragmentHomeNewBinding.recommendationContainerParent.rvRecommendedProducts.setLayoutManager(gridLayoutManager);
        fragmentHomeNewBinding.recommendationContainerParent.rvRecommendedProducts.setItemAnimator(new DefaultItemAnimator());

        // just to remove lag from recommendation products adapter
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            fragmentHomeNewBinding.brandsInFocusContainerParent.rvBrandsInFocus.setNestedScrollingEnabled(false);
            fragmentHomeNewBinding.getTheLooksContainerParent.rvGetTheLooks.setNestedScrollingEnabled(false);
            fragmentHomeNewBinding.blogsContainerParent.rvBlogs.setNestedScrollingEnabled(false);
            fragmentHomeNewBinding.recommendationContainerParent.rvRecommendedProducts.setNestedScrollingEnabled(false);

        } else {

            ViewCompat.setNestedScrollingEnabled(fragmentHomeNewBinding.brandsInFocusContainerParent.rvBrandsInFocus, false);
            ViewCompat.setNestedScrollingEnabled(fragmentHomeNewBinding.getTheLooksContainerParent.rvGetTheLooks, false);
            ViewCompat.setNestedScrollingEnabled(fragmentHomeNewBinding.blogsContainerParent.rvBlogs, false);
            ViewCompat.setNestedScrollingEnabled(fragmentHomeNewBinding.recommendationContainerParent.rvRecommendedProducts, false);
        }

        // It takes almost 3 4 days just because, recyclerview is under nestedScrollView (onScrolled does not call)
        fragmentHomeNewBinding.containersParent.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (v.getChildAt(v.getChildCount() - 1) != null) {
                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                        scrollY > oldScrollY) {
                    //code to fetch more data for endless scrolling


                    if (v.getChildCount() > 10) {
                        fragmentHomeNewBinding.btnScrollUp.setVisibility(View.VISIBLE);
                    }

                    if (!isLoading) {

                        fragmentHomeNewBinding.pbProductLoad.setVisibility(View.VISIBLE);
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

                if (scrollY > 5000) {
                    fragmentHomeNewBinding.btnScrollUp.setVisibility(View.VISIBLE);
                } else {
                    fragmentHomeNewBinding.btnScrollUp.setVisibility(View.GONE);
                }
            }
        });


        parameters.put(START_INDEX, String.valueOf(INITIAL_INDEX));
        parameters.put(LENGTH, String.valueOf(END_INDEX)); // multiple of 3 due to 3 products are listing in a row
        getProductItems();

    }

    void setFeatureCategory() {

        FeatureCategoryAdapter featureCategoryAdapter = new FeatureCategoryAdapter(getContext(), featuredCategory -> {

            Intent intent = new Intent(getContext(), ActionProductListingActivity.class);

            intent.putExtra(ACTION_NAME, featuredCategory.getName());
            intent.putExtra(ACTION_ID, String.valueOf(featuredCategory.getId()));
            intent.putExtra(PRODUCT_TYPE, "category");
            startActivity(intent);
        });

        fragmentHomeNewBinding.categoryInFocusContainerParent.rvCategoryInFocus.setAdapter(featureCategoryAdapter);
        fragmentHomeNewBinding.categoryInFocusContainerParent.rvCategoryInFocus.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        fragmentHomeNewBinding.categoryInFocusContainerParent.rvCategoryInFocus.setHasFixedSize(true);
        featureCategoryAdapter.setData(featuredCategories);
    }


    @Override
    public void onProductProductClicked(Product product) {

        Intent intent = new Intent(getContext(), ProductDetailActivity.class);
        intent.putExtra(PRODUCT_ID, product.getId());
        startActivity(intent);
    }

    public void addDots() {


/*        if (fragmentHomeNewBinding.dots.getChildCount() > 0)
            fragmentHomeNewBinding.dots.removeAllViews();*/

        dots.clear();

        if (bannerList.size() < 2) {
            fragmentHomeNewBinding.dots.setVisibility(View.GONE);
            return;
        }

        for (int i = 0; i <= bannerList.size() - 1; i++) {
            ImageView dot = new ImageView(getContext());
            dot.setImageDrawable(getResources().getDrawable(R.drawable.empty_intro_circle));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );


            fragmentHomeNewBinding.dots.addView(dot, params);

            dot.setTag(R.string.banner_circle_tag, i);

            dot.setOnClickListener(v -> {

                onBannerCircleClicked(Integer.parseInt(v.getTag(R.string.banner_circle_tag).toString()));
            });

            dots.add(dot);
        }

        selectDot(0);

        fragmentHomeNewBinding.vpImages.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                currentPage = position;
                selectDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void onBannerCircleClicked(int parseInt) {
        currentPage = parseInt;
        fragmentHomeNewBinding.vpImages.setCurrentItem(parseInt);
    }

    public void selectDot(int idx) {

        if (getContext() == null)
            return;

        Resources res = getResources();
        for (int i = 0; i <= bannerList.size() - 1; i++) {
            int drawableId = (i == idx) ? (R.drawable.filled_circle) : (R.drawable.empty_intro_circle);
            Drawable drawable = res.getDrawable(drawableId);
            dots.get(i).setImageDrawable(drawable);
        }
    }


    private void setupAutoPager() {
        final Handler handler = new Handler();

        final Runnable update = () -> {

            fragmentHomeNewBinding.vpImages.setCurrentItem(currentPage, true);
            if (currentPage == bannerList.size()) {
                currentPage = 0;
            } else {

                ++currentPage;
            }
        };


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 1000, 3500);
    }

}
