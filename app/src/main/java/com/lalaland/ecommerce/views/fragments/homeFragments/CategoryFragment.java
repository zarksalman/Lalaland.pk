package com.lalaland.ecommerce.views.fragments.homeFragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.adapters.CategoryAdapter;
import com.lalaland.ecommerce.adapters.MajorCategoryAdapter;
import com.lalaland.ecommerce.data.models.categories.CategoryHomeBanner;
import com.lalaland.ecommerce.data.models.categories.SubCategory;
import com.lalaland.ecommerce.data.models.category.Category;
import com.lalaland.ecommerce.databinding.FragmentCategoryBinding;
import com.lalaland.ecommerce.helpers.AppConstants;
import com.lalaland.ecommerce.viewModels.categories.CategoriesViewModel;
import com.lalaland.ecommerce.viewModels.products.CategoryViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;


public class CategoryFragment extends Fragment implements MajorCategoryAdapter.MajorCategoryClickListener, CategoryAdapter.CategoryListener {

    private FragmentCategoryBinding fragmentCategoryBinding;
    private CategoryViewModel categoryViewModel;
    private List<Category> categoryList = new ArrayList<>();
    private List<CategoryHomeBanner> categoryHomeBanners = new ArrayList<>();
    private List<SubCategory> subCategories = new ArrayList<>();

    private MajorCategoryAdapter majorCategoryAdapter;
    private CategoryAdapter categoryAdapter;
    private CategoriesViewModel categoriesViewModel;


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

//        initUi();
        return fragmentCategoryBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);

        Glide
                .with(getContext())
                .load(AppConstants.testImagesUrl.get(0))
                .into(fragmentCategoryBinding.ivCategoryHeader);


        setMajorCategoryList();
        setCategoryAdapter();
    }

    private void getCategories(int majorCategoryId) {

        String firstMajorCategoryId = String.valueOf(majorCategoryId);
        categoriesViewModel.getCategories(firstMajorCategoryId).observe(this, categoriesContainer -> {

            if (categoriesContainer != null) {

                if (categoriesContainer.getCode().equals(SUCCESS_CODE)) {

                    if (categoriesContainer.getData().getSubCategories().size() > 0) {
                        categoryHomeBanners.addAll(categoriesContainer.getData().getHomeBanner());
                        subCategories.addAll(categoriesContainer.getData().getSubCategories());
                        categoryAdapter.notifyItemRangeChanged(0, subCategories.size());
                    }
                }
            }
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

        getCategories(categoryList.get(0).getId());

    }

    @Override
    public void onMajorCategoryClicked(Category category) {

        subCategories.clear();
        getCategories(category.getId());
    }

    @Override
    public void onCategoryClicked(SubCategory subCategory) {

    }
}
