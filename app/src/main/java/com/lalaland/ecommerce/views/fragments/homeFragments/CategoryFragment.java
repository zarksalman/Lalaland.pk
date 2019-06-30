package com.lalaland.ecommerce.views.fragments.homeFragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.adapters.CategorBrandAdapter;
import com.lalaland.ecommerce.adapters.CategoryAdapter;
import com.lalaland.ecommerce.adapters.MajorCategoryAdapter;
import com.lalaland.ecommerce.data.models.categories.CategoryHomeBanner;
import com.lalaland.ecommerce.data.models.categories.InnerCategory;
import com.lalaland.ecommerce.data.models.categories.SubCategory;
import com.lalaland.ecommerce.data.models.category.Category;
import com.lalaland.ecommerce.data.models.category.CategoryBrand;
import com.lalaland.ecommerce.databinding.FragmentCategoryBinding;
import com.lalaland.ecommerce.helpers.AppConstants;
import com.lalaland.ecommerce.viewModels.categories.CategoriesViewModel;
import com.lalaland.ecommerce.viewModels.products.CategoryViewModel;
import com.lalaland.ecommerce.views.activities.ActionProductListingActivity;

import java.util.ArrayList;
import java.util.List;

import static com.lalaland.ecommerce.helpers.AppConstants.ACTION_ID;
import static com.lalaland.ecommerce.helpers.AppConstants.ACTION_NAME;
import static com.lalaland.ecommerce.helpers.AppConstants.BANNER_STORAGE_BASE_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.BRANDS_IN_FOCUS_PRODUCTS;
import static com.lalaland.ecommerce.helpers.AppConstants.CATEGORY_PRODUCTS;
import static com.lalaland.ecommerce.helpers.AppConstants.PRODUCT_TYPE;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;


public class CategoryFragment extends Fragment implements MajorCategoryAdapter.MajorCategoryClickListener,
        CategoryAdapter.CategoryListener, CategorBrandAdapter.CategoryBrandListener {

    private FragmentCategoryBinding fragmentCategoryBinding;
    private CategoryViewModel categoryViewModel;
    private List<Category> categoryList = new ArrayList<>();
    private List<CategoryHomeBanner> categoryHomeBanners = new ArrayList<>();
    private List<SubCategory> subCategories = new ArrayList<>();
    private List<Category> subCategoriesBrand = new ArrayList<>();

    private MajorCategoryAdapter majorCategoryAdapter;
    private CategoryAdapter categoryAdapter;
    private CategorBrandAdapter categorBrandAdapter;
    private CategoriesViewModel categoriesViewModel;
    private int categoryId;

    public CategoryFragment() {
        // Required empty public constructor
    }


    public static CategoryFragment newInstance() {
        CategoryFragment fragment = new CategoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentCategoryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_category, container, false);
        categoriesViewModel = ViewModelProviders.of(this).get(CategoriesViewModel.class);
        return fragmentCategoryBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);

        //setting viewpagger height because in scrollview wrap/match does not calculate their height correctly
        android.view.Display display = ((android.view.WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        fragmentCategoryBinding.ivCategoryHeader.getLayoutParams().height = ((int) (display.getHeight() * 0.16));


    }

    @Override
    public void onResume() {
        super.onResume();

        setMajorCategoryList();
        setCategoryAdapter();
    }

    private void getCategories(int majorCategoryId) {

        String firstMajorCategoryId = String.valueOf(majorCategoryId);
        categoriesViewModel.getCategories(firstMajorCategoryId).observe(this, categoriesContainer -> {


            if (categoriesContainer != null) {

                if (categoriesContainer.getCode().equals(SUCCESS_CODE)) {

                    if (categoriesContainer.getData().getSubCategories().size() > 0) {

                        subCategories.clear();
                        categoryHomeBanners.clear();

                        categoryHomeBanners.addAll(categoriesContainer.getData().getHomeBanner());
                        subCategories.addAll(categoriesContainer.getData().getSubCategories());
                        trimZeroSizeInnerCategories();

                        fragmentCategoryBinding.subCategoryContainer.setVisibility(View.VISIBLE);
                        categoryAdapter.notifyDataSetChanged();

                        String bannerImageUrl = BANNER_STORAGE_BASE_URL.concat(categoryHomeBanners.get(0).getBannerImage());
                        Glide.with(getContext())
                                .load(bannerImageUrl)
                                .placeholder(R.drawable.placeholder_products)
                                .into(fragmentCategoryBinding.ivCategoryHeader);

                    }
                }
            }

            fragmentCategoryBinding.pbLoading.setVisibility(View.GONE);
        });

    }

    private void setMajorCategoryList() {

        categoryList = new ArrayList<>();
        categoryList = AppConstants.staticCategoryList;
        majorCategoryAdapter = new MajorCategoryAdapter(getContext(), this);
        fragmentCategoryBinding.rvCategory.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        fragmentCategoryBinding.rvCategory.setAdapter(majorCategoryAdapter);
        fragmentCategoryBinding.rvCategory.setHasFixedSize(true);
        majorCategoryAdapter.setData(categoryList);
    }

    private void setCategoryAdapter() {

        categoryAdapter = new CategoryAdapter(getContext(), this);
        fragmentCategoryBinding.rvSubCategory.setAdapter(categoryAdapter);
        fragmentCategoryBinding.rvSubCategory.setLayoutManager(new LinearLayoutManager(getContext()));
        categoryAdapter.setData(subCategories);

        categorBrandAdapter = new CategorBrandAdapter(getContext(), this);
        fragmentCategoryBinding.rvSubCategoryBrand.setHasFixedSize(true);
        fragmentCategoryBinding.rvSubCategoryBrand.setAdapter(categorBrandAdapter);
        fragmentCategoryBinding.rvSubCategoryBrand.setLayoutManager(new LinearLayoutManager(getContext()));
        categorBrandAdapter.setData(AppConstants.staticCategoryBrandsList);

        if (categoryList.size() > 0)
            getCategories(categoryList.get(0).getId());
        else
            Toast.makeText(getContext(), "No category found", Toast.LENGTH_SHORT).show();

        fragmentCategoryBinding.rvSubCategory.setVisibility(View.VISIBLE);
    }

    private void trimZeroSizeInnerCategories() {

        List<SubCategory> subCategoryArrayList = new ArrayList<>();
        subCategoryArrayList.addAll(subCategories);

        for (int i = 0; i < subCategories.size(); i++) {

            if (subCategories.get(i).getInnerCategories().size() <= 0) {
                subCategoryArrayList.remove(subCategories.get(i));
            }
        }

        subCategories.clear();
        subCategories.addAll(subCategoryArrayList);
    }

    @Override
    public void onMajorCategoryClicked(Category category) {

        // if same category do not clicked again
        if (categoryId != category.getId()) {


            //       fragmentCategoryBinding.subCategoryContainer.setVisibility(View.GONE);

/*            fragmentCategoryBinding.rvSubCategoryBrand.setVisibility(View.GONE);
            fragmentCategoryBinding.pbLoading.setVisibility(View.VISIBLE);*/

            if (category.getName().equals("Brands")) {

                fragmentCategoryBinding.ivCategoryHeader.setVisibility(View.GONE);
                fragmentCategoryBinding.rvSubCategory.setVisibility(View.GONE);

                fragmentCategoryBinding.rvSubCategoryBrand.setVisibility(View.VISIBLE);

//                fragmentCategoryBinding.subCategoryContainer.setVisibility(View.VISIBLE);

            } else if (category.getName().equals("Sale")) {
                Intent intent;
                intent = new Intent(getContext(), ActionProductListingActivity.class);
                intent.putExtra(ACTION_NAME, category.getName());
                intent.putExtra(ACTION_ID, String.valueOf(category.getId()));
                intent.putExtra(PRODUCT_TYPE, category.getName());
                startActivity(intent);

            } else {

                fragmentCategoryBinding.ivCategoryHeader.setVisibility(View.VISIBLE);
                fragmentCategoryBinding.rvSubCategory.setVisibility(View.VISIBLE);

                fragmentCategoryBinding.rvSubCategoryBrand.setVisibility(View.GONE);

                categoryId = category.getId();
                getCategories(categoryId);
            }
        }
    }

    @Override
    public void onCategoryClicked(SubCategory subCategory) {

        loadCategoryProducts(subCategory.getId(), subCategory.getName());
    }

    @Override
    public void onInnerCategoryClicked(InnerCategory innerCategory) {
        loadCategoryProducts(innerCategory.getId(), innerCategory.getName());
    }

    void loadCategoryProducts(int categoryId, String categoryName) {

        Intent intent = new Intent(getContext(), ActionProductListingActivity.class);

        intent.putExtra(ACTION_NAME, categoryName);
        intent.putExtra(ACTION_ID, String.valueOf(categoryId));
        intent.putExtra(PRODUCT_TYPE, CATEGORY_PRODUCTS);
        startActivity(intent);
    }

    @Override
    public void onCategoryBrandClicked(CategoryBrand categoryBrand) {

        Intent intent = new Intent(getContext(), ActionProductListingActivity.class);
        intent.putExtra(ACTION_NAME, categoryBrand.getName());
        intent.putExtra(ACTION_ID, String.valueOf(categoryBrand.getId()));
        intent.putExtra(PRODUCT_TYPE, BRANDS_IN_FOCUS_PRODUCTS);
        startActivity(intent);
    }
}
